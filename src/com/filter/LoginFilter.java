package com.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter("/*")
public class LoginFilter implements Filter {
    @Override
    public void destroy() { }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        String uri = request.getRequestURI();
        // 用户访问除了资源文件、主页、、登录servlet、注册servlet之外，都要进行登录状态检查
        if (uri.contains("/index.jsp") || uri.contains("/loginServlet") || uri.contains("/css/")
                || uri.contains("/js/") || uri.contains("/images/") || uri.contains("/registerServlet")) {
            chain.doFilter(req, resp);
        } else {
            Object username = request.getSession().getAttribute("username");
            if (username != null) {
                chain.doFilter(req, resp);
            } else {
                request.setAttribute("login_msg", "您尚未登录，请登录");
                request.getRequestDispatcher("/index.jsp").forward(request, resp);
            }
        }
    }

    @Override
    public void init(FilterConfig config) throws ServletException { }
}
