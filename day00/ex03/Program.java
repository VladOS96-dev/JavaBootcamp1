import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class Program {

  public static int parse_rating(String line) {
    int min = 10, num1;
    boolean err = false;
    String[] mass = line.split(" ");

    if (mass.length == 5) {
      for (String num : mass) {
        if (num.matches("[1-9]")) {
          num1 = Integer.parseInt(num);
          if (num1 < min) {
            min = num1;
          }
        }
      }
    }
    return min;
  }
  public static int digit_in_number(int number) {
    int sum = 0, r;
    while (number != 0) {
      r = number % 10;
      number /= 10;
      sum += r;
    }
    return sum;
  }
  public static int get_week(String line) {
    int week = 0;
    String[] tmp = line.split(" ");
    if (tmp.length == 2) {
      try {
        week = Integer.parseInt(tmp[tmp.length - 1]);

      } catch (Exception e) {
      }
    } else {
      week = -1;
    }

    return week;
  }
  public static void draw_line(int length) {
    for (int i = 0; i < length; i++) {
      System.out.print("=");
    }
    System.out.println(">");
  }
  public static long reverse_number(long all_rating) {
    long reverse = 0;
    int r;
    while (all_rating != 0) {
      r = (int)all_rating % 10;
      all_rating /= 10;
      reverse *= 10;
      reverse += r;
    }
    return reverse;
  }
  public static void draw_graph(int weeks, long all_rating) {
    for (int i = 1; i < weeks; i++) {
      System.out.print("Week " + i);
      int r;

      r = (int)all_rating % 10;
      draw_line(r);
      all_rating /= 10;
    }
  }
  public static void main(String[] args) {
    ArrayList<String> namesWeek = new ArrayList<String>();
    String name;
    String numbers;
    int number = 0;
    int count_simple = 0;
    int week, weeks = 1;
    int sum;
    long all_rating = 0;
    try (Scanner scanner = new Scanner(System.in)) {
      do {
        name = scanner.nextLine();
        if (!name.equals("42")) {
          week = get_week(name);
          if (week == -1 || weeks < week) {
            System.err.println("IllegalArgument");
            System.exit(-1);
          } else {
            weeks++;
            numbers = scanner.nextLine();

            all_rating *= 10;
            all_rating += parse_rating(numbers);
          }
        }

      } while (!name.equals("42") && weeks < 18);
      scanner.close();
      all_rating = reverse_number(all_rating);
      draw_graph(weeks, all_rating);
    }
    System.exit(0);
  }
}