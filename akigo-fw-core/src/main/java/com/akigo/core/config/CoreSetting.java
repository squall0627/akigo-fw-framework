package com.akigo.core.config;

import com.akigo.core.message.MessageProvider;
import com.akigo.core.message.ResourceMessageProvider;
import com.akigo.core.util.ClassUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CoreSetting {

    @JsonProperty
    private String execEnv = "DEFAULT";

    @JsonProperty("messageProvider")
    private String messageProviderString = "";

    private MessageProvider messageProvider = new ResourceMessageProvider();

    public String getExecEnv() {
        return execEnv;
    }

    public void setExecEnv(String execEnv) {
        this.execEnv = execEnv;
    }

    @JsonIgnore
    public MessageProvider getMessageProvider() {
        return messageProvider;
    }

    @JsonProperty
    public void setMessageProvider(String messageProvider) {
        this.messageProviderString = messageProvider;
        ((ResourceMessageProvider) this.messageProvider)
                .addMessageProvider((MessageProvider) ClassUtils.newInstance(messageProvider));
    }
}
