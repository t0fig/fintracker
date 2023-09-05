package me.t0fig.fintracker.controller;

public record ErrorResponse(Integer status, String message, Long timestamp) {
    public ErrorResponse(Integer status, String message) {
        this(status, message, System.currentTimeMillis());
    }
}
