package com.example.demo.common;

import java.io.IOException;

import com.example.demo.common.util.DateUtils;

public class T {

    public static void main(String[] args) throws IOException {

        int day=30;
        long t1 = day *  DateUtils.SECOND_PER_DAY * 1000L;
        
        long t2 = System.currentTimeMillis();
        long t = t1 + t2;

        System.out.println(t);
        System.out.println(t1);
        System.out.println(t2);
    }

}
