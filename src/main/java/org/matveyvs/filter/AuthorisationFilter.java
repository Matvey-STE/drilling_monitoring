//package org.matveyvs.filter;
//
//import jakarta.servlet.*;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.Set;
//
//import static org.matveyvs.utils.UrlPath.LOGIN;
//import static org.matveyvs.utils.UrlPath.REGISTRATION;
//@Log4j2
//@Component
//public class AuthorisationFilter implements Filter {
//    private static final Set<String> PUBLIC_PATH = Set.of(LOGIN, REGISTRATION);
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        log.info("Authorisation filter has launched");
//        var requestURI = ((HttpServletRequest) servletRequest).getRequestURI();
//        if (isPublicPath(requestURI) || isUserLoggedIn(servletRequest)){
//            filterChain.doFilter(servletRequest,servletResponse);
//        } else {
//            ((HttpServletResponse) servletResponse).sendRedirect("/login");
//        }
//    }
//
//    private boolean isPublicPath(String requestURI) {
//        return PUBLIC_PATH.stream().anyMatch(requestURI::startsWith);
//    }
//
//    private boolean isUserLoggedIn(ServletRequest servletRequest) {
//        var user = ((HttpServletRequest) servletRequest).getSession().getAttribute("user");
//        return user != null;
//    }
//
//}
