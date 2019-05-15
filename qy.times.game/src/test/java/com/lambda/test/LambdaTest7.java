package com.lambda.test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LambdaTest7 {
	public static void main(String[] args) {
		LambdaTest7 lambdaTest7 = new LambdaTest7();
		lambdaTest7.test();
	}
	
	void test() {
		List<Student> stuList = Arrays.asList(new Student("张三", 98, 1)
				, new Student("李四", 98, 0)
				, new Student("王五", 95, 1)
		);
			    
		List<Integer> scoreList = stuList.stream().map(Student::getScore).collect(Collectors.toList());
		System.out.println(scoreList);
	    
	}

	class Student {
		public Student(String name, Integer score, Integer sex) {
			this.name = name;
			this.score = score;
			this.sex = sex;
		}

		private String name;
		private Integer score;
		private Integer sex;

		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Integer getScore() {
			return score;
		}
		public void setScore(Integer score) {
			this.score = score;
		}
		public Integer getSex() {
			return sex;
		}
		public void setSex(Integer sex) {
			this.sex = sex;
		}
	}
}
