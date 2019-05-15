package com.lambda.test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LambdaTest6 {
	public static void main(String[] args) {
		LambdaTest6 lambdaTest5 = new LambdaTest6();
		lambdaTest5.test();
	}
	
	void test() {
		List<Student> stuList = Arrays.asList(new Student("张三", 98, 1)
				, new Student("李四", 98, 0)
				, new Student("王五", 95, 1)
		);
			    
	    Optional<Student> scoreMaxBy = stuList.stream()
	            .collect(Collectors.maxBy(Comparator.comparingInt(Student::getScore)));
	    Student maxStudent = scoreMaxBy.get();
	    System.out.println("成绩最好的学生：" + maxStudent.getName());
	    
	    List<Student> manList = stuList.stream().filter(s -> s.getSex().intValue() == 1).collect(Collectors.toList());
	    System.out.println("男学生列表：");
	    manList.forEach(s -> System.out.print(" " + s.getName()));
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
