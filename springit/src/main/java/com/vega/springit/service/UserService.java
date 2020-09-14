package com.vega.springit.service;

import com.vega.springit.Repository.UserRepository;
import com.vega.springit.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private  final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final RoleService roleService;
    private final MailService mailService;

    public UserService(UserRepository userRepository, RoleService roleService, MailService mailService) {
        this.mailService =mailService;
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    public User register (User user){
        String secret ="{bcrypt}"+encoder.encode(user.getPassword());
        user.setConfirmPassword(secret);
        user.setEnabled(false);
        user.setPassword(secret);

        user.addRole(roleService.findByName("ROLE_USER"));

        user.setActivationCode(UUID.randomUUID().toString());//set an activation code
        save(user);

        sendActivationEmail(user);
        return user;
    }

    public void save(User user) {
        userRepository.save(user);
    }

   private void  sendActivationEmail(User user) {
   mailService.sendActivationEmail(user);
   }

    public void sendWelcomeEmail(User user) {
        mailService.sendWelcomeEmail(user);
    }


    public Optional<User> findByEmailAndActivationCode(String email, String activationCode) {
        return userRepository.findByEmailAndActivationCode(email,activationCode);
    }

}