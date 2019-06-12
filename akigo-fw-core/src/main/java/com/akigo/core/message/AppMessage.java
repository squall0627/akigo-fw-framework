package com.akigo.core.message;

import com.akigo.core.App;
import com.akigo.core.config.CoreSetting;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.Optional;


public class AppMessage {

    private Level level = Level.INFO;
    private String key;
    private Object[] args;
    private String plainMessage;
    private String messageType;


    public Optional<String> getTemplate(String messageId) {
        CoreSetting core = App.setting(CoreSetting.class);
        Optional<AppMessage> temp = core.getMessageProvider().getMessage(messageId);

        if (temp.isPresent()) {
            setMessageType(temp.get().getMessageType());
            return Optional.of(temp.get().getPlainMessage());
        } else {
            return Optional.empty();
        }
    }


    public AppMessage() {
    }


    public AppMessage(String key, Object... args) {
        this(null, key, args);
    }


    public AppMessage(Level level, String key, Object... args) {
        if (level != null) {
            this.level = level;
        }
        this.key = Objects.requireNonNull(key);
        this.args = args;
    }

    public String getMessage() {
        return MessageFormat.format(getPlainMessage(), getArgs());
    }


    public String getPlainMessage() {
        if (plainMessage == null) {
            plainMessage = getTemplate(getKey()).orElseGet(this::getKey);
        }
        return plainMessage;
    }


    public String getKey() {
        return key;
    }


    public Object[] getArgs() {
        return args;
    }


    public Level getLevel() {
        return level;
    }


    public String getMessageType() {
        return messageType;
    }


    public void setLevel(Level level) {
        this.level = level;
    }


    public void setArgs(Object... args) {
        this.args = args;
    }


    public void setKey(String key) {
        this.key = key;
    }


    public void setPlainMessage(String plainMessage) {
        this.plainMessage = plainMessage;
    }


    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }


    public static enum Level {
        INFO,
        WARN,
        ERROR;
    }
}
