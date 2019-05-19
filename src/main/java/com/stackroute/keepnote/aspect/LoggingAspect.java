package com.stackroute.keepnote.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/* Annotate this class with @Aspect and @Component */
@Aspect
@Component
public class LoggingAspect {

	/*
	 * Write loggers for each of the methods of controller, any particular method
	 * will have all the four aspectJ annotation
	 * (@Before, @After, @AfterReturning, @AfterThrowing).
	 */
	private Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

	@Before("execution(* com.stackroute.keepnote.controller..*(..))")
	public void logBefore(JoinPoint joinPoint) {

		LOGGER.info("logBefore() is running!");
		LOGGER.info("LoggingAspect : " + joinPoint.getSignature().getName());
		LOGGER.info("******");
	}

	@After("execution(* com.stackroute.keepnote.controller..*(..))")
	public void logAfter(JoinPoint joinPoint) {

		LOGGER.info("logAfter() is running!");
		LOGGER.info("LoggingAspect : " + joinPoint.getSignature().getName());
		LOGGER.info("******");

	}

	@AfterReturning(pointcut = "execution(* com.stackroute.keepnote.controller..*(..))", returning = "result")
	public void logAfterReturning(JoinPoint joinPoint, Object result) {

		LOGGER.info("logAfterReturning() is running!");
		LOGGER.info("LoggingAspect : " + joinPoint.getSignature().getName());
		LOGGER.info("Method returned value is : " + result);
		LOGGER.info("******");

	}

	@AfterThrowing(pointcut = "execution(* com.stackroute.keepnote.controller..*(..))", throwing = "error")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {

		LOGGER.info("logAfterThrowing() is running!");
		LOGGER.info("LoggingAspect : " + joinPoint.getSignature().getName());
		LOGGER.info("Exception : " + error);
		LOGGER.info("******");

	}
}
