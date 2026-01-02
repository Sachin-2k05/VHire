package com.example.vHire.repository;

import com.example.vHire.entity.Role;
import com.example.vHire.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import org.springframework.data.domain.Pageable;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {


    Optional<User> findById(long workerId);

    Optional <User> findByEmail(String email);

    boolean existsByEmail(String email);

    Page<User> findAllByRoleAndCity(Role role , String city, Pageable pageable);

    Page<User> findByRole(Role role , Pageable pageable);

}
