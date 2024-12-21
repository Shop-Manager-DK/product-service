package com.shop.microservices.product.Exception;

import com.shop.microservices.product.Utils.ErrorMessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * GlobalExceptionHandler is a centralized class for handling exceptions across the entire Spring MVC application.
 * It leverages the @ControllerAdvice annotation to catch and handle specific exceptions thrown from
 * any controller in the application.
 * The handler is responsible for creating consistent error responses using custom error messages
 * from the ErrorMessageUtil.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    // Injected ErrorMessageUtil to fetch error messages from property files for internationalization (i18n)
    private final ErrorMessageUtil errorMessageUtil;

    /**
     * Constructor for dependency injection of the ErrorMessageUtil class.
     * The ErrorMessageUtil is used to fetch error messages based on keys from the message properties file.
     *
     * @param errorMessageUtil The ErrorMessageUtil instance to be injected.
     */
    @Autowired
    public GlobalExceptionHandler(ErrorMessageUtil errorMessageUtil) {
        this.errorMessageUtil = errorMessageUtil;
    }
}
