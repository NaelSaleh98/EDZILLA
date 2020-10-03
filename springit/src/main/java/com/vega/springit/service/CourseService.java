package com.vega.springit.service;

import com.vega.springit.Repository.CourseRepository;
import com.vega.springit.model.Course;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> findAll(){
        return courseRepository.findAll();
    }

    public  List<Course> findTop10ByOrderByVoteCountDesc(){ return  courseRepository.findTop10ByOrderByVoteCountDesc();}

}
