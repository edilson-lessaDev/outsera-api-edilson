package br.com.outsera.api.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(1)
public class ApiKeyFilter implements Filter {

    @Value("${api.security.key}")
    private String apiKey;

    private static final String HEADER = "X-API-KEY";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();

        // libera actuator
        if (path.contains("/actuator")) {
            chain.doFilter(request, response);
            return;
        }

        // protege endpoint
        if (path.contains("/producers")) {

            String header = req.getHeader(HEADER);

            if (header == null || !header.equals(apiKey)) {
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                res.getWriter().write("Unauthorized");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}