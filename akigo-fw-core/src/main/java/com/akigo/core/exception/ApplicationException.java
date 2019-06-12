package com.akigo.core.exception;

import com.akigo.core.message.AppMessage;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ApplicationException extends Exception {

    private List<AppMessage> appMessages = Collections.emptyList();

    public ApplicationException(AppMessage... appMessages) {
        this.appMessages = Arrays.asList(Objects.requireNonNull(appMessages));
    }

    protected ApplicationException(String s) {
        super(s);
    }

    protected ApplicationException(String s, Throwable t) {
        super(s, t);
    }

    protected ApplicationException(Throwable t) {
        super(t);
    }

    protected void setAppMessages(AppMessage... appMessages) {
        this.appMessages = Arrays.asList(Objects.requireNonNull(appMessages));
    }

    public List<AppMessage> getAppMessages() {
        return appMessages;
    }

    @Override
    public String getMessage() {
        return super.getMessage() != null ? super.getMessage() : "Application error";
    }
}
