package com.vega.springit.service;

import com.vega.springit.Repository.CourseRepository;
import com.vega.springit.model.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Scheduler {
    @Autowired
    public CourseRepository courseRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(Scheduler.class);


    @Scheduled(cron = "0 30 00 * * ?") // every day at 12:30 AM
    public void autoRemoveReportedCourse() {
    LOGGER.info("start removing reported course....");
    List<Course> reportedCourseList = courseRepository.findReportedCourses();
    reportedCourseList.forEach(course -> {
        LOGGER.info("remove course: " + course.getTitle());
        courseRepository.deleteById(course.getId());
    });
    }
}
