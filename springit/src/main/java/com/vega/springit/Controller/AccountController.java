package com.vega.springit.Controller;

import com.vega.springit.Repository.CourseRepository;
import com.vega.springit.Repository.UserRepository;
import com.vega.springit.model.Course;
import com.vega.springit.model.User;
import com.vega.springit.service.CourseService;
import com.vega.springit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
public class AccountController {
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CourseService courseService;

    @Secured({"ROLE_USER"})
    @GetMapping("/UserProfile/CurrentUser")
    public String getProfile(Model model) throws Exception {
        User user;
        List<Course> courseList;
        Optional<User> userOptional;


        if(userService.isLogged()){
            //get user courses
            userOptional = userRepository.findByEmail(userService.loggedInUserEmail());

            if(userOptional.isPresent()){
                user = userOptional.get();
                courseList = courseRepository.findByUserId(user.getId());
                model.addAttribute("userCourseList", courseService.addIsFavoriteIsUpIsDownAttributes(courseList));
                return "auth/profile";
            }

        }
        throw new Exception();

    }



}
