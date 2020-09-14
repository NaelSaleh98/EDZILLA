package com.vega.springit.Controller;


import com.vega.springit.Repository.CommentRepository;
import com.vega.springit.Repository.LinkRepository;
import com.vega.springit.Repository.UserRepository;
import com.vega.springit.model.Comment;
import com.vega.springit.model.Link;
import com.vega.springit.service.LinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;


@Controller
public class LinkController {
    static public Link currentLinkForComment;
    private static final Logger logger = LoggerFactory.getLogger(LinkController.class);
    private LinkRepository linkRepository;
    private CommentRepository commentRepository;
    private LinkService linkService;
    private UserRepository userRepository;

    public LinkController(UserRepository userRepository,LinkRepository linkRepository, CommentRepository commentRepository,LinkService linkService) {
        this.linkRepository = linkRepository;
        this.commentRepository = commentRepository;
        this.linkService=linkService;
        this.userRepository =userRepository;
    }

    @GetMapping("/")
    public String list(Model model){
        model.addAttribute("links",linkService.findAll());
        return "Link/list";
    }
    @GetMapping("/link/{id}")
    public String read(@PathVariable Long id,Model model) {
        Optional<Link> link = linkRepository.findById(id);
        if( link.isPresent() ) {
            Link currentLink = link.get();
            Comment comment = new Comment();
            currentLinkForComment =currentLink;
            comment.setLink(currentLink);
            model.addAttribute("comment",comment);
            model.addAttribute("link",currentLink);
            model.addAttribute("success", model.containsAttribute("success"));
            return "Link/view";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/link/submit")
    public String newLinkForm(Model model) {
        model.addAttribute("link",new Link());
        return "Link/submit";
    }

    @PostMapping("/link/submit")
    public String createLink(@Valid Link link, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

        //@Valid -> ensure if link is valid , we will know if it is valid or not and default message error from bindingResult
        //not Valid if one attribute in link not satisfy the specification in entity Link
        //bindingResult.hasErrors() ->will return true if one attribute in comment not satisfy the specification in entity Comment
        //this is server side protection
        if(bindingResult.hasErrors()){
            logger.info("Validation error was found!");
            model.addAttribute("link",link);
            return "Link/submit";
        }
        else{
            //following 3 lines i change them without any recommend from tutorials ,
            // before change they was : linkRepository.save(link);
        linkRepository.save(link);
        link.setUser(userRepository.findByEmail( linkRepository.findById(link.getId()).get().getCreatedBy() ).get());
        linkRepository.save(link);
        //
        logger.info("new link was saved successfully!");

        redirectAttributes
                .addAttribute("id",link.getId())
                .addFlashAttribute("success",true);
        //FlashAttribute available once redirect occur, after we do page reload FlashAttribute will disappear
       return "redirect:/link/"+link.getId();//fire new uel
       //or -> return "redirect:/link/{id}";
        }



    }

    @Secured({"ROLE_USER"})//to protect from server side,it mean only logged in user with ROLE_USER can initat this requist
    @PostMapping("/link/comments")
    public String addComment(@Valid Comment comment, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if( bindingResult.hasErrors() ) {
            logger.info("Something went wrong.");
        } else {
            logger.info("New Comment Saved!");
            comment.setLink(currentLinkForComment);
            commentRepository.save(comment);
        }
        return "redirect:/link/" + comment.getLink().getId();
    }
}
