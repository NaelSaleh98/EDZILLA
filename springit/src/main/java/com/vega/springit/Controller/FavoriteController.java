package com.vega.springit.Controller;

import com.vega.springit.Repository.*;
import com.vega.springit.model.Course;
import com.vega.springit.model.FavoriteCourse;
import com.vega.springit.model.User;
import com.vega.springit.service.CourseCardService;
import com.vega.springit.service.CourseService;
import com.vega.springit.service.RecommendationService;
import com.vega.springit.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
public class FavoriteController {

    private CourseRepository courseRepository;
    private FavoriteCourseRepository favoriteCourseRepository;
    private UserRepository userRepository;
    private UserService userService;

    public FavoriteController(CourseRepository courseRepository, FavoriteCourseRepository favoriteCourseRepository, UserRepository userRepository, UserService userService) {
        this.courseRepository = courseRepository;
        this.favoriteCourseRepository = favoriteCourseRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/addtofavorite/course/id/{courseID}")
    public void addCourseToFavorite (@PathVariable Long courseID){

         Optional<Course> optionalCourse =courseRepository.findById(courseID);
         if(userService.isLogged()){
             Optional<User> optionalUser = userRepository.findByEmail(userService.loggedInUserEmail());

             if(optionalUser.isPresent() && optionalCourse.isPresent()){
                 FavoriteCourse favoriteCourse = new FavoriteCourse(optionalCourse.get(),optionalUser.get());
                 favoriteCourseRepository.save(favoriteCourse);
             }
         }


    }

    @GetMapping("/removefromfavorite/course/id/{courseID}")
    public void removeCourseFromFavorite (@PathVariable Long courseID){

        Optional<Course> optionalCourse =courseRepository.findById(courseID);
        if(userService.isLogged()){
            Optional<User> optionalUser = userRepository.findByEmail(userService.loggedInUserEmail());

            if(optionalUser.isPresent() && optionalCourse.isPresent()){
                Optional<FavoriteCourse> favoriteCourseOptional = favoriteCourseRepository.findByUserIdAndCourseId(optionalUser.get().getId(),optionalCourse.get().getId());
                if(favoriteCourseOptional.isPresent())
                favoriteCourseRepository.delete(favoriteCourseOptional.get());

            }
        }
    }


}
