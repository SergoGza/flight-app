package com.tokioschool.flightapp.flight.mvc.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleIllegalArgumentException(IllegalArgumentException ex, Model model) {
        log.error("IllegalArgumentException occurred: ", ex);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error/400");
        modelAndView.addObject("errorMessage", ex.getMessage());
        modelAndView.addObject("errorType", "Solicitud Incorrecta");
        return modelAndView;
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ModelAndView handleIllegalStateException(IllegalStateException ex, Model model) {
        log.error("IllegalStateException occurred: ", ex);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error/409");
        modelAndView.addObject("errorMessage", ex.getMessage());
        modelAndView.addObject("errorType", "Conflicto de Estado");
        return modelAndView;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNotFoundException(NoHandlerFoundException ex, Model model) {
        log.error("Page not found: ", ex);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error/404");
        modelAndView.addObject("errorMessage", "La página solicitada no existe");
        modelAndView.addObject("errorType", "Página No Encontrada");
        return modelAndView;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleValidationException(MethodArgumentNotValidException ex, Model model) {
        log.error("Validation error occurred: ", ex);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error/400");
        modelAndView.addObject("errorMessage", "Error en la validación de datos");
        modelAndView.addObject("errorType", "Datos Inválidos");
        modelAndView.addObject("fieldErrors", ex.getBindingResult().getFieldErrors());
        return modelAndView;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleGeneralException(Exception ex, Model model) {
        log.error("Unexpected error occurred: ", ex);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error/500");
        modelAndView.addObject("errorMessage", "Ha ocurrido un error inesperado");
        modelAndView.addObject("errorType", "Error del Servidor");
        return modelAndView;
    }
}