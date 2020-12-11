package com.vega.springit.Controller;

import com.vega.springit.Repository.CommentRepository;
import com.vega.springit.Repository.CourseRepository;
import com.vega.springit.Repository.UserRepository;
import com.vega.springit.Repository.VoteRepository;
import com.vega.springit.model.*;
import com.vega.springit.service.UserService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class VoteController {
    private String logedInUserEmail;
    private VoteRepository voteRepository;
    private CourseRepository courseRepository;
    private UserRepository userRepository;
    private CommentRepository commentRepository;

    public VoteController(VoteRepository voteRepository, CourseRepository courseRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.voteRepository = voteRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @Secured({"ROLE_USER"})//to protect from server side,it mean only logged in user with ROLE_USER can initat this requist
    @GetMapping("/vote/course/{courseID}/direction/{direction}/votecount/{voteCount}")
    public int vote(@PathVariable Long courseID, @PathVariable short direction, @PathVariable int voteCount) {
        Optional<Course> optionalCourse = courseRepository.findById(courseID);

            Object auth = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            logedInUserEmail= ((UserDetails)auth).getUsername();

        Optional<User> optionalUser = userRepository.findByEmail(logedInUserEmail);
        if( optionalCourse.isPresent() && optionalUser.isPresent()) {
            Course course = optionalCourse.get();
            User user =optionalUser.get();
            Optional<Vote> userCourseVote=voteRepository.findByUserIdAndCourseId(user.getId(),course.getId());

            if(!userCourseVote.isPresent()) {
                Vote vote = new Vote(direction, course, user);
                voteRepository.save(vote);

                int updatedVoteCount = voteCount + direction;
                course.setVoteCount(updatedVoteCount);
                courseRepository.save(course);
                return updatedVoteCount;
            }else{
                if(userCourseVote.get().getDirection() != direction){
                    Vote vote = userCourseVote.get();
                    vote.setDirection(direction);
                    voteRepository.save(vote);

                    short newDirction= (direction == -1)? (short)-2 : (short)2;
                    int updatedVoteCount = voteCount + newDirction;
                    course.setVoteCount(updatedVoteCount);
                    courseRepository.save(course);
                    return updatedVoteCount;
                }

            }

        }

        return voteCount;
    }

}
