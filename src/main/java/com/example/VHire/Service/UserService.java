package com.example.VHire.Service;


import com.example.VHire.Entity.Role;
import com.example.VHire.Entity.User;
import com.example.VHire.Repository.AvailabilitySlotRepository;
import com.example.VHire.Repository.BookingRepository;
import com.example.VHire.Repository.UserRepository;
import com.example.VHire.Repository.WorkerProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final AvailabilityService availabilityService;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final WorkerProfileRepository workerProfileRepository;
    private final AvailabilitySlotRepository availabilitySlotRepository;

    public UserService(UserRepository userRepository,
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


    public User getUserById(Long userId) {


        return userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException("User not found with id: " + userId));

    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new EntityNotFoundException("User not found with email: " + email));
    }


    public User getWorkerEntityById(Long userId) {
        User user = getUserById(userId);
        validateRole(user, Role.Worker);
        return user;
    }

    public void validateRole(User user, Role expectedRole) {
        if (user.getRole() != expectedRole) {
            throw new IllegalArgumentException(
                    "User role must be " + expectedRole + " but was " + user.getRole()
            );
        }
    }
}

