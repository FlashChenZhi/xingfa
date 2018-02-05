package com.wms;

public class Test {
    public static void main(String[] args) {
        while (true) {
            System.out.println("Hello World");

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
