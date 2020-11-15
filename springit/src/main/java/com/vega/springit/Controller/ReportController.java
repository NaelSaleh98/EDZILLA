package com.vega.springit.Controller;

import com.vega.springit.Repository.CourseRepository;
import com.vega.springit.Repository.ReportRepository;
import com.vega.springit.Repository.UserRepository;
import com.vega.springit.model.Course;
import com.vega.springit.model.Report;
import com.vega.springit.model.User;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class ReportController {
    private String logedInUserEmail;
    private ReportRepository reportRepository;
    private CourseRepository courseRepository;
    private UserRepository userRepository;
    private int index = 0;

    public ReportController(ReportRepository reportRepository, CourseRepository courseRepository, UserRepository userRepository) {
        this.reportRepository = reportRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }
    @Secured({"ROLE_USER"})//to protect from server side,it mean only logged in user with ROLE_USER can initat this requist
    @GetMapping("/report/{reason}/{courseId}")
    public void report(@PathVariable String reason, @PathVariable Long courseId) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);

        Object auth = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        logedInUserEmail = ((UserDetails) auth).getUsername();
        Optional<User> optionalUser = userRepository.findByEmail(logedInUserEmail);
        Optional<Report> reportOptional = reportRepository.findByUserIdAndCourseId(optionalUser.get().getId() , optionalCourse.get().getId());
        if (optionalCourse.isPresent() && optionalUser.isPresent() && !reportOptional.isPresent()) {
            Course course = optionalCourse.get();
            Report report = new Report();
            report.setCourse(course);
            report.setReportReason(reason);
            report.setUser(optionalUser.get());
            reportRepository.save(report);
            int count = course.getReportCount();
            course.setReportCount(++count);
            courseRepository.save(course);
        }
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/reportCourse/{reportedCourseId}")
    public Map<String, String>  getReports(@PathVariable long reportedCourseId){
        List<Report> reportList = reportRepository.findByCourseId(reportedCourseId);
        HashMap<String, String> map = new HashMap<>();
        map.put("number_of_reporter", reportList.size()+"");

        reportList.forEach((report) -> {
            Optional<User> reportingUserOptional = userRepository.findById(report.getUser().getId());
            if(reportingUserOptional.isPresent()){
                User reportingUser = reportingUserOptional.get();
                map.put("reporter_name" + index , reportingUser.getAlias());
                map.put("reason" + index++, report.getReportReason());
            }
        });
        index=0;
        return map;
    }


    @GetMapping("/deleteCourse/{courseId}")
    public String deleteCourse(@PathVariable Long courseId){
        courseRepository.deleteById(courseId);
    return "success";
    }
}
