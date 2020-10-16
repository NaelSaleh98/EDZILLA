package com.vega.springit.service;

import com.vega.springit.Repository.CourseRepository;
import com.vega.springit.Repository.FavoriteCourseRepository;
import com.vega.springit.Repository.UserRepository;
import com.vega.springit.model.Course;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    private FavoriteCourseRepository favoriteCourseRepository;
    private final CourseRepository courseRepository;
    private UserService userService;
    private UserRepository userRepository;

    public CourseService( UserService userService,UserRepository userRepository,FavoriteCourseRepository favoriteCourseRepository, CourseRepository courseRepository) {
        this.favoriteCourseRepository = favoriteCourseRepository;
        this.courseRepository = courseRepository;
        this.userRepository=userRepository;
        this.userService=userService;
    }

    public List<Course> findAll(){
        return courseRepository.findAll();
    }

    public  List<Course> findTop10ByOrderByVoteCountDesc(){ return  courseRepository.findTop10ByOrderByVoteCountDesc();}

    /**
     * addIsFavoriteattribute
     * @param courseList
     * @return
     */
    public List<Course> addIsFavoriteattribute (List<Course> courseList){
        if(!userService.isLogged()){
            return courseList;
        }

        courseList.forEach((course)->{

            long userId = userRepository.findByEmail(userService.loggedInUserEmail()).get().getId();

            if(favoriteCourseRepository.findByUserIdAndCourseId(userId,course.getId()).isPresent()){
                course.isFavorite = true;
            }else{
                course.isFavorite = false;
            }

        });

        return courseList;
    }
}
