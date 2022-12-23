package com.techelevator.tenmo.controller.webcontrollers;

import com.techelevator.tenmo.model.LoginDTO;
import com.techelevator.tenmo.model.RegisterUserDTO;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.security.UserNotActivatedException;
import com.techelevator.tenmo.security.jwt.TokenProvider;
import com.techelevator.tenmo.services.UserDao;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;

@PreAuthorize("permitAll()")
@CrossOrigin
@Controller
@Data
public class WebAuthenticationController {

        @Autowired
        private final TokenProvider tokenProvider;

        @Autowired
        private final AuthenticationManagerBuilder authenticationManagerBuilder;

        @Autowired
        private UserDao userDao;

        @Autowired
        public WebAuthenticationController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, UserDao userDao) {
            this.tokenProvider = tokenProvider;
            this.authenticationManagerBuilder = authenticationManagerBuilder;
            this.userDao = userDao;

        }
    @GetMapping("/")
    public String showLogInForm(Model model){
        model.addAttribute("message", "Log in");
        return "login";
    }

    @GetMapping("/register")
    public String showRegisteredUserForm(Model model){
        model.addAttribute("message", "New User Registration");
        return "register";
    }

    /**
     * When the user enters their username and password in the log in form
     * The form action calls this endpoint to authenticate the user and
     * add the user to session storage
     * @param loginDto a DTO object to store a users credentials
     * @param session An interface between the user and browser allowing
     *                storage of user-specific authentication.
     * @return a Model and View object representing the user and the view
     */
        @PostMapping("/login")
        @ResponseStatus()
            public ModelAndView login(@Valid @ModelAttribute(name ="loginDto") LoginDTO loginDto, HttpSession session) throws UserNotActivatedException {

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);


            String jwt = tokenProvider.createToken(authentication, false);
            User user = userDao.findByUsername(loginDto.getUsername());
                System.err.println("User before authentication " + user);
            WebAuthenticationController.LoginResponse loginResponse =  new WebAuthenticationController.LoginResponse(jwt, user);
                System.err.println("User after authentication " + loginResponse.user);
            ModelAndView modelAndView = new ModelAndView("layout");
            modelAndView.addObject("jwt", jwt);
            modelAndView.addObject("user", user);
            session.setAttribute("user",user);
            System.err.println("User authentication: " + user);
            System.err.println("jwt authentication: " + jwt);
            return modelAndView;
        }

    /**
     * Register new user function that creates an account. All new accounts start with
     * $1000 balance.
     * @param newUser DTO to transfer a new users credentials to the database.
     * @return Returns the login view to allow a new user to log in.
     */
    @ResponseStatus(HttpStatus.CREATED)
        @PostMapping("/register")
        public String register(@Valid @ModelAttribute(name ="registerUserDTO") RegisterUserDTO newUser){

            if (!userDao.create(newUser.getUsername(), newUser.getPassword())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User registration failed.");
            }
            return "login";
        }

        /**
         * Object to return as body in JWT Authentication.
         */
        static class LoginResponse {

            private String token;
            private User user;

            LoginResponse(String token, User user) {
                this.token = token;
                this.user = user;
            }

            public String getToken() {
                return token;
            }

            void setToken(String token) {
                this.token = token;
            }

            public User getUser() {
                return user;
            }

            public void setUser(User user) {
                this.user = user;
            }
            @Override
            public String toString() {
                return "LoginResponse: [token=" + token + ", user = "+ user+ "] ";
            }
        }
}
