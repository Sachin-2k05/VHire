package com.example.vHire.service;

import com.example.vHire.Config.EmailTemplates;
import com.example.vHire.dto_Layer.BookingDto.BookingResponseDto;
import com.example.vHire.dto_Layer.BookingDto.CreateBookingDto;
import com.example.vHire.entity.*;
import com.example.vHire.repository.AvailabilitySlotRepository;
import com.example.vHire.repository.BookingRepository;
import com.example.vHire.repository.UserRepository;
import com.example.vHire.repository.WorkerProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


@Service
@Transactional
public class BookingService{

    private final EmailService emailService;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final WorkerProfileRepository workerProfileRepository;
    private final AvailabilitySlotRepository availabilitySlotRepository;

    public BookingService(UserRepository userRepository,
                          BookingRepository bookingRepository,
                          WorkerProfileRepository workerProfileRepository,
                          AvailabilitySlotRepository availabilitySlotRepository,
                          EmailService emailService
                          ) {
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.workerProfileRepository = workerProfileRepository;
        this.availabilitySlotRepository = availabilitySlotRepository;
        this.emailService = emailService;

    }





    public BookingResponseDto createBooking(User company, CreateBookingDto bookingRequest) throws Throwable {



        User worker = userRepository.findById(bookingRequest.getWorkerId())
                .orElseThrow(() -> new IllegalArgumentException("Worker not found"));



        if (!bookingRequest.getStartTime().isBefore(bookingRequest.getEndTime())) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        if (bookingRequest.getDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Date cannot be in the past");
        }

        validateWorkerAvailability(
                worker,
                bookingRequest.getDate(),
                bookingRequest.getStartTime(),
                bookingRequest.getEndTime()
        );



        boolean conflictExists = bookingRepository.existsAcceptedOverlap(
                worker,
                bookingRequest.getDate(),
                bookingRequest.getStartTime(),
                bookingRequest.getEndTime()
        );

        if (conflictExists) {
            throw new IllegalArgumentException("Booking conflict exists");
        }

        Bookings booking = new Bookings();
        booking.setCompany(company);
        booking.setWorker(worker);
        booking.setDate(bookingRequest.getDate());
        booking.setStartTime(bookingRequest.getStartTime());
        booking.setEndTime(bookingRequest.getEndTime());
        booking.setStatus(BookingStatus.REQUESTED);
        booking.setCreatedAt(LocalDateTime.now());
        booking.setResponseDeadline(LocalDateTime.now().plusDays(2));

        bookingRepository.save(booking);
        emailService.sendHtmlEmail(
                worker.getEmail(),
                "New Booking Request",
                EmailTemplates.bookingRequest(
                        company.getName(),
                        booking.getDate().toString(),
                        booking.getStartTime() + " - " + booking.getEndTime()
                )
        );

        return mapToBookingResponse(booking);
    }




    private BookingResponseDto mapToBookingResponse(Bookings booking) {
        BookingResponseDto dto = new BookingResponseDto();
        dto.setWorkerName(booking.getWorker().getName());
        dto.setBookingId(booking.getId());
        dto.setCompanyName(booking.getCompany().getName());
        dto.setStartTime(booking.getStartTime());
        dto.setEndTime(booking.getEndTime());
        dto.setDate(booking.getDate());
        dto.setBookingDate(booking.getDate());
        dto.setCreateAt(booking.getCreatedAt());

        dto.setStatus(booking.getStatus().name());
        return dto;
    }






