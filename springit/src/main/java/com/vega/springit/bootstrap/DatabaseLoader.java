package com.vega.springit.bootstrap;

import com.vega.springit.Repository.CommentRepository;
import com.vega.springit.Repository.CourseRepository;
import com.vega.springit.Repository.RoleRepository;
import com.vega.springit.Repository.UserRepository;
import com.vega.springit.model.Comment;
import com.vega.springit.model.Course;
import com.vega.springit.model.Role;
import com.vega.springit.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Component
public class DatabaseLoader implements CommandLineRunner {

    private CourseRepository courseRepository;
    private CommentRepository commentRepository;
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    private Map<String,User> users = new HashMap<>();

    public DatabaseLoader(CourseRepository courseRepository, CommentRepository commentRepository, UserRepository userRepository, RoleRepository roleRepository) {
        this.courseRepository = courseRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {

        // add users and roles
        addUsersAndRoles();

        Map<String,String> courses = new HashMap<>();
        courses.put("Securing Spring Boot APIs and SPAs with OAuth 2.0","Desc");
        courses.put("Easy way to detect Device in Java Web Application using Spring Mobile - Source code to download from GitHub","Desc");
        courses.put("Tutorial series about building microservices with SpringBoot (with Netflix OSS)","Desc");
        courses.put("Detailed steps to send encrypted email using Java / Spring Boot - Source code to download from GitHub","Desc");
        courses.put("Build a Secure Progressive Web App With Spring Boot and React","Desc");
        courses.put("Building Your First Spring Boot Web Application - DZone Java","Desc");
        courses.put("Building Microservices with Spring Boot Fat (Uber) Jar","Desc");
        courses.put("Spring Cloud GCP 1.0 Released","Desc");
        courses.put("Simplest way to Upload and Download Files in Java with Spring Boot - Code to download from Github","Desc");
        courses.put("Add Social Login to Your Spring Boot 2.0 app","Desc");
        courses.put("File download example using Spring REST Controller","Desc");

        courses.forEach((k,v) -> {
            User u1 = users.get("user@gmail.com");
            User u2 = users.get("super@gmail.com");
            Course course = new Course(k,v);
            if(k.startsWith("Build")) {
                course.setUser(u1);
            } else {
                course.setUser(u2);
            }

            courseRepository.save(course);

            // we will do something with comments later
            Comment spring = new Comment("Thank you for this Course related to Spring Boot. I love it, great post!", course);
            Comment security = new Comment("I love that you're talking about Spring Security", course);
            Comment pwa = new Comment("What is this Progressive Web App thing all about? PWAs sound really cool.", course);
            Comment comments[] = {spring,security,pwa};
            for(Comment comment : comments) {
                commentRepository.save(comment);
                course.addComment(comment);
            }
        });

        long CourseCount = courseRepository.count();
        System.out.println("Number of Courses in the database: " + CourseCount );
    }

    private void addUsersAndRoles() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String secret = "{bcrypt}" + encoder.encode("password");

        Role userRole = new Role("ROLE_USER");
        roleRepository.save(userRole);
        Role adminRole = new Role("ROLE_ADMIN");
        roleRepository.save(adminRole);

        User user = new User("user@gmail.com",secret,true,"Joe","User","joedirt");
        user.addRole(userRole);
        user.setConfirmPassword(secret);
        userRepository.save(user);
        users.put("user@gmail.com",user);

        User admin = new User("admin@gmail.com",secret,true,"Joe","Admin","masteradmin");
        admin.addRole(adminRole);
        admin.setConfirmPassword(secret);
        userRepository.save(admin);
        users.put("admin@gmail.com",admin);

        User master = new User("super@gmail.com",secret,true,"Super","User","superduper");
        master.addRoles(new HashSet<>(Arrays.asList(userRole,adminRole)));
        master.setConfirmPassword(secret);
        userRepository.save(master);
        users.put("super@gmail.com",master);
    }

}