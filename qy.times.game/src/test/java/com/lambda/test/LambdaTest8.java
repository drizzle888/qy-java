package com.lambda.test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LambdaTest8 {
	public static void main(String[] args) {
		LambdaTest8 lambdaTest8 = new LambdaTest8();
		lambdaTest8.test();
	}
	
	void test() {
		Student student = new Student("张三", 98, 1);
		List<Student> stuList = new ArrayList<Student>();
		stuList.add(student);
		stuList.add(student);
			    
		List<Student> stuList1 = stuList.stream().distinct().collect(Collectors.toList());
		System.out.println(stuList1);
	    
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
