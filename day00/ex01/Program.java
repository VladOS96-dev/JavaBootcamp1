import java.util.Scanner;

public class Program {
    public static int count_step_simple_number(int number)
    {
        int count =0;
        boolean is_simple=true;
        for (int i = 2; i <= Math.sqrt(number)&&is_simple; i++) {
            count++;
            if (number % i == 0) {
                is_simple = false;
                
            }
        }
        return count;
    }
    public static void main(String[] args) {
        int number = 0;
        try (Scanner scanner = new Scanner(System.in)) {
            number = scanner.nextInt();
            scanner.close();
            boolean flag = true;
            if (number<=1) {
                System.err.println("Illegal Argument");
                System.exit(-1);
            }
            System.out.println(count_step_simple_number(number));
        }
        System.exit(0);
    }
}
