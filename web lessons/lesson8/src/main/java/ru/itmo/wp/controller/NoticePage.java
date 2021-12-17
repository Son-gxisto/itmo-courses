package ru.itmo.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itmo.wp.form.NoticeForm;
import ru.itmo.wp.service.NoticeService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class NoticePage extends Page {
    private final NoticeService noticeService;

    public NoticePage(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @GetMapping("/addNotice")
    public String noticeGet(Model model, HttpSession httpSession) {
        model.addAttribute("noticeForm", new NoticeForm());
        return "NoticeAddPage";
    }

    @PostMapping("/addNotice")
    public String noticePost(@Valid @ModelAttribute("noticeForm") NoticeForm noticeForm,
                             BindingResult bindingResult,
                             HttpSession httpSession) {
        if (bindingResult.hasErrors()) {
            return "NoticeAddPage";
        }
        noticeService.save(noticeForm);
        putMessage(httpSession, "You have been added notice");
        return "redirect:/";
    }
}
