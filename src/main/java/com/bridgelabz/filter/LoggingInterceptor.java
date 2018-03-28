package com.bridgelabz.filter;

import javax.servlet.http.HttpServletRequest;

import com.bridgelabz.user.model.User;
import com.bridgelabz.user.service.UserService;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


import com.bridgelabz.user.util.TokenUtils;
@Component
public class LoggingInterceptor implements HandlerInterceptor {
	@Autowired
	private UserService userService;

	private static final Logger logger = Logger.getLogger(LoggingInterceptor.class);
	private static final String OPTIONS = "OPTIONS";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)

			throws Exception 
	   {
	
		if (!request.getMethod().equals(OPTIONS)) {
			try {
				int userId = TokenUtils.verifyToken(request.getHeader("Authorization"));
				request.setAttribute("userId",userId);
				
				logger.info("this  is interceptor");
				//check user null
				User user=userService.getUserById(userId);
				if(user==null)
				{
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;

	   }

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception 
	{
		logger.info("After handling the request");
		System.out.println("After handling the request");

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception 
	{
		logger.info("After rendering the view");
		System.out.println("After rendering the view");

	}
}
