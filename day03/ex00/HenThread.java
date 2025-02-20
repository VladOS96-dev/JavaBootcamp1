package ex00;

public class HenThread extends Thread{
    private final int count;

    public HenThread(int count) {
        this.count = count;
    }

    @Override
    public void run() {
        for (int i = 0; i < count; i++) {
            System.out.println("Hen");
        }
    }
}
