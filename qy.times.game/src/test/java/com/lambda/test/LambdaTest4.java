package com.lambda.test;

import java.util.Arrays;
import java.util.List;

public class LambdaTest4 {
	public static void main(String[] args) {
		List<Person> guiltyPersons = Arrays.asList(
				new Person("yixing", "Zhao", 25),
				new Person("yanggui", "Li", 30),
				new Person("chao", "Ma", 29)
				);
		
		guiltyPersons.stream().filter(p -> p.getFirstName().startsWith("Z")).forEach(System.out::println);
	}
	
}
