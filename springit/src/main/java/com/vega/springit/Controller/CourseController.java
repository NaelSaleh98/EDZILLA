package com.vega.springit.Controller;


import com.vega.springit.Repository.*;
import com.vega.springit.model.*;
import com.vega.springit.service.CourseCardService;
import com.vega.springit.service.CourseService;
import com.vega.springit.service.RecommendationService;
import com.vega.springit.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
public class CourseController {

    private  int count = 0;
    static public Course currentCourseForComment;
    static public Course currentCourseForCard;
    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);
    private CourseRepository courseRepository;
    private CommentRepository commentRepository;
    private CourseService courseService;
    private UserRepository userRepository;
    private CourseCardRepository courseCardRepository;
    private CourseCardService courseCardService;
    private RecommendationService recommendationService;
    private  String logedInUserEmail;
    private UserService userService;

    public CourseController(CourseRepository courseRepository, CommentRepository commentRepository, CourseService courseService, UserRepository userRepository, CourseCardRepository courseCardRepository, CourseCardService courseCardService, RecommendationService recommendationService, UserService userService) {
        this.courseRepository = courseRepository;
        this.commentRepository = commentRepository;
        this.courseService = courseService;
        this.userRepository = userRepository;
        this.courseCardRepository = courseCardRepository;
        this.courseCardService = courseCardService;
        this.recommendationService = recommendationService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String list(Model model){
        if (userService.isLogged()){
            model.addAttribute("recommendations", recommendationService.getRecommindedCourses(userService.loggedInUserEmail()));
        }
        model.addAttribute("topTen", courseService.findTop10ByOrderByVoteCountDesc());
        return "Course/list";
    }
    @GetMapping("/course/{id}")
    public String read(@PathVariable Long id,Model model) {
        Optional<Course> course = courseRepository.findById(id);
        if( course.isPresent() ) {
            Course currentCourse = course.get();
            Comment comment = new Comment();
            currentCourseForComment = currentCourse;
            comment.setCourse(currentCourse);
            model.addAttribute("comment",comment);
            model.addAttribute("course", currentCourse);
            model.addAttribute("success", model.containsAttribute("success"));
            return "Course/view";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/course/submit")
    public String newCourseForm(Model model) {
        model.addAttribute("course",new Course());
        return "Course/submit";
    }

    @PostMapping("/course/submit")
    public String createCourse(@Valid Course course, BindingResult bindingResult, Model model) {

        if(bindingResult.hasErrors()){
            logger.info("Validation error was found!");
            model.addAttribute("course", course);
            return "Course/submit";
        }
        else{
            //following 3 lines i change them without any recommend from tutorials ,
            // before change they was : linkRepository.save(link);
        courseRepository.save(course);
        course.setUser(userRepository.findByEmail( courseRepository.findById(course.getId()).get().getCreatedBy() ).get());
        courseRepository.save(course);
        currentCourseForCard = course;
        //
        logger.info("new Course was saved successfully!");

        model.addAttribute("course", course);
        model.addAttribute("courseCard", new CourseCard());
            //FlashAttribute available once redirect occur, after we do page reload FlashAttribute will disappear
       return "Course/editCourse";
       //or -> return "redirect:/link/{id}";
        }



    }

    @Secured({"ROLE_USER"})//to protect from server side,it mean only logged in user with ROLE_USER can initat this requist
    @PostMapping("/course/comments")
    public String addComment(@Valid Comment comment, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if( bindingResult.hasErrors() ) {
            logger.info("Something went wrong.");
        } else {
            logger.info("New Comment Saved!");
            comment.setCourse(currentCourseForComment);
            commentRepository.save(comment);
            String email = comment.getCreatedBy();
            Optional<User> user =  userRepository.findByEmail(email);
            comment.setCreatedBy(user.get().getAlias());
            commentRepository.save(comment);
        }
        return "redirect:/course/" + comment.getCourse().getId();
    }

    @PostMapping("/courseCard/submit")
    public String addCourseCard(@Valid CourseCard courseCard, BindingResult bindingResult , Model model,
                                @RequestParam("image") MultipartFile multipartFile){
        String fileName = "photos/image" + count + "." + multipartFile.getContentType().substring(6);
        if( bindingResult.hasErrors() ) {
            logger.info(bindingResult.getAllErrors().toString());
            return "redirect:/";
        } else {
            logger.info("New Card Saved!");
            courseCard.setCourse(currentCourseForCard);
            String oldVideo = courseCard.getVideoUrl();
            if (oldVideo.equals("")){
                courseCard.setVideoUrl("");
            }else{
                String newUrl =  convertUrl(oldVideo);
                courseCard.setVideoUrl(newUrl);
            }
            courseCard.setImagePath("/" + fileName);
            courseCardRepository.save(courseCard);
            currentCourseForCard.addCard(courseCard);

            //
            try {
                courseCardService.saveImage(multipartFile , fileName);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Photo cant upload." , e);
            }
            count++;
            //
            model.addAttribute("course", currentCourseForCard);
            model.addAttribute("courseCard", new CourseCard());
        }
        return "Course/editCourse";
    }

    private String convertUrl(String oldUrl) {
        String[] urlSplit = oldUrl.split("watch\\?v=");
        return urlSplit[0] + "embed/" + urlSplit[1];
    }
}
