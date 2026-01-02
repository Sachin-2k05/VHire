package com.example.vHire.service;


import com.example.vHire.dto_Layer.UserDto.UserResponseDto;
import com.example.vHire.entity.Role;
import com.example.vHire.entity.User;
import com.example.vHire.repository.AvailabilitySlotRepository;
import com.example.vHire.repository.BookingRepository;
import com.example.vHire.repository.UserRepository;
import com.example.vHire.repository.WorkerProfileRepository;
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

        return user;
    }

    public void validateRole(User user, Role expectedRole) {
        if (user.getRole() != expectedRole) {
            throw new IllegalArgumentException(
                    "User role must be " + expectedRole + " but was " + user.getRole()
            );
        }
    }

    public UserResponseDto mapToUserResponse(User user) {

        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());
        dto.setCity(user.getCity());

        return dto;
    }
    public UserResponseDto getUserByIdAsDto(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "User not found with id: " + userId
                        )
                );

        return mapToUserResponse(user);
    }


}

