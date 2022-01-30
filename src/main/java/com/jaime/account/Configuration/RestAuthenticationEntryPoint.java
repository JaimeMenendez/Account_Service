package com.jaime.account.Configuration;

import com.jaime.account.Services.LogService;
import com.jaime.account.util.EventAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter out = response.getWriter();
        out.print("{\n" +
                "  \"timestamp\": \"" + new Date() + "\",\n" +
                "  \"status\": " + 401 + ",\n" +
                "  \"error\": \"Unauthorized\",\n" +
                "  \"message\": \"User account is locked\",\n" +
                "  \"path\": \"" + request.getServletPath() + "\"\n" +
                "}");
        out.flush();
        out.close();
    }

}