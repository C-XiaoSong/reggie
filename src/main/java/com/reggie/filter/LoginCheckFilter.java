package com.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.reggie.utis.BaseContext;
import com.reggie.utis.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 * @author：陈晓松
 * @CLASS_NAME：LoginCheckFilter
 * @date：2023/4/18 19:55
 * @注释：检查用户是否已经完成登录（过滤器）
 */
@Slf4j
@WebFilter(filterName = "LoginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    // 路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        log.info("拦截到的请求：{}",req.getRequestURI());
        // 1、获取本次请求的URI
        String requestURI = req.getRequestURI(); // /backend/index.html
        // 2、不需要拦截的请求
        String[] urls = new String[]{
          "/employee/login",
          "/employee/logout",
          "/backend/**",
          "/front/**",
          "/user/sendMsg",
          "/user/login"
        };
        // 3、判断本次请求是否需要处理
        boolean check = check(urls, requestURI);
        // 4、不需要处理就直接放行
        if (check) {
            log.info("本次请求不需要处理：{}",requestURI);
            chain.doFilter(req,res);
            return;
        }
        // 5-1、如果已登录，直接放行
        if (req.getSession().getAttribute("employee") != null){
            log.info("用户已登录，Id为：{}",req.getSession().getAttribute("employee"));
            // 把用户ID传到工具类
            Long empId = (Long) req.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);
            chain.doFilter(req,res);
            return;
        }
        // 5-2、如果已登录，直接放行（移动端登录的操作）
        log.info("用户已登录，Id为：{}",req.getSession().getAttribute("user"));
        if (req.getSession().getAttribute("user") != null){
            log.info("用户已登录，Id为：{}",req.getSession().getAttribute("user"));
            // 把用户ID传到工具类
            Long userId = (Long) req.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);
            chain.doFilter(req,res);
            return;
        }
        // 6、如果未登录，返回登录页面，通过输出流向客户端响应数据
        log.info("用户未登录");
        res.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }


    /**
     * 路径匹配，检查本次请求是否需要放行
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls,String requestURI){
        for (String url:urls){
            // match() 匹配的意思
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match){
                return true;
            }
        }
        return false;
    }
}
