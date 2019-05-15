package com.lambda.test;

import java.util.Arrays;

public class LambdaTest1 {
	public static void main(String[] args) {
		Arrays.asList( "a", "b", "d" ).forEach( e -> System.out.println( e ) );
	}
}
