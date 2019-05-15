package com.lambda.test;

import java.util.Arrays;
import java.util.List;

public class LambdaTest9 {
	public static void main(String[] args) {
		LambdaTest9 lambdaTest9 = new LambdaTest9();
		lambdaTest9.test();
	}
	
	void test() {
		List<Student> stuList = Arrays.asList(new Student("张三", 98, 1)
				, new Student("李四", 98, 0)
				, new Student("王五", 95, 1)
		);
			    
		boolean isExist = stuList.stream().anyMatch(s -> s.getName().equals("张三"));
		System.out.println(isExist);
	    
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
