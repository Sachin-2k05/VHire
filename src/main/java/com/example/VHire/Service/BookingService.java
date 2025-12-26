package com.example.VHire.Service;

import com.example.VHire.DTO_Layer.BookingDto.BookingResponseDto;
import com.example.VHire.DTO_Layer.BookingDto.CreateBookingDto;
import com.example.VHire.Entity.BookingStatus;
import com.example.VHire.Entity.Bookings;
import com.example.VHire.Entity.Role;
import com.example.VHire.Entity.User;
import com.example.VHire.Repository.AvailabilitySlotRepository;
import com.example.VHire.Repository.BookingRepository;
import com.example.VHire.Repository.UserRepository;
import com.example.VHire.Repository.WorkerProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Service
@Transactional
public class BookingService{

    private final AvailabilityService availabilityService;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final WorkerProfileRepository workerProfileRepository;
    private final AvailabilitySlotRepository availabilitySlotRepository;

    public BookingService(UserRepository userRepository,
                          BookingRepository bookingRepository,
                          WorkerProfileRepository workerProfileRepository,
                          AvailabilitySlotRepository availabilitySlotRepository,
                          AvailabilityService availabilityService) {
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.workerProfileRepository = workerProfileRepository;
        this.availabilitySlotRepository = availabilitySlotRepository;
        this.availabilityService = availabilityService;
    }





    public BookingResponseDto createBooking(User company, CreateBookingDto bookingRequest) throws Throwable {

        if (company.getRole() != Role.Company) {
            throw new IllegalArgumentException("Only companies can create bookings");
        }

        User worker = userRepository.findById(bookingRequest.getWorker_id())
                .orElseThrow(() -> new IllegalArgumentException("Worker not found"));

        if (worker.getRole() != Role.Worker) {
            throw new IllegalArgumentException("Selected user is not a worker");
        }

        if (!bookingRequest.getStartTime().isBefore(bookingRequest.getEndTime())) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        if (bookingRequest.getDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Date cannot be in the past");
        }

        boolean available = availabilityService.isWorkerAvailable(
                worker,
                bookingRequest.getDate(),
                bookingRequest.getStartTime(),
                bookingRequest.getEndTime()
        );

        if (!available) {
            throw new IllegalArgumentException("Worker not available");
        }

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

        return mapToBookingResponse(booking);
    }




    private BookingResponseDto mapToBookingResponse(Bookings booking) {
        BookingResponseDto dto = new BookingResponseDto();
        dto.setBookingId(booking.getId());
        dto.setCompanyName(booking.getCompany().getName());
        dto.setStartTime(booking.getStartTime());
        dto.setEndTime(booking.getEndTime());
        dto.setDate(booking.getDate());
        dto.setStatus(booking.getStatus().name());
        return dto;
    }






    @Transactional
    public BookingResponseDto acceptBooking(long bookingId, User worker) {

        if (worker.getRole() != Role.Worker) {
            throw new IllegalArgumentException("Only workers can accept bookings");
        }

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

        return mapToBookingResponse(booking);
    }

     @Transactional
    public BookingResponseDto rejectBooking(long bookingId, User worker) {
        if (worker.getRole() != Role.Worker) {
            throw new IllegalArgumentException("Only workers can reject bookings");
        }
        Bookings booking = bookingRepository.findById(bookingId).orElseThrow(()-> new IllegalArgumentException("Booking not found"));

        if(booking.getWorker().getId()!= (worker.getId())){
            throw new IllegalArgumentException("you are not authorised to reject this booking ");


        }

        if(booking.getStatus() != BookingStatus.REQUESTED){
            throw new IllegalStateException("only requested bookings can be rejected ");

        }

        booking.setStatus(BookingStatus.REJECTED_BY_WORKER);
        bookingRepository.save(booking);
        return mapToBookingResponse(booking);



    }


    public BookingResponseDto cancelBooking(long bookingId, User company) {
        if(company.getRole() != Role.Company){
            throw new IllegalArgumentException("Only companies can cancel bookings");
        }

        Bookings bookings = bookingRepository.findById(bookingId).orElseThrow(()-> new IllegalArgumentException("Booking not found"));


        if(bookings.getCompany().getId() != (company.getId())){
            throw new IllegalArgumentException("onlly companies can cancel this booking ");

        }

        if(bookings.getStatus() != BookingStatus.COMPLETED){
            throw new IllegalStateException("completed bookings cannot be cancelled ");


        }

        bookings.setStatus(BookingStatus.CANCELLED_BY_COMPANY);
        bookingRepository.save(bookings);
        return mapToBookingResponse(bookings);


    }


    public BookingResponseDto getBookingById(long bookingId) {
        Bookings booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Booking not found with id: " + bookingId
                        )
                );

        return mapToBookingResponse(booking);
    }

    public void AutoRejectExpiredBookings(){
        LocalDateTime now = LocalDateTime.now();

        List<Bookings> expiredBookings  = bookingRepository.findExpiredTRequestedBookings(now) ;
        for(Bookings booking : expiredBookings){
            booking.setStatus(BookingStatus.AUTO_REJECTED);

        }
        bookingRepository.saveAll(expiredBookings);
    }

}
