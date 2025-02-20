package ex02;

public class PartArraySumRunnable implements Runnable {
    private final int beginArray;
    private final int endArray;
    private final int[] array;
    private int sum = 0;

    public PartArraySumRunnable(int beginArray, int endArray, int[] array) {
        this.beginArray = beginArray;
        this.endArray = endArray;
        this.array = array;
    }

    @Override
    public void run() {
        for (int i = beginArray; i < endArray; i++) {
            sum += array[i];
        }

        System.out.printf("%s: from %d to %d sum is %d%n",
                Thread.currentThread().getName(),
                beginArray,
                endArray,
                sum);
        Program.addToSum(sum);
    }
}
