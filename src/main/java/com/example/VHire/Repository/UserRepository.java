package com.example.VHire.Repository;

import com.example.VHire.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {


    Optional<User> findById(long workerId);
}
