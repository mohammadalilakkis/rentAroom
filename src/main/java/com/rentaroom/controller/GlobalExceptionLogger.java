package com.rentaroom.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@ControllerAdvice
public class GlobalExceptionLogger {

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex, HttpServletRequest request) {
        // #region agent log
        try {
            Path logPath = Paths.get(System.getProperty("user.dir"), ".cursor", "debug.log");
            Files.createDirectories(logPath.getParent());
            try (FileWriter fw = new FileWriter(logPath.toString(), true)) {
                String msg = ex.getMessage() != null ? ex.getMessage().replace("\"", "'") : "null";
                String causeMsg = ex.getCause() != null && ex.getCause().getMessage() != null
                    ? ex.getCause().getMessage().replace("\"", "'") : "null";
                fw.write(String.format("{\"location\":\"GlobalExceptionLogger\",\"message\":\"500 exception\",\"data\":{\"requestUri\":\"%s\",\"type\":\"%s\",\"message\":\"%s\",\"causeType\":\"%s\",\"causeMessage\":\"%s\"},\"timestamp\":%d,\"hypothesisId\":\"global\"}\n",
                    request.getRequestURI(),
                    ex.getClass().getName(),
                    msg,
                    ex.getCause() != null ? ex.getCause().getClass().getName() : "null",
                    causeMsg,
                    System.currentTimeMillis()));
            }
        } catch (Exception e) { /* ignore */ }
        // #endregion
        throw new RuntimeException(ex);
    }
}
