//package org.matveyvs.filter;
//
//import jakarta.servlet.*;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//
//@Component
//@Log4j2
//public class CharsetFilter implements Filter {
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        log.info("Authorisation filter has launched");
//        servletRequest.setCharacterEncoding(StandardCharsets.UTF_8.name());
//        servletResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
//        filterChain.doFilter(servletRequest, servletResponse);
//    }
//}
