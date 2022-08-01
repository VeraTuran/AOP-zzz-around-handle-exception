package com.luv2code.aopdemo;

import java.util.logging.Logger;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.luv2code.aopdemo.service.TrafficFortuneService;

public class AroundWithLoggerDemoApp {

	public static void main(String[] args) {
		final Logger myLogger = Logger.getLogger(AroundWithLoggerDemoApp.class.getName());
		// lire la classe spring config
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DemoConfig.class);

		// choper bean du conteneur spring
		TrafficFortuneService theFortuneService = context.getBean("trafficFortuneService", TrafficFortuneService.class);

		// messages diagnostiques
		myLogger.info("\nMain Program: AroundDemoApp");
		myLogger.info("Calling getFortune()");

		// appeler notre nouvelle methode
		String data = theFortuneService.getFortune();

		myLogger.info("Mon horoscope aujourd'hui : " + data);
		myLogger.info("FIN");

		// close the spring context
		context.close();
	}

}
