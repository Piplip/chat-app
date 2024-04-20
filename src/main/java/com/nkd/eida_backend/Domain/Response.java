package com.nkd.eida_backend.Domain;

import org.springframework.http.HttpStatus;

import java.util.Map;

public record Response(String time, int code, String path, HttpStatus status, String message, Map<?, ?> data) {}
