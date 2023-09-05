package me.t0fig.fintracker.controller;

import me.t0fig.fintracker.exception.IllegalTransactionAmountException;
import me.t0fig.fintracker.exception.TransactionNotFoundException;
import me.t0fig.fintracker.exception.UsernameAlreadyTakenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvise {
    @ExceptionHandler
    ResponseEntity<ErrorResponse> handleTransactionNotFoundException(TransactionNotFoundException e) {
        int status = HttpStatus.NOT_FOUND.value();
        return ResponseEntity.status(status).body(new ErrorResponse(status, "Transaction not found!"));
    }

    @ExceptionHandler
    ResponseEntity<ErrorResponse> handleDisabledUserException(DisabledException e) {
        int status = HttpStatus.FORBIDDEN.value();
        return ResponseEntity.status(status).body(new ErrorResponse(status, "User is disabled!"));
    }

    @ExceptionHandler
    ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e) {
        int status = HttpStatus.FORBIDDEN.value();
        return ResponseEntity.status(status).body(new ErrorResponse(status, "Bad credentials!"));
    }

    @ExceptionHandler
    ResponseEntity<ErrorResponse> handleUsernameAlreadyExistsException(UsernameAlreadyTakenException e) {
        int status = HttpStatus.CONFLICT.value();
        return ResponseEntity.status(status).body(new ErrorResponse(status, "Username already taken!"));
    }

    @ExceptionHandler
    ResponseEntity<ErrorResponse> handleIllegalTransactionAmountException(IllegalTransactionAmountException e) {
        int status = HttpStatus.BAD_REQUEST.value();
        return ResponseEntity.status(status).body(new ErrorResponse(status, "Transaction value cannot be negative," +
                " you can use category to indicate that it is income otherwise is is considered an expense of" +
                " the given category!"));
    }

    @ExceptionHandler
    ResponseEntity<ErrorResponse> handleGeneralException(Exception e) {
        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        return ResponseEntity.status(status).body(new ErrorResponse(status, e.getMessage()));
    }

}
