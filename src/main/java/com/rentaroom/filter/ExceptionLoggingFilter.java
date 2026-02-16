package com.rentaroom.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExceptionLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String uri = req.getRequestURI();
        // #region agent log - only log dashboard/admin requests
        if (uri.contains("dashboard") || uri.startsWith("/admin")) {
            try {
                Path logPath = Paths.get(System.getProperty("user.dir"), ".cursor", "debug.log");
                Files.createDirectories(logPath.getParent());
                try (FileWriter fw = new FileWriter(logPath.toString(), true)) {
                    fw.write("{\"location\":\"ExceptionLoggingFilter\",\"message\":\"request\",\"data\":{\"uri\":\"" + uri + "\"},\"timestamp\":" + System.currentTimeMillis() + "}\n");
                }
            } catch (Exception e) { /* ignore */ }
        }
        // #endregion
        try {
            chain.doFilter(request, response);
        } catch (Throwable t) {
            // #region agent log
            try {
                Path logPath = Paths.get(System.getProperty("user.dir"), ".cursor", "debug.log");
                Files.createDirectories(logPath.getParent());
                try (FileWriter fw = new FileWriter(logPath.toString(), true)) {
                    String msg = t.getMessage() != null ? t.getMessage().replace("\"", "'") : "null";
                    String causeMsg = t.getCause() != null && t.getCause().getMessage() != null ? t.getCause().getMessage().replace("\"", "'") : "null";
                    fw.write("{\"location\":\"ExceptionLoggingFilter\",\"message\":\"exception\",\"data\":{\"uri\":\"" + uri + "\",\"type\":\"" + t.getClass().getName() + "\",\"message\":\"" + msg + "\",\"causeType\":\"" + (t.getCause() != null ? t.getCause().getClass().getName() : "null") + "\",\"causeMessage\":\"" + causeMsg + "\"},\"timestamp\":" + System.currentTimeMillis() + "}\n");
                }
            } catch (Exception e) { /* ignore */ }
            // #endregion
            if (t instanceof ServletException) throw (ServletException) t;
            throw new ServletException(t);
        }
    }
}
