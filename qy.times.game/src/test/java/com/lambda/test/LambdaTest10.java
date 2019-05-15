package com.lambda.test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class LambdaTest10 {
	public static void main(String[] args) {
		LambdaTest10 lambdaTest9 = new LambdaTest10();
		lambdaTest9.test();
	}
	
	void test() {
		List<Student> stuList = Arrays.asList(new Student("张三", 95, 1)
				, new Student("李四", 95, 0)
				, new Student("王五", 95, 1)
		);
			    
		Optional<Student> op = stuList.stream().min(Comparator.comparing(r -> r.getScore()));
		System.out.println(op.get());
	    
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
