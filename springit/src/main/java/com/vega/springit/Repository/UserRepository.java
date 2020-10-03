package com.vega.springit.Repository;

import com.vega.springit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
//ON
public interface UserRepository extends JpaRepository<User,Long> {

    public Optional<User> findByEmail(String email);

    public  Optional<User> findByAlias(String alias);
    Optional<User> findByEmailAndActivationCode(String email, String activationCode);
    List<User> findAllByEmailNot(String email);
}
