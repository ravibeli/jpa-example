package com.hr.app.jpaexample.i18n.exceptions.helper;

import com.hr.app.jpaexample.i18n.exceptions.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * LocalizationHelper helper class used to get the translated message.
 *
 * @author ravibeli@gmail.com
 * @project jpa-example
 *
 **/

@Slf4j
@Service
public class LocalizationTranslator {

    MessageSource messageSource;

    /**
     * Constructor injection.
     *
     * @param messageSource the messageSource
     */
    @Autowired
    public LocalizationTranslator(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * localize method looks up code to message in ValidationMessages.
     * @param exception the exception
     * @param <T> generic param
     * @return localized exception message in exception object
     */
    public <T extends ApplicationException> T localize(T exception) {
        if (StringUtils.hasText(exception.getCode())) {
            String message;
            try {
                message = messageSource.getMessage(exception.getCode(), exception.getArguments(),
                    LocaleContextHolder.getLocale());
            } catch (NoSuchMessageException e) {
                log.warn(e.getMessage());
                message = exception.getCode();
            }
            exception.setMessage(message);
        }
        return exception;
    }
}
