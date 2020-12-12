package com.vega.springit.Controller;

import com.vega.springit.Repository.CourseRepository;
import com.vega.springit.Repository.UserRepository;
import com.vega.springit.model.Course;
import com.vega.springit.model.EditUser;
import com.vega.springit.model.User;
import com.vega.springit.service.CourseService;
import com.vega.springit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
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
                model.addAttribute("userName", user.getAlias());
                model.addAttribute("EditUser", new EditUser());
                model.addAttribute("favoriteCourseList" , courseService.addIsFavoriteIsUpIsDownAttributes(courseService.getFavoriteCourses()));
                return "auth/profile";
            }

        }
        throw new Exception();
    }

    @PostMapping("/edit/user/details")
    public String EditUserDetails(@Valid EditUser edituser){
        try{
            Optional<User> userOptional;
            User user;

            userOptional = userRepository.findByEmail(userService.loggedInUserEmail());

            if(userOptional.isPresent()) {
                user = userOptional.get();
                user.setAlias(edituser.getAlias());
                user.setEmail(edituser.getEmail());
                user.setFirstName(edituser.getFirstName());
                user.setLastName(edituser.getLastName());
                user.setConfirmPassword(user.getPassword());

                userRepository.save(user);
            }
            return "redirect:/UserProfile/CurrentUser";
        } catch (Exception e){
            return "redirect:/UserProfile/CurrentUser";
        }

    }


}
