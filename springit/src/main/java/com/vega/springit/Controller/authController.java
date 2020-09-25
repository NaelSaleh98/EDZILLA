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
            ObjectError objectError = new ObjectError("test" , "Email is already exist. Try to Sign in.");
            errorList.add(objectError);
        }

        if (testAlias.isPresent()){
            ObjectError objectError = new ObjectError("test" , "Alias is already exist. Try another one.");
            errorList.add(objectError);
        }
        if(! user.getFirstName().matches("[A-Z][a-z]*")){
            ObjectError objectError = new ObjectError("test" , "First Name must contain only letters.");
            errorList.add(objectError);
        }
        if(! user.getLastName().matches("[A-Z][a-z]*")){
            ObjectError objectError = new ObjectError("test" , "Last Name must contain only letters.");
            errorList.add(objectError);
        }
        if(! user.getAlias().matches("^[a-zA-Z][a-zA-Z0-9_]*")){
            ObjectError objectError = new ObjectError("test" , "Alias field can contain letters & numbers only.");
            errorList.add(objectError);
        }
        if(! user.getEmail().matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")){
            ObjectError objectError = new ObjectError("test" , "Email should be like: example@gmail.com");
            errorList.add(objectError);
        }
        if(! user.getPassword().matches("^[A-Za-z]\\w{7,14}$")){
            ObjectError objectError = new ObjectError("test" , "check a password between 7 to 16 characters which contain only characters, numeric digits, underscore and first character must be a letter.");
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
