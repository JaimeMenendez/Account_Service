package com.jaime.account.Listener;

import com.jaime.account.Services.LogService;
import com.jaime.account.util.EventAction;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Lazy
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final LogService logService;

    public CustomAccessDeniedHandler(LogService logService) {
        this.logService = logService;
    }


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        // Loggin action
        String user = request.getUserPrincipal().getName();
        String path = request.getRequestURI();
        logService.logAction(EventAction.ACCESS_DENIED, user, path, path);
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied!");
    }

}
