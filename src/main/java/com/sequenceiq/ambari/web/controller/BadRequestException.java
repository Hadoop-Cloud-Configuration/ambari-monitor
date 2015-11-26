package com.sequenceiq.ambari.web.controller;

import org.apache.http.client.HttpResponseException;

/**
 * Created by jiang on 15/11/22.
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
