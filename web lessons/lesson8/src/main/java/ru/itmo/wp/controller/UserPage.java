package ru.itmo.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.itmo.wp.domain.User;
import ru.itmo.wp.service.UserService;

@Controller
public class UserPage extends Page {
    private final UserService userService;

    public UserPage(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/user/{id}")
    public String view(@PathVariable String id, Model model) {
        long uId;
        try {
            uId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            return "UserPage";
        }
        User user = userService.findById(uId);
        model.addAttribute("userP", user);
        return "UserPage";
    }
}
