package com.stack.test;

public class BuffTest {
	
	public static void main(String[] args) {
		int value = 100;
		System.out.println("value=" + toBinaryString(value));
		byte[] buff = getBytes(value);
		for (int i = 0; i < buff.length; i++) {
			System.out.print(buff[i] + " ");
		}
	}

	public static byte[] getBytes(int data) {
	    byte[] buff = new byte[4];
	    for (int i = 0; i < buff.length; i++) {
	    	/**
	    	 与&运算(真真为真，真假为假，假假为假)，特点是只要有假就为假，都是真才为真
	    	 实际是在底层全部转换成二进制的与运算
	    	 0x00ff是255的十六进制，一个数n与 0000000011111111计算
	    	 高8位和0与运算必定为0，低8位和1与运算得本身，留下来的就是低8位的值
	    	 最终取到的值为低8位的值
	    	 */
	        buff[i] = (byte)(data & 0x000000ff);
	        System.out.println(String.format("buff[%s]=%s", i, toBinaryString(buff[i])));
	        /**
	         * 各二进制向右移8位，高位补0，即剔除低位8位的数据
	         */
	        data >>= 8;
	    	System.out.println(String.format("第%s次右移后，data=%s", i, toBinaryString(data)));
	    }
	    return buff;
	}
	
	public static String toBinaryString(int value) {
		return String.format("%s",Integer.toBinaryString(value));
	}
}


