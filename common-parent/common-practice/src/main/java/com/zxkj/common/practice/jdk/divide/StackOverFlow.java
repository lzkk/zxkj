package com.zxkj.common.practice.jdk.divide;

/**
 * 
 * @ClassName: StackOverFlow(jdk8默认大小1M)
 * @Description: java栈溢出
 * @author yuh
 * @date 2017年2月20日 下午1:24:25
 *
 */
public class StackOverFlow {
	private int i;

	public void plus() {
		i++;
		plus();
	}

	public static void main(String[] args) {
		StackOverFlow stackOverFlow = new StackOverFlow();
		try {
			stackOverFlow.plus();
		} catch (Exception e) {
			System.out.println("Exception:stack length:" + stackOverFlow.i);
			e.printStackTrace();
		} catch (Error e) {
			System.out.println("Error:stack length:" + stackOverFlow.i);
			e.printStackTrace();
		}
	}
}