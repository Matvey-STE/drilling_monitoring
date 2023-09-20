package org.matveyvs.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.matveyvs.dto.CreateUserDto;

import java.io.IOException;

@WebFilter("/admin")
public class UnsaveFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        var user = (CreateUserDto)((HttpServletRequest) servletRequest).getSession().getAttribute("user");
        if(user != null){
            filterChain.doFilter(servletRequest,servletResponse);
        } else {
            ((HttpServletResponse)servletResponse).sendRedirect("/registration");
        }
    }
}
