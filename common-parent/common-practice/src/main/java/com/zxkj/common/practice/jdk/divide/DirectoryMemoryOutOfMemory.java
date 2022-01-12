package com.zxkj.common.practice.jdk.divide;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author yuh
 * @ClassName: DirectoryMemoryOutOfmemory
 * @Description: 直接内存溢出测试
 * @date 2017年2月20日 下午1:31:52
 */
@SuppressWarnings("restriction")
public class DirectoryMemoryOutOfMemory {
    private static final int ONE_MB = 1024 * 1024;
    private static int count = 1;

    public static void main(String[] args) {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            Unsafe unsafe = (Unsafe) field.get(null);
            while (true) {
                unsafe.allocateMemory(ONE_MB);
                count++;
            }
        } catch (Exception e) {
            System.out.println("Exception:instance created " + count);
            e.printStackTrace();
        } catch (Error e) {
            System.out.println("Error:instance created " + count);
            e.printStackTrace();
        }
    }
}