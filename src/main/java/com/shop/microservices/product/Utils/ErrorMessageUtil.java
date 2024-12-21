package com.shop.microservices.product.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Utility class to fetch error messages from message properties.
 * Supports internationalization (i18n) for different locales.
 */

@Component
public class ErrorMessageUtil {

    // Injected MessageSource to retrieve messages from the property files
    private final MessageSource messageSource;

    // Constructor for dependency injection
    @Autowired
    public ErrorMessageUtil(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Fetches the error message corresponding to the given code and arguments.
     * Supports dynamic message formatting (e.g., replacing placeholders).
     *
     * @param code The error message code to look up.
     * @param args The arguments to format the message (optional).
     * @return The error message in the current locale.
     */
    public String getErrorMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, Locale.getDefault());
    }

    /**
     * Fetches the error message corresponding to the given code.
     * No arguments are passed for message formatting.
     *
     * @param code The error message code to look up.
     * @return The error message in the current locale.
     */
    public String getErrorMessage(String code) {
        return messageSource.getMessage(code, null, Locale.getDefault());
    }

}
