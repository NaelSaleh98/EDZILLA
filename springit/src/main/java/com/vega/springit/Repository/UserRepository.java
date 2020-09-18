package com.vega.springit.Repository;

import com.vega.springit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
//ON
public interface UserRepository extends JpaRepository<User,Long> {

    public Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndActivationCode(String email, String activationCode);
}
