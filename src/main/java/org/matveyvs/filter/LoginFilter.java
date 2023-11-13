//package org.matveyvs.filter;
//
//import jakarta.servlet.*;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.Arrays;
//
//@Component
//@Log4j2
//public class LoginFilter implements Filter {
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
//            throws IOException, ServletException {
//        log.info("Login filter has launched");
//        servletRequest.getParameterMap().forEach((k, v) -> log.info(k + " : " + Arrays.toString(v)));
//        filterChain.doFilter(servletRequest, servletResponse);
//    }
//}
