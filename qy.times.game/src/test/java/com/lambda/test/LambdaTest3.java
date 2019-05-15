package com.lambda.test;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Predicate;

import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue.Consumer;

public class LambdaTest3 {
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
	
	private static void checkAndExecutor(List<Person> personList, Predicate<Person> predicate, Consumer<Person> consumer) {
		personList.forEach(p -> {
			if (predicate.test(p)) {
				consumer.accept(p);
			}
		});
	}

}
