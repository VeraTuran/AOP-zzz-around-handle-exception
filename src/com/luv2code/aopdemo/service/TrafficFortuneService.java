package com.luv2code.aopdemo.service;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

@Component
public class TrafficFortuneService {
	public String getFortune() {
		// simuler un delai
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// retourner l'horoscope
		return "Vous serez en retard aujourd'hui...";

	}

	public String getFortune(boolean tripWire) {
		if (tripWire) {
			throw new RuntimeException("Oh-oooh! Il y a des travaux sur votre chemin !");
		}
//		return "Vous serez en retard aujourd'hui...";
		return getFortune();
	}
}
