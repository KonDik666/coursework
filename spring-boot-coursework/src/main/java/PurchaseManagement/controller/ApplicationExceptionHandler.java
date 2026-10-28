package PurchaseManagement.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import java.util.NoSuchElementException;

@Slf4j
@ControllerAdvice
public class ApplicationExceptionHandler {
    private ModelAndView error(HttpStatus status, String text) {
        ModelAndView view = new ModelAndView("error"); view.setStatus(status);
        view.addObject("status", status.value()); view.addObject("message", text); return view;
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView denied(AccessDeniedException e) { return error(HttpStatus.FORBIDDEN, e.getMessage()); }
    @ExceptionHandler(NoSuchElementException.class)
    public ModelAndView missing(NoSuchElementException e) { return error(HttpStatus.NOT_FOUND, e.getMessage()); }
    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentTypeMismatchException.class})
    public ModelAndView invalid(Exception e) { return error(HttpStatus.BAD_REQUEST, "Некорректные параметры запроса"); }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ModelAndView constraint(DataIntegrityViolationException e) {
        return error(HttpStatus.BAD_REQUEST, "Данные нарушают ограничения базы. Проверьте значения и повторите операцию.");
    }
}
