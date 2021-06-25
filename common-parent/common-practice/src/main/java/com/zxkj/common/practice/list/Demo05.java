package com.zxkj.common.practice.list;
 
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
 
public class Demo05 {
	public static void main(String[] args) {
		List<String> list = new ArrayList<>();
		
		list.add("abc");
		list.add("ab");
		list.add("abcd");
		list.add("abcde");
		
		// 获取最大值
		int max = list.stream().map((s) -> s.length()).max(Integer :: compareTo).get();
		System.out.println(max);
		
		// 获取最小值，另一种方法
		int min = list.stream().min(Comparator.comparing((s) -> s.length())).get().length();
		System.out.println(min);
	}
 
}