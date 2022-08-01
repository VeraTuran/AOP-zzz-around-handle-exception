package com.luv2code.aopdemo.aspect;

import java.util.List;
import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.luv2code.aopdemo.Account;

@Aspect
@Component
@Order(2)
public class MyDemoLoggingAspect {

	// @Around advice
	// cas d'utilisation:
	// pre-traiter ou post-traiter des données, vérifications, logging, securité
	// le profilage : combien de temps, quelles fonctions, combien de mémoire EXO
	// résoudre des exceptions (swallow, handle, stop) EXO
	// rendre l'exception au programme main EXO

	// Il se declenche à l'entrée et au retour de l'execution d'une méthode [img]

	// Quand on veut implémenter un @Around advice, il nous faut une référence à un
	// "proceeding join point". Un proceeding join point nous permet d'accéder à la
	// methode visée, et de l'executer ensuite.

	// On va essayer de savoir combien de temps rend une methode getFortuneService()
	// à executer. On écrit donc:

	// @Around("execution(*com.luv2code.aopdemo.service.*.getFortune(..))")
	// public Object aroundGetFortune(ProceedingJoinPoint pjp) throws Throwable {
	// return Object
	// }

	private Logger myLogger = Logger.getLogger(getClass().getName());

//	//////////////////// PROFILAGE:
//
//	@Around("execution(* com.luv2code.aopdemo.service.*.getFortune(..))")
//	public Object aroundGetFortune(ProceedingJoinPoint pjp) throws Throwable {
//		// imprimer methode de cet advice
//		String method = pjp.getSignature().toShortString();
//		myLogger.info("\n---------------> Executing @Around on method: " + method);
//
//		// on obtient l'horodotage du debut
//		long begin = System.currentTimeMillis();
//
//		// on execute la methode : avec le ProceedingJoinPoint on peut marquer la
//		// methode (comme un simple JoinPoint), mais le .proceed associé donne plus de
//		// contrôle pour le faire executer
//		// essaie de le faire tourner en commentant cette ligne ;)
//		Object result = pjp.proceed();
//
//		// on obtient l'horodotage de la fin
//		long end = System.currentTimeMillis();
//
//		// on calcule la durée
//		long duration = end - begin;
//		myLogger.info("\n------------> Duree : " + duration / 1000 + "secondes");
//
//		return result;
//	}

	//////////////////// Exception Handling:

	@Around("execution(* com.luv2code.aopdemo.service.*.getFortune(..))")
	public Object aroundGetFortune(ProceedingJoinPoint pjp) throws Throwable {
		// imprimer methode de cet advice
		String method = pjp.getSignature().toShortString();
		myLogger.info("\n---------------> Executing @Around on method: " + method);

		// on obtient l'horodotage du debut
		long begin = System.currentTimeMillis();

		// on execute la methode
		Object result = null;
		try {
			result = pjp.proceed();
		} catch (Exception e) {
			// logger l'exception
			myLogger.warning(e.getMessage());

//			// retourner un message custom à l'utilisateur
//			result = "Votre chemin est en travaux, mais AOP vous envoie un helicopter !";

			// sinon, pour rendre l'exception:
			throw e;
		}

		// on obtient l'horodotage de la fin
		long end = System.currentTimeMillis();

		// on calcule la durée
		long duration = end - begin;
		myLogger.info("\n------------> Duree : " + duration / 1000 + "secondes");

		return result;
	}

	@After("execution(* com.luv2code.aopdemo.dao.AccountDAO.findAccounts(..))")
	public void afterFinallyFindAccountsAdvice(JoinPoint theJoinPoint) {
		// imprimer methode de cet advice
		String method = theJoinPoint.getSignature().toShortString();
		myLogger.info("\n---------------> Executing @After on method: " + method);
	}

	@AfterThrowing(pointcut = "execution(* com.luv2code.aopdemo.dao.AccountDAO.findAccounts(..))", throwing = "theExc")
	public void afterThrowingFindAccountsAdvice(JoinPoint theJoinPoint, Throwable theExc) {
		// imprimer methode de cet advice
		String method = theJoinPoint.getSignature().toShortString();
		myLogger.info("\n---------------> Executing @AfterThrowing on method: " + method);
		// logger l'exception
		myLogger.info("\n---------------> result is: " + theExc);
	}

	@AfterReturning(pointcut = "execution(* com.luv2code.aopdemo.dao.AccountDAO.findAccounts(..))", returning = "result")
	public void afterReturningFindAccountsAdvice(JoinPoint theJoinPoint, List<Account> result) {
		// imprimer le nom de la methode sur laquelle on aura le advice
		String method = theJoinPoint.getSignature().toShortString();
		myLogger.info("\n---------------> Executing @AfterReturning on method: " + method);
		// imprimer les resultats de la methode
		myLogger.info("\n---------------> result is: " + result);
		// modifier les données
		// convertir les noms des Account en MAJUSCULES
		convertAccountNamesToUppercase(result);
		myLogger.info("\n---------------> result is: " + result);
	}

	private void convertAccountNamesToUppercase(List<Account> result) {
		// boucler sur les accounts
		for (Account tempAccount : result) {
			// choper version en majuscules
			String theUpperName = tempAccount.getName().toUpperCase();
			// mettre à jour le nom de l'account
			tempAccount.setName(theUpperName);
		}
	}

	@Before("com.luv2code.aopdemo.aspect.LuvAopExpressions.forDaoPackageNoGetterSetter()")
	public void beforeAddAccountAdvice(JoinPoint theJoinPoint) {
		myLogger.info("\n----------> Executing @Before advice on addAccount()");
		// afficher la signature de la methode
		MethodSignature methodSig = (MethodSignature) theJoinPoint.getSignature();
		myLogger.info("Method: " + methodSig);
		// afficher les args de la methode, pour ce faire:
		//// 1. choper les args
		Object[] args = theJoinPoint.getArgs();
		//// 2. boucler sur les args
		for (Object tempArg : args) {
			myLogger.info(tempArg.toString());

			if (tempArg instanceof Account) {
				Account theAccount = (Account) tempArg;
				myLogger.info("account name: " + theAccount.getName());
				myLogger.info("account level: " + theAccount.getLevel());

			}

		}
	}

}
