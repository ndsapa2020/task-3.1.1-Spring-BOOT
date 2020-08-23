package com.jm.spring.controllers;

import com.jm.spring.model.Role;
import com.jm.spring.model.User;
import com.jm.spring.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userService;

    @GetMapping("/start")
    public String startAdmin(ModelMap modelMap) {
        Iterable<User> userList = userService.findAll();
        modelMap.addAttribute("listUsers", userList);
        return "admin/start";
    }

    @GetMapping(value = "/new")
    public ModelAndView addNewUserForm(ModelAndView modelAndView) {
        getNewModelAndView(modelAndView);
        modelAndView.addObject("isAdmin", false);
        modelAndView.addObject("isUser", true);
        modelAndView.setViewName("admin/add");
        return modelAndView;
    }

    @PostMapping("/new")
    public String newUser(@RequestParam(name = "isAdmin", required = false) boolean isAdmin,
                          @RequestParam(name = "isUser", required = false) boolean isUser,
                          @ModelAttribute User user) {
        Set<Role> rolesToAdd = new HashSet<>();
        if (isUser) {
            rolesToAdd.add(new Role(1L, "ROLE_USER"));
        }
        if (isAdmin) {
            rolesToAdd.add(new Role(2L, "ROLE_ADMIN"));
        }
        user.setRoles(rolesToAdd);
        userService.save(user);
        return "redirect:/admin/start";
    }

    @GetMapping("/edit")
    public ModelAndView editForm(@RequestParam(name = "id", defaultValue = "1") long id) {
        ModelAndView mav = new ModelAndView("admin/update");
        User user = userService.findById(id).orElseThrow();
        mav.addObject("user", user);
        return mav;
    }

    @PostMapping("/edit")
    public String editUser(@RequestParam(name = "id", defaultValue = "1") Long id,
                           @ModelAttribute User user) {
        Set<Role> setOldRoles = userService.findById(id).orElseThrow().getRoles();
        System.out.println("Here " + user);
        user.setRoles(setOldRoles);
        userService.save(user);
        return "redirect:/admin/start";
    }

    @GetMapping("/delete")
    public String deleteUserForm(@RequestParam(name = "id", defaultValue = "1") long id) {
        userService.deleteById(id);
        return "redirect:/admin/start";
    }

    @GetMapping("/search")
    public String findUserByIdForm() {
        return "admin/search_form";
    }

    @PostMapping("/searchResult")
    public ModelAndView findUserResultForm(@RequestParam(name = "id", defaultValue = "1") long id,
                                           ModelAndView mav) {
        User user = userService.findById(id).orElseThrow();
        mav.setViewName("admin/search_result_form");
        mav.addObject("user", user);
        return mav;
    }

    private void getNewModelAndView(ModelAndView modelAndView) {
        User user = new User();
        user.setLogin("somebody");
        user.setPassword("password");
        System.out.println(user);
        Set<Role> rolesSet = Role.getRolesSet();
        modelAndView.addObject("roles", rolesSet);
        modelAndView.addObject("user", user);
    }
}
