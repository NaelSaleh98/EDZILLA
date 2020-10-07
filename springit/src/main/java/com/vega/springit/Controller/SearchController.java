package com.vega.springit.Controller;

import com.vega.springit.Repository.CourseRepository;
import com.vega.springit.model.Course;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController

public class SearchController {
    private CourseRepository courseRepository;

    public SearchController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping("/course/search/{courseTitle}")
    public List<Course> search(@PathVariable String courseTitle, Model model){
        List<Course> courses = courseRepository.findByTitleContaining(courseTitle);
        System.out.println(courses.size());
        return courses;
    }


}
