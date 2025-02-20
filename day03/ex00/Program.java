package ex00;

public class Program {
    public static void main(String [] args)
    {
        if (args.length != 1 || !args[0].startsWith("--count=")) {
            System.out.println("Usage: java Program --count=<number>");
            return;
        }

        int count;
        try {
            count = Integer.parseInt(args[0].substring("--count=".length()));
        } catch (NumberFormatException e) {
            System.out.println("Error: <number> must be an integer.");
            return;
        }

        try {
            EggThread eggThread = new EggThread(count);
            HenThread henThread = new HenThread(count);


            eggThread.start();
            henThread.start();
            eggThread.join();
            henThread.join();
        } catch (InterruptedException e) {
            System.out.println("The stream was interrupted.");
            return;
        }


        for (int i = 0; i < count; i++) {
            System.out.println("Human");
        }
    }
}
