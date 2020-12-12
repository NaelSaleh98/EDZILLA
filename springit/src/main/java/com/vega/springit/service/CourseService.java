package com.vega.springit.service;

import com.vega.springit.Repository.*;
import com.vega.springit.model.Course;
import com.vega.springit.model.FavoriteCourse;
import com.vega.springit.model.Report;
import com.vega.springit.model.User;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    private FavoriteCourseRepository favoriteCourseRepository;
    private final CourseRepository courseRepository;
    private UserService userService;
    private UserRepository userRepository;
    private VoteRepository voteRepository;
    private ReportRepository reportRepository;

    public CourseService(ReportRepository reportRepository, VoteRepository voteRepository, UserService userService,UserRepository userRepository,FavoriteCourseRepository favoriteCourseRepository, CourseRepository courseRepository) {
        this.favoriteCourseRepository = favoriteCourseRepository;
        this.courseRepository = courseRepository;
        this.userRepository=userRepository;
        this.userService=userService;
        this.voteRepository =voteRepository;
        this.reportRepository = reportRepository;
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

    public Course addIsReportedAttribute(Course course){
        if(!userService.isLogged()){
            return course;
        }
        long userId = userRepository.findByEmail(userService.loggedInUserEmail()).get().getId();
        Optional<Report> reportOptional = reportRepository.findByUserIdAndCourseId(userId , course.getId());
        if (reportOptional.isPresent()){
            course.isReported = true;
            return course;
        }
        else {
            course.isReported = false;
            return course;
        }
    }


    public List<Course> getFavoriteCourses(){
        Optional<User>  userOptional = userRepository.findByEmail(userService.loggedInUserEmail());
        User user;
        List<FavoriteCourse> favoriteCourseList;
        List<Course> courseList = new ArrayList<>();

        if(userOptional.isPresent()){
            user = userOptional.get();
            favoriteCourseList = favoriteCourseRepository.findByUserId(user.getId());
            favoriteCourseList.forEach(favoriteCourse -> {
                courseList.add(favoriteCourse.getCourse());
            });

        }
        return courseList;

    }

}
