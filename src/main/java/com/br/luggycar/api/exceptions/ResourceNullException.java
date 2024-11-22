package com.br.luggycar.api.exceptions;

import java.util.List;

public class ResourceNullException extends Exception {
    private final List<String> messages;

    public ResourceNullException(List<String> messages) {
        super();
        this.messages = messages;
    }

    public List<String> getMessages() {
        return messages;
    }
}
