package ex01;

public class HenThread extends Thread{
    private final int count;

    public HenThread(int count) {
        this.count = count;

    }

    @Override
    public void run() {
        synchronized (Program.class) {
            for (int i = 0; i < count; i++) {
                System.out.println("Hen");
                Program.class.notify();
                try {
                    Program.class.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Program.class.notify();
        }
    }
}
