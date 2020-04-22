package ppj.weather.web;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class mOnlyGet extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(request.getMethod().equals("GET") == false) {
            response.getWriter().write("Only GET method is allowed");
            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);

            return false;
        }

        return true;
    }

}
