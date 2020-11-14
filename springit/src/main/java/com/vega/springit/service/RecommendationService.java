package com.vega.springit.service;

import com.vega.springit.Repository.CourseRepository;
import com.vega.springit.Repository.UserRepository;
import com.vega.springit.Repository.VoteRepository;
import com.vega.springit.model.Course;
import com.vega.springit.model.User;
import com.vega.springit.model.Vote;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecommendationService {

    private UserRepository userRepository;
    private VoteRepository voteRepository;
    private CourseRepository courseRepository;

    public RecommendationService(UserRepository userRepository, VoteRepository voteRepository, CourseRepository courseRepository) {
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
        this.courseRepository = courseRepository;
    }

    public float personCorrelation(short [] userA , short [] userB , int length){
        int count = 0;
        for (int i = 0; i < length; i++) {
            if (userA[i] == userB[i]) count++;
        }
        return count/(float)length;
    }

    public List<Course> getRecommindedCourses(String loggedInUserEmail){
        short userA[],userB[];
        List <Course> courseList = new ArrayList<>();

        Optional<User> optionalUser = userRepository.findByEmail(loggedInUserEmail);

        if(optionalUser.isPresent()) {
            User loggedInUser =optionalUser.get();
            List<Vote> currentUserVotes = voteRepository.findAllByUserId(loggedInUser.getId());
            List<User> groupOfUser = userRepository.findAllByEmailNot(loggedInUserEmail);
            if (groupOfUser.size() > 50){
                groupOfUser = groupOfUser.subList(0 , 49);
            }
            userA = new short[currentUserVotes.size()];
            userB = new short[currentUserVotes.size()];
            groupOfUser.forEach(user -> {

                // find courses that upvoted by other users
                List<Course> likedCoursesByOther = new ArrayList<>();
                List <Vote> likedCourseVotes = voteRepository.findAllByUserIdAndDirection(user.getId() , (short)1);
                likedCourseVotes.forEach((vote -> {
                    likedCoursesByOther.add(courseRepository.findById(vote.getCourse().getId()).get());
                }));

                // find courses that upvoted current user
                List<Course> likedCoursesByuser = new ArrayList<>();
                List <Vote> likedCourseVotesByUser = voteRepository.findAllByUserId(loggedInUser.getId());
                likedCourseVotesByUser.forEach((vote -> {
                    likedCoursesByuser.add(courseRepository.findById(vote.getCourse().getId()).get());
                }));


                //fill userA and userB to fine correlation
                for(int i =0 ; i< currentUserVotes.size(); i ++){
                    userA[i]=currentUserVotes.get(i).getDirection();
                    Optional<Vote> voteB = voteRepository.findByUserIdAndCourseId(user.getId(),currentUserVotes.get(i).getCourse().getId());
                    userB[i]=0;
                    if(voteB.isPresent()){
                        userB[i]=voteB.get().getDirection();
                    }
                }

                float correlation = personCorrelation(userA , userB , userA.length);

                if (correlation > 0.80){
                    for (int j=0 ; j< likedCoursesByOther.size() ; j++){
                        if (courseList.size() > 10){
                            break;
                        }
                        if (!likedCoursesByuser.contains(likedCoursesByOther.get(j))){ // only courses liked by other and not by user
                            if (!courseList.contains(likedCoursesByOther.get(j))){ // not exist in whole course list
                                courseList.add(likedCoursesByOther.get(j));
                            }
                        }
                    }
                }

            });
        }
        return courseList;
    }

}
