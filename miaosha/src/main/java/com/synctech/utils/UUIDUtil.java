package com.synctech.utils;

import java.util.UUID;

public class UUIDUtil {

	public static String uuid(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	public static void main(String[] args) {
//		String st = "Java String";
//	    char result = st.charAt(10);
//	    System.out.println(result);
//		String st1 = "Hello";
//        String st2 = "World";
//        System.out.println(st1.compareTo(st2));
//		String name = "Hello";
//        System.out.println(name.contains("World"));
//        System.out.println(name.contains("Java"));
//        System.out.println(name.contains("Community"));
		String s1 = "JavaFolder";
        String s2 = "JavaFolder";
        String s3 = "javafolder";
        String s4 = "JAVAFOLDER";
        System.out.println(s1.equals(s2));
        System.out.println(s1.equals(s3));
        System.out.println(s1.equals(s4));
        String st = String.format("First: %1$d Second: %2$d Third: %3$d", 100, 200, 300);
        System.out.println(st);

	}
}
