package ex00;

public class EggThread extends Thread{
    private final int count;

    public EggThread(int count) {
        this.count = count;
    }

    @Override
    public void run() {
        for (int i = 0; i < count; i++) {
            System.out.println("Egg");
        }
    }
}
