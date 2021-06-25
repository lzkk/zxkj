package com.zxkj.common.practice.list;
 
import java.util.ArrayList;
import java.util.List;
 
public class Demo06 {
	public static void main(String[] args) {
		List<Long> list = new ArrayList<>();
		
		// 封装到集合
		for (long i = 1; i <= 100; i++) {
			list.add(i);
		}
		
		// 计算
		// reduce：参1，和的初始值
		Long sum = list.stream().parallel().reduce(0L, (r, l) -> r + l);
		
		System.out.println(sum);
	}
 
}