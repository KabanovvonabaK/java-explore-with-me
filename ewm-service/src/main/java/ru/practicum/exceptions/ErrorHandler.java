package ru.practicum.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import java.time.DateTimeException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(final ConflictException e) {
        for (StackTraceElement ste : e.getStackTrace()) {
            log.error(ste.toString());
        }
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(final BadRequestException e) {
        for (StackTraceElement ste : e.getStackTrace()) {
            log.error(ste.toString());
        }
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEntityNotFound(final EntityNotFoundException e) {
        for (StackTraceElement ste : e.getStackTrace()) {
            log.error(ste.toString());
        }
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(DateTimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDateTimeException(final DateTimeException e) {
        for (StackTraceElement ste : e.getStackTrace()) {
            log.error(ste.toString());
        }
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
        for (StackTraceElement ste : e.getStackTrace()) {
            log.error(ste.toString());
        }
        return new ErrorResponse("Object already exist " + e.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingServletRequestParameterException(final MissingServletRequestParameterException e) {
        for (StackTraceElement ste : e.getStackTrace()) {
            log.error(ste.toString());
        }
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        for (StackTraceElement ste : e.getStackTrace()) {
            log.error(ste.toString());
        }
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(UnpublishedEventException.class)
    @ResponseStatus(HttpStatus.LOCKED)
    public ErrorResponse handleUnpublishedEventException(final UnpublishedEventException e) {
        for (StackTraceElement ste : e.getStackTrace()) {
            log.error(ste.toString());
        }
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleRemainingErrors(final Throwable e) {
        for (StackTraceElement ste : e.getStackTrace()) {
            log.error(ste.toString());
        }
        return new ErrorResponse(e.getMessage());
    }
}