    @Transactional
    public BookingResponseDto acceptBooking(Long bookingId, User worker) {



        Bookings booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));


        if (booking.getWorker().getId() != worker.getId()) {
            throw new IllegalArgumentException("This booking does not belong to the worker");
        }


        if (booking.getStatus() != BookingStatus.REQUESTED) {
            throw new IllegalStateException("Booking is not in REQUESTED state");
        }

        if (LocalDateTime.now().isAfter(booking.getResponseDeadline())){
            booking.setStatus(BookingStatus.AUTO_REJECTED);
            bookingRepository.save(booking);
            throw new IllegalStateException("Booking request has expired");
        }

        boolean conflictExists =
                bookingRepository.existsAcceptedOverlap(
                        worker,
                        booking.getDate(),
                        booking.getStartTime(),
                        booking.getEndTime()
                );

        if (conflictExists) {
            throw new IllegalStateException(
                    "Cannot accept booking due to overlapping accepted booking"
            );
        }

        booking.setStatus(BookingStatus.ACCEPTED);
        bookingRepository.save(booking);
        emailService.sendHtmlEmail(
                booking.getCompany().getEmail(),
                "Booking Accepted",
                EmailTemplates.bookingAccepted(
                        booking.getWorker().getName(),
                        booking.getDate().toString(),
                        booking.getStartTime() + " - " + booking.getEndTime()
                )
        );

        return mapToBookingResponse(booking);
    }

     @Transactional
    public BookingResponseDto rejectBooking(Long bookingId, User worker) {


        Bookings booking = bookingRepository.findById(bookingId).orElseThrow(()-> new IllegalArgumentException("Booking not found"));

        if(booking.getWorker().getId()!= (worker.getId())){
            throw new IllegalArgumentException("you are not authorised to reject this booking ");


        }

        if(booking.getStatus() != BookingStatus.REQUESTED){
            throw new IllegalStateException("only requested bookings can be rejected ");

        }

        booking.setStatus(BookingStatus.REJECTED_BY_WORKER);
        bookingRepository.save(booking);
         emailService.sendHtmlEmail(
                 booking.getCompany().getEmail(),
                 "Booking Rejected",
                 EmailTemplates.bookingRejected(
                         booking.getWorker().getName()
                 )
         );
        return mapToBookingResponse(booking);



    }


    public BookingResponseDto cancelBooking(Long bookingId, User company) {


        Bookings bookings = bookingRepository.findById(bookingId).orElseThrow(()-> new IllegalArgumentException("Booking not found"));


        if(!bookings.getCompany().getId().equals (company.getId())){
            throw new IllegalArgumentException("onlly companies can cancel this booking ");

        }

        if(bookings.getStatus() == BookingStatus.COMPLETED){
            throw new IllegalStateException("completed bookings cannot be cancelled ");


        }

        bookings.setStatus(BookingStatus.CANCELLED_BY_COMPANY);
        bookingRepository.save(bookings);
        return mapToBookingResponse(bookings);


    }


    public BookingResponseDto getBookingById(long bookingId , User user) {
        Bookings booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Booking not found with id: " + bookingId
                        )
                );

        return mapToBookingResponse(booking);
    }

    @Transactional
    public void AutoRejectExpiredBookings(){
        LocalDateTime cutoff= LocalDateTime.now().plusDays(2);

        List<Bookings> expiredBookings  = bookingRepository.findExpiredRequestedBookings(cutoff) ;
        for(Bookings booking : expiredBookings){
            booking.setStatus(BookingStatus.AUTO_REJECTED);

        }
        bookingRepository.saveAll(expiredBookings);
    }


    public Page<BookingResponseDto> getCompanyBookings(
            User company,
            Pageable pageable
    ) {
        return bookingRepository
                .findByCompany(company, pageable)
                .map(this::mapToBookingResponse);
    }

    public Page<BookingResponseDto> getWorkerBookings(
            User worker,
            Pageable pageable
    ) {
        return bookingRepository
                .findByWorker(worker, pageable)
                .map(this::mapToBookingResponse);
    }
    public Availability_slot validateWorkerAvailability(
            User worker,
            LocalDate bookingDate,
            LocalTime startTime,
            LocalTime endTime
    ) {

        List<Availability_slot> slots =
                availabilitySlotRepository
                        .findByWorkerAndDateOrderByStartTime(worker, bookingDate);

        if (slots.isEmpty()) {
            throw new IllegalStateException("Worker not available on this date");
        }

        Availability_slot coveringSlot = slots.stream()
                .filter(slot ->
                        !slot.getStartTime().isAfter(startTime) &&
                                !slot.getEndTime().isBefore(endTime)
                )
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException("Worker not available for selected time slot")
                );


        boolean alreadyBooked =
                bookingRepository.existsByWorkerAndDateAndStartTimeLessThanAndEndTimeGreaterThan(  worker,
                        bookingDate,
                        endTime,
                        startTime);

        if (alreadyBooked) {
            throw new IllegalStateException("Selected slot is already booked");
        }

        return coveringSlot;
    }



}
