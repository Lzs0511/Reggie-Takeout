package com.kpsilent.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.kpsilent.reggie.common.BaseContext;
import com.kpsilent.reggie.common.R;
import com.kpsilent.reggie.entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否已经完成登录
 */
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    // 路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 1. 获取本次请求的URI
        String requestURI = request.getRequestURI();
        log.info("拦截到请求 {}", request.getRequestURI());
        // 定义不需要处理的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/login",
                "/user/sendMsg"
        };

        // 2. 判断本次请求是否需要处理
        boolean check = check(urls, requestURI);

        // 3. 如果不需要处理则直接方行
        if(check){
            log.info("本次请求{}不需要处理", requestURI);
            filterChain.doFilter(request, response);
            return ;
        }

        // 4-1.判断登录状态，如果已经登录了，则放行
        if(request.getSession().getAttribute("employee") != null){
            log.info("用户已登录, 用户Id为：{}", request.getSession().getAttribute("employee"));

            // 将用户id存入到BaseContext中
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            // 放行
            filterChain.doFilter(request, response);
            return ;
        }
        // 4-2.判断登录状态，如果已经登录了，则放行
        if(request.getSession().getAttribute("user") != null){
            log.info("用户已登录, 用户Id为：{}", request.getSession().getAttribute("user"));

            // 将用户id存入到BaseContext中
            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            // 放行
            filterChain.doFilter(request, response);
            return ;
        }
        log.info("用户未登录");
        // 5. 如果未登录，则返回未登录结果，通过输出流方式向客户端页面响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));


    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     * @param urls
     * @param URI
     * @return
     */
    public boolean check(String[] urls, String URI){
        for (String url : urls) {
          boolean match = PATH_MATCHER.match(url, URI);
          if(match){
              return true;
          }
        }
        return false;
    }


}
