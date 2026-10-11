package com.test.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

import com.test.entity.User;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 1. 获取请求路径
        String uri = request.getRequestURI();

        // 2. 放行所有以 /api/user/login 和 /api/user/register 开头的请求
        //    以及商品列表、分类列表等不需要登录的接口
        if (uri.contains("/user/login") || uri.contains("/user/register") ||
                uri.contains("/product/list") || uri.contains("/product/detail") ||
                uri.contains("/category/list")) {
            return true;
        }

        // 3. 检查 Session 中是否有用户
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // 4. 如果用户未登录，返回 401 状态码
        if (user == null) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"msg\":\"请先登录\",\"data\":null}");
            return false;
        }

        return true;
    }
}