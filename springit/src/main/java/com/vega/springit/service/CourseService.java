package com.vega.springit.service;

import com.vega.springit.Repository.CourseRepository;
import com.vega.springit.Repository.FavoriteCourseRepository;
import com.vega.springit.Repository.UserRepository;
import com.vega.springit.Repository.VoteRepository;
import com.vega.springit.model.Course;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {
    private FavoriteCourseRepository favoriteCourseRepository;
    private final CourseRepository courseRepository;
    private UserService userService;
    private UserRepository userRepository;
    private VoteRepository voteRepository;

    public CourseService( VoteRepository voteRepository, UserService userService,UserRepository userRepository,FavoriteCourseRepository favoriteCourseRepository, CourseRepository courseRepository) {
        this.favoriteCourseRepository = favoriteCourseRepository;
        this.courseRepository = courseRepository;
        this.userRepository=userRepository;
        this.userService=userService;
        this.voteRepository =voteRepository;
    }

    public List<Course> findAll(){
        return courseRepository.findAll();
    }

    public  List<Course> findTop10ByOrderByVoteCountDescNotIn(List<Course> courseList){
        List<Long> coursesIDS =new ArrayList<>();

        courseList.forEach((course) ->{
            coursesIDS.add(course.getId());
        } );
        return  courseRepository.findTop10ByOrderByVoteCountDescNot(coursesIDS);
    }

    /**
     * add IsFavorite attribute in order to make the star colored when re loading the page
     * add IsUp and IsDown attribute in order to make the up and down arrow colored when re loading the page
     * @param courseList
     * @return List<Course>
     */
    public List<Course> addIsFavoriteIsUpIsDownAttributes(List<Course> courseList){
        if(!userService.isLogged()){
            return courseList;
        }
        long userId = userRepository.findByEmail(userService.loggedInUserEmail()).get().getId();

        courseList.forEach((course)->{
            //IsFavorite attribute
            if(favoriteCourseRepository.findByUserIdAndCourseId(userId,course.getId()).isPresent()){
                course.isFavorite = true;
            }else{
                course.isFavorite = false;
            }

            //isUp attribute
            if(voteRepository.findByuserIdAndDirectionAndCourseId(userId,(short) 1,course.getId()).isPresent()){
                course.isUp = true;
            }else{
                course.isUp = false;
            }

            //isDown attribute
            if(voteRepository.findByuserIdAndDirectionAndCourseId(userId,(short) -1,course.getId()).isPresent()){
                course.isDown = true;
            }else{
                course.isDown = false;
            }
        });

        return courseList;
    }




}
