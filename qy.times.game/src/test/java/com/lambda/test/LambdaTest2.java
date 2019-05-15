package com.lambda.test;

import java.util.Arrays;
import java.util.List;

public class LambdaTest2 {
	public static void main(String[] args) {
		List<Person> guiltyPersons = Arrays.asList(
				new Person("yixing", "Zhao", 25),
				new Person("yanggui", "Li", 30),
				new Person("chao", "Ma", 29)
				);
		
		checkAndExecutor(guiltyPersons
				, p -> p.getFirstName().startsWith("Z")
				, p -> System.out.println(p.getFirstName()));
	}
	
	private static void checkAndExecutor(List<Person> personList, NameChecker nameChecker, Executor executor) {
		for (Person p : personList) {
			if (nameChecker.check(p)) {
				executor.execute(p);
			}
		}
	}

	@FunctionalInterface
	interface NameChecker {
		boolean check(Person p);
	}
	
	@FunctionalInterface
	interface Executor {
		void execute(Person p);
	}
	
}
