package com.company;

import java.util.Arrays;
import java.util.GregorianCalendar;

public class Main {

    public static void main(String[] args) {
        ProcessorController pc = null;

        if(args.length != 2)
            pc = ProcessorController.getInstance();
        else
            pc = ProcessorController.getInstance(Integer.parseInt(args[0]), Integer.parseInt(args[1]));

        pc.fillArray();
        bubbleSort(pc.getArray());

        pc.start();
        pc.printStatistics();
    }

    private static int[] bubbleSort(int[] array) {
        long start = new GregorianCalendar().getTimeInMillis();

        int[] arr = Arrays.copyOf(array, array.length);
        for (int i = arr.length - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int t = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = t;
                }
            }
        }

        long end = new GregorianCalendar().getTimeInMillis();

        System.out.println("Обычная сортировка: " + ((int)(end - start)) + "ms");
        System.out.println("-------");

        return arr;
    }
}
