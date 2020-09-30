package com.vega.springit.Controller;

import com.vega.springit.model.User;
import com.vega.springit.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class authController {

    private static final Logger logger = LoggerFactory.getLogger(authController.class);
    private UserService userService;

    public authController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/login")
    public String login(){
        return "auth/login";
    }

    @GetMapping("/profile")
    public String profile() {
        return "auth/profile";
    }

    // ON
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user",new User());
        return "auth/register";
    }

    // ON
    @PostMapping("/register")
    public String registerNewUser(@Valid User user, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        Optional<User> testEmail = userService.findByEmail(user.getEmail());
        Optional<User> testAlias = userService.findByAlias(user.getAlias());
        List<ObjectError> errorList =  new ArrayList<>();
        if (testEmail.isPresent()){
            ObjectError objectError = new ObjectError("test" , "Please, This Email is already exist. Try to Sign in.");
            errorList.add(objectError);
        }

        if (testAlias.isPresent()){
            ObjectError objectError = new ObjectError("test" , "Please, This Alias is already exist. Try another one.");
            errorList.add(objectError);
        }
        if(! user.getFirstName().matches("^[A-Za-z]+$")){
            ObjectError objectError = new ObjectError("test" , "Please, Your Name should contain only letters.");
            errorList.add(objectError);
        }
        else if(! user.getLastName().matches("^[A-Za-z]+$")){
            ObjectError objectError = new ObjectError("test" , "Please, Your Name should contain only letters.");
            errorList.add(objectError);
        }
        if(! user.getAlias().matches("^(?![_])(?!.*[_]{2})[a-zA-Z0-9_]+(?<![_])$")){
            ObjectError objectError = new ObjectError("test" , "Please, Alias can contain letters & numbers & underscore only, but cannot end or start with underscore.");
            errorList.add(objectError);
        }
        if(! user.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){
            ObjectError objectError = new ObjectError("test" , "Please, Enter valid Email => user@test.com.");
            errorList.add(objectError);
        }
        if(! user.getPassword().matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$")){
            ObjectError objectError = new ObjectError("test" , "Please, Check that the a password contain, At least one digit.  At least one lowercase character. At least one uppercase character.");
            errorList.add(objectError);
        }
        if( errorList.size() > 0 ){
            logger.info("Validation errors were found while registering a new user.");
            model.addAttribute("user",user);
            model.addAttribute("validationErrors", errorList);
            return "auth/register";
        }

        if( bindingResult.hasErrors() ) {
            logger.info("Validation errors were found while registering a new user");
            model.addAttribute("user",user);
            model.addAttribute("validationErrors", bindingResult.getAllErrors());
            return "auth/register";
        } else {
            User newUser = userService.register(user);
            redirectAttributes
                    .addAttribute("id", newUser.getId())
                    .addFlashAttribute("success",true);
            return "redirect:/register";
        }
    }

    // ON
    @GetMapping("/activate/{email}/{activationCode}")
    public String activate(@PathVariable String email, @PathVariable String activationCode) {
        Optional<User> user = userService.findByEmailAndActivationCode(email,activationCode);
        if( user.isPresent() ){
            User newUser = user.get();
            newUser.setEnabled(true);
            newUser.setConfirmPassword(newUser.getPassword());
            userService.save(newUser);
            userService.sendWelcomeEmail(newUser);
            return "auth/activated";
        }
        return "redirect:/";
    }

}
