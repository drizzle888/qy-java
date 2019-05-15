package com.stack.test;

import java.util.List;
import java.util.Stack;

public class StackTest {

	public static void main(String[] args) {
		Stack<String> stack = new Stack<String>();
		// 将1,2,3,4,5添加到栈中
		for (int i = 1; i < 6; i++) {
			stack.push(String.valueOf(i));
		}
		String val1 = (String) stack.peek();
		System.out.println("val1=" + val1);
		iteratorThroughRandomAccess(stack);
		String val2 = (String) stack.pop();
		System.out.println("val2=" + val2);
		iteratorThroughRandomAccess(stack);
		String val3 = (String) stack.peek();
		System.out.println("val3=" + val3);
		iteratorThroughRandomAccess(stack);
	}

	/**
	 * 通过快速访问遍历Stack
	 */
	public static void iteratorThroughRandomAccess(List<String> list) {
		String val = null;
		for (int i = 0; i < list.size(); i++) {
			val = (String) list.get(i);
			System.out.print(val + " ");
		}
		System.out.println();
	}
}