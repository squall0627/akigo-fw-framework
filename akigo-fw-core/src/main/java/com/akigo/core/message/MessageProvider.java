package com.akigo.core.message;

import java.util.Optional;

public interface MessageProvider {

    Optional<String> get(String messageId);

    Optional<AppMessage> getMessage(String messageId);
}
