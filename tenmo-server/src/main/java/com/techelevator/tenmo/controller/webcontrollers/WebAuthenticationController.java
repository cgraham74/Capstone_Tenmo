package com.techelevator.tenmo.controller.webcontrollers;

import com.techelevator.tenmo.model.LoginDTO;
import com.techelevator.tenmo.model.RegisterUserDTO;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.security.jwt.TokenProvider;
import com.techelevator.tenmo.services.UserDao;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;


@CrossOrigin
@Controller
@Data
public class WebAuthenticationController {


        private final TokenProvider tokenProvider;
        private final AuthenticationManagerBuilder authenticationManagerBuilder;
        private UserDao userDao;

        @Autowired
        public WebAuthenticationController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, UserDao userDao) {
            this.tokenProvider = tokenProvider;
            this.authenticationManagerBuilder = authenticationManagerBuilder;
            this.userDao = userDao;

        }

    @GetMapping("/login")
        public String login(Model model){
            model.addAttribute("user", new User());
            return "login";
        }


        @PostMapping("/login")
        @ResponseStatus(HttpStatus.OK)
            public ModelAndView login(@Valid @ModelAttribute LoginDTO loginDto, HttpSession session) {

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.createToken(authentication, false);
            User user = userDao.findByUsername(loginDto.getUsername());
            WebAuthenticationController.LoginResponse loginResponse =  new WebAuthenticationController.LoginResponse(jwt, user);
            ModelAndView currentUser = new ModelAndView("layout");
            currentUser.addObject("loginResponse", loginResponse);
            session.setAttribute("user",user);
            return currentUser;
        }

        @ResponseStatus(HttpStatus.CREATED)
        @PostMapping("/register")
        public String register(@Valid @ModelAttribute RegisterUserDTO newUser){
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
