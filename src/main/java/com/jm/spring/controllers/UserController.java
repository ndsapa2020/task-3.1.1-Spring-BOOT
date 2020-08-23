package com.jm.spring.controllers;

import com.jm.spring.model.User;
import com.jm.spring.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    public UserRepository userService;

    @GetMapping("userProfile")
    public ModelAndView index(Principal principal, ModelAndView mav) {
        User user = userService.getUserByUsername(principal.getName());
        mav.addObject("user", user);
        mav.setViewName("/user/info");
        return mav;
    }
}
