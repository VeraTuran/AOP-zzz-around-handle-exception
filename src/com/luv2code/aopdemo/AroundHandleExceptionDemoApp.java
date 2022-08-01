package com.luv2code.aopdemo;

import java.util.logging.Logger;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.luv2code.aopdemo.service.TrafficFortuneService;

public class AroundHandleExceptionDemoApp {

	public static void main(String[] args) {
		final Logger myLogger = Logger.getLogger(AroundHandleExceptionDemoApp.class.getName());
		// lire la classe spring config
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DemoConfig.class);

		// choper bean du conteneur spring
		TrafficFortuneService theFortuneService = context.getBean("trafficFortuneService", TrafficFortuneService.class);

		// messages diagnostiques
		myLogger.info("\nMain Program: AroundDemoApp");
		myLogger.info("Calling getFortune()");

		// simuler exception et appeler la methode
		boolean tripWire = true;

		String data = theFortuneService.getFortune(tripWire);

		myLogger.info("Mon horoscope aujourd'hui : " + data);
		myLogger.info("FIN");

		// close the spring context
		context.close();
	}

}
