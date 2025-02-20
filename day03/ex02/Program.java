package ex02;

import java.util.Random;

public class Program {
    private static int sumByThreads = 0;

    public static void main(String[] args) {
        if (args.length != 2 || !args[0].startsWith("--arraySize=") || !args[1].startsWith("--threadsCount=")) {
            System.out.println("Usage: java Program --arraySize=<number> --threadsCount=<number>");
            return;
        }

        int arraySize, threadsCount;

        try {
            arraySize = Integer.parseInt(args[0].substring("--arraySize=".length()));
            threadsCount = Integer.parseInt(args[1].substring("--threadsCount=".length()));

            if (arraySize > 2000000 || threadsCount > arraySize || threadsCount <= 0 || arraySize <= 0) {
                throw new IllegalArgumentException("Invalid array size or thread count.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: invalid arguments.");
            return;
        }

        int[] array = new int[arraySize];
        Random rand = new Random();
        int totalSum = 0;


        for (int i = 0; i < arraySize; i++) {
            array[i] = rand.nextInt(2001) - 1000;
            totalSum += array[i];
        }

        System.out.println("Sum: " + totalSum);


        Thread[] threads = new Thread[threadsCount];
        int step = arraySize / threadsCount;

        for (int i = 0; i < threadsCount; i++) {
            int start = i * step;
            int end = (i == threadsCount - 1) ? arraySize : (i + 1) * step;

            Runnable worker = new PartArraySumRunnable(start, end, array);
            threads[i] = new Thread(worker);
        }


        for (Thread thread : threads) {
            thread.start();
        }


        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            System.out.println("The stream was interrupted.");
        }

        System.out.println("Sum by threads: " + sumByThreads);
    }

    public static synchronized void addToSum(int value) {
        sumByThreads += value;
    }
}
