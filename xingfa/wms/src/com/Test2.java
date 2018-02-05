package com;

public class Test2 {
    public static void main(String[] args) {
        while (true){
            try {
                System.out.println("test");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
