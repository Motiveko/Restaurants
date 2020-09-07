package com.motiveko.restaurants.aop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

@Aspect
@Component
public class SessionChecker {


//	@Pointcut("* com.motiveko.restaurants..ViewController.(..)")
//	public void checkValidUser() {}

//	@Around("excution(* com.motiveko.restaurants..ViewController.*())"
//			+  "&&!execution(* com.motiveko.restaurants..ViewController.login*(*)")
	
	@Around("execution(* com.motiveko.restaurants..ViewController.*(..))"
			+ "&&!execution(* com.motiveko.restaurants..ViewController.*login*(..))")
	public String checkValidUser(ProceedingJoinPoint joinPoint) throws Throwable {
		System.out.println("Session Check");
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		HttpSession session = request.getSession();

		if(session.getAttribute("sEmail")==null) {
			return "login";
		} else {
			return (String) joinPoint.proceed();
		}
	}
	
	@Around("execution(* com.motiveko.restaurants..ViewController.*login*(..))")
	public String checkLogin(ProceedingJoinPoint joinPoint) throws Throwable {
		System.out.println("Check Login");
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		HttpSession session = request.getSession();

		if(session.getAttribute("sEmail")!=null) { 
			return "index";
		} else {
			return (String) joinPoint.proceed();
		}
	}	
}
