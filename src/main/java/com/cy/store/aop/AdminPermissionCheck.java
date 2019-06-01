package com.cy.store.aop;

import com.cy.store.config.AdminConfig;
import com.cy.store.service.AdminService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Aspect
@Component
public class AdminPermissionCheck extends AdminConfig {

    @Autowired
    private AdminService adminService;

    @Pointcut("within(com.cy.store.server..*)"+
            "&& !within(com.cy.store.server.LoginController)")
    public void privilege(){}

    @ResponseBody
    @Around("privilege()")
    public Object isAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes =   (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session= request.getSession();

        if(session.getAttribute(adminAccount) == null)
            return "/admin/login";

        //获取访问目标方法
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();

        try {
            final String methodAccess = signature.getMethod().getAnnotation(Permission.class).value();
            if (!StringUtils.isBlank(methodAccess) && !session.getAttribute(adminAuth).toString().contains(methodAccess)){
                return "/error/403";
            }
        }catch (NullPointerException e){
            System.out.println(e.getMessage());
        }

        //获取访问类
        try {
            final String classAccess = joinPoint.getTarget().getClass().getAnnotation(Permission.class).value();
            if (!StringUtils.isBlank(classAccess) && !session.getAttribute(adminAuth).toString().contains(classAccess)){
                return "/error/403";
            }
        }catch (NullPointerException e){
            System.out.println(e.getMessage());
        }

        return joinPoint.proceed();
    }
}
