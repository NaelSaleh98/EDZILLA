package com.vega.springit.Controller;

import com.vega.springit.Repository.CourseRepository;
import com.vega.springit.Repository.UserRepository;
import com.vega.springit.model.Course;
import com.vega.springit.model.EditUser;
import com.vega.springit.model.User;
import com.vega.springit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController

public class SearchController {
    private CourseRepository courseRepository;
    private UserService userService;
    private UserRepository userRepository;

    public SearchController(CourseRepository courseRepository, UserService userService, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/course/search/{courseTitle}")
    public List<Course> search(@PathVariable String courseTitle, Model model){
        List<Course> courses = courseRepository.findByTitleContaining(courseTitle);
        System.out.println(courses.size());
        return courses;
    }

    @GetMapping("profile/setting")
    public User getUser() throws Exception {
        Optional<User> userOptional;
        if(userService.isLogged()){
            userOptional = userRepository.findByEmail(userService.loggedInUserEmail());
            if(userOptional.isPresent()){
                User user =userOptional.get();
                return user;
            }
        }
        throw new Exception();
    }




}
