package com.pkg.event.test;

import com.common.event.ManualResetEvent;

public class ManualResetEventTest {
	//默认信号为不发送状态
	private static ManualResetEvent mre;
	
	public static void main(String[] args) {
//		test1();
		test2();
	}
	
	public static void test1() {
		mre = new ManualResetEvent(false);
		System.out.println("周瑜(主线程)和诸葛亮(子线程)商量好，周瑜(主线程)执行火攻，诸葛亮(子线程)执行借东风");
		// 诸葛亮(子线程)去借东风
		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("诸葛亮登上祭祀台开始借东风");
				for (int i = 0; i <= 5; i++) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("东风吹啊吹，越来越近了...");
				}
				System.out.println("诸葛亮(子线程)通知周瑜(主线程)东风借到了，准备进攻了");
				mre.set();
			}
		}).start();

		// 万事俱备只欠东风
		mre.waitOne();
		System.out.println("东风终于到了");
		
		// 东风到了，可以进攻了
		System.out.println("进攻了，满载燃料的大船接着东风冲向曹操的战船");
	}
	
	public static void test2() {
		mre = new ManualResetEvent(false);
		System.out.println("周瑜(主线程)和诸葛亮(子线程)商量好，周瑜(主线程)执行火攻，诸葛亮(子线程)执行借东风");
		// 诸葛亮(子线程)去借东风
		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("诸葛亮登上祭祀台开始借东风");
				for (int i = 0; i <= 5; i++) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("东风吹啊吹，越来越近了...");
				}
				System.out.println("诸葛亮(子线程)通知周瑜(主线程)东风借到了，可是周瑜已经出发了...");
				mre.set();
			}
		}).start();

		// 万事俱备只欠东风
		mre.waitOne(2000);
		System.out.println("东风久等不来，周瑜实在按耐不住");
		
		// 东风到了，可以进攻了
		System.out.println("进攻了，满载燃料的大船逆风驶向曹操的战船");
	}
}