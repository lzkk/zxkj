package com.zxkj.common.practice.jdk.divide;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yuh
 * @ClassName: HeapOutOfMemory
 * @Description: 堆溢出测试
 * @date 2017年2月20日 下午1:17:41
 */
public class HeapOutOfMemory {
    public static void main(String[] args) {
        List<TestCase> cases = new ArrayList<TestCase>();
        while (true) {
            cases.add(new TestCase());
        }
    }
}

class TestCase {

}