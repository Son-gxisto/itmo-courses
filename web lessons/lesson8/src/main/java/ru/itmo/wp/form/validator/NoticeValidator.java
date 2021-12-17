package ru.itmo.wp.form.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.itmo.wp.form.NoticeForm;
import ru.itmo.wp.service.NoticeService;

public class NoticeValidator implements Validator {
    private final NoticeService noticeService;

    public NoticeValidator(NoticeService noticeService) {
        this.noticeService = noticeService;
    }
    public boolean supports(Class<?> clazz) {
        return NoticeForm.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
    }

}
