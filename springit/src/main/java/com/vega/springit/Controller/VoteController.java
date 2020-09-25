package com.vega.springit.Controller;

import com.vega.springit.Repository.CourseRepository;
import com.vega.springit.Repository.VoteRepository;
import com.vega.springit.model.Course;
import com.vega.springit.model.Vote;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class VoteController {
    private VoteRepository voteRepository;
    private CourseRepository courseRepository;

    public VoteController(VoteRepository voteRepository, CourseRepository courseRepository) {
        this.voteRepository = voteRepository;
        this.courseRepository = courseRepository;
    }

    @Secured({"ROLE_USER"})//to protect from server side,it mean only logged in user with ROLE_USER can initat this requist
    @GetMapping("/vote/course/{courseID}/direction/{direction}/votecount/{voteCount}")
    public int vote(@PathVariable Long courseID, @PathVariable short direction, @PathVariable int voteCount) {
        Optional<Course> optionalCourse = courseRepository.findById(courseID);
        if( optionalCourse.isPresent() ) {
            Course course = optionalCourse.get();
            Vote vote = new Vote(direction, course);
            voteRepository.save(vote);

            int updatedVoteCount = voteCount + direction;
            course.setVoteCount(updatedVoteCount);
            courseRepository.save(course);
            return updatedVoteCount;
        }

        return voteCount;
    }

}
