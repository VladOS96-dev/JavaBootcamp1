import java.util.Scanner;

public class Program {
    public static boolean is_simple_number(int number)
    {

        boolean is_simple=true;
        for (int i = 2; i <= Math.sqrt(number)&&is_simple; i++) {
            if (number % i == 0) {
                is_simple = false;
                
            }
        }
        return is_simple;
    }
    public static int sum_digit_in_number(int number)
    {
        int sum=0,r;
        while (number!=0) {
            r=number%10;
            number/=10;
            sum+=r;
        }
        return sum;
    }
    public static void main(String[] args) {
        int number = 0;
        int count_simple=0;
        int sum;
        try (Scanner scanner = new Scanner(System.in)) {
           do{
            number = scanner.nextInt();
            
                sum= sum_digit_in_number(number);
                if(is_simple_number(sum))
                {
                    count_simple++;
                }
            
           }while(number!=42); 
           scanner.close();
            System.out.println("Count of coffee-request – "+count_simple);
        }
        System.exit(0);
    }
}