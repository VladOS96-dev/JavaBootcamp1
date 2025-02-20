
public class Program {

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
    public static void main(String[] args)
    {
        System.out.println(sum_digit_in_number(479598));
        System.out.println(sum_digit_in_number(1000));
        System.out.println(sum_digit_in_number(123));
    }
}