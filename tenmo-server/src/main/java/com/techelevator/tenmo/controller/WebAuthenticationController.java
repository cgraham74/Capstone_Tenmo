package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.exceptions.InvalidUsernameOrPasswordException;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 *
 *
 * @author Christina Graham
 */
@PreAuthorize("permitAll()")
@CrossOrigin
@Controller
@Data
public class WebAuthenticationController {

    private  static final String registerPage = "register";
    private static final String errorMessage ="errorMessage";

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

    /**
     * This method displays the login form to the user by populating the Model object with the necessary
     * attributes and returning the view name for the login form. It handles incoming GET requests at the root path ("/").
     *
     * @param model The Model object is used to pass attributes (data) from the controller to the view.
     *              It serves as a container for the data that needs to be displayed on the view.
     * @return The view name for the login form, which is "login" in this case.
     *
     * Note:
     * Ensure that the "login" view name returned by this method corresponds to the actual login form view
     * in your application. Also, make sure to properly configure the LoginDTO class to
     * capture the user's login credentials from the form.
     */
    @GetMapping("/")
    public String showLogInForm(Model model) {
        model.addAttribute("message", "Log in");
        model.addAttribute("loginDTO", new LoginDTO());
        return "login";
    }

    @GetMapping("/register")
    public String showRegisteredUserForm(Model model) {
        model.addAttribute("message", "Create a new account");
        return registerPage;
    }

    /**
     * When the user enters their username and password in the login form
     * The form action calls this endpoint to authenticate the user and
     * add the user to session storage
     *
     * @param loginDto a DTO object to store a users credentials
     * @param session  An interface between the user and browser allowing
     *                 storage of user-specific authentication.
     * @return a Model and View object representing the user and the view
     */
    @PostMapping("/login")
    public ModelAndView login(@Valid @ModelAttribute(name = "loginDto") LoginDTO loginDto, HttpSession session) throws UserNotActivatedException, InvalidUsernameOrPasswordException {
        try {

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = tokenProvider.createToken(authentication, false);
            User user = userDao.findByUsername(loginDto.getUsername());

            WebAuthenticationController.LoginResponse loginResponse = new WebAuthenticationController.LoginResponse(jwt, user);
            ModelAndView modelAndView = new ModelAndView("layout");

            modelAndView.addObject("loginResponse", loginResponse);
            modelAndView.addObject("user", loginResponse.getUser());
            modelAndView.addObject("jwt", loginResponse.getToken());
            System.out.println("login Response user: " + loginResponse.getUser());

            session.setAttribute("user", loginResponse.getUser());
            return modelAndView;
        } catch (UserNotActivatedException e) {
            ModelAndView modelAndView = new ModelAndView("error");
            modelAndView.addObject(errorMessage, e.getMessage());
            return modelAndView;
        } catch (Exception e) {
            ModelAndView modelAndView = new ModelAndView("error");
            modelAndView.addObject(errorMessage, "Invalid username or password");
            return modelAndView;
        }
    }

    /**
     * Register new user function that creates an account. All new accounts start with
     * $1000 balance.
     *
     * @param newUser DTO to transfer a new users credentials to the database.
     * @return Returns the login view to allow a new user to log in.
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute(name = "registerUserDTO") RegisterUserDTO newUser, Model model) {
        if (!newUser.getPassword().equals(newUser.getPasswordConfirmation())) {
            model.addAttribute(errorMessage, "Passwords do not match");
            return registerPage;
        }
        if (!userDao.create(newUser.getUsername(), newUser.getPassword())) {
            model.addAttribute(errorMessage, "User registration failed");
            return registerPage;
        }
        return "login";
    }

    /**
     *  This function logs off the user from the current session and redirects
     *  them to the root path of the application. The function can handle both GET and POST requests.
     *
     * @param request The incoming HTTP request object that contains information about the
     *                client's request, such as request headers, path, and parameters.
     * @param response The outgoing HTTP response object that the server sends back to the client.
     *                 It contains information such as status code, response headers, and response body.
     * @return the redirection path, which is the root path ("/") of the application.
     *
     * Usage:
     * To log off a user, send an HTTP request (either GET or POST) to the "/logoff" path.
     * The function checks if the user is authenticated, and if so, logs the user off using the
     * SecurityContextLogoutHandler().logout() method. After logging off, the user is
     * redirected to the root path of the application.
     *
     * Note:
     * Make sure to properly configure the security context and SecurityContextLogoutHandler to ensure that
     * the logout process works as intended. Additionally, ensure that the "/logoff" path is allowed for
     * both authenticated and unauthenticated users in your application's security configuration
     */
    @RequestMapping(value = "/logoff", method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
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
            return "LoginResponse: [token=" + token + ", user = " + user + "] ";
        }
    }
}
