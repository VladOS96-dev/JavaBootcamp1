import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class Program {

  public static final int max_size_array = 65535;
  public static final int top_size = 10;

  public static short[] calculation_simvol(String line) {
    short[] tmp = new short[max_size_array];
    for (char ch : line.toCharArray()) {
      tmp[ch]++;
    }
    return tmp;
  }
  public static char[] topRating(short[] base) {
    char[] tmp = new char[top_size];
    short[] counts = new short[top_size];

    for (int i = 0; i < top_size; i++) {
      counts[i] = -1;
    }
    for (int i = 0; i < base.length; i++) {

      for (int j = 0; j < top_size; j++) {

        if (base[i] > counts[j]) {

          for (int k = top_size - 1; k > j; k--) {
            counts[k] = counts[k - 1];
            tmp[k] = tmp[k - 1];
          }

          counts[j] = base[i];
          tmp[j] = (char)i;
          break;
        }
      }
    }

    return tmp;
  }
  public static void print_gistogram(short[] count_simvols, char[] simvols) {
    int[] value_gistogram = new int[10];
    int max_value = count_simvols[simvols[0]];
    int min_value = 0;
    for (int i = 0; i < 10; i++) {
      value_gistogram[i] = (int)((count_simvols[simvols[i]] * 10) / max_value);
    }
    int index = 0;
    for (int i = 0; i <= top_size; i++) {
      for (int j = 0; j < top_size; j++) {
        if (top_size - i == value_gistogram[j]) {
          System.out.print(String.format("%3d", count_simvols[simvols[j]]));
        } else if (top_size - i < value_gistogram[j]) {
          System.out.print(String.format("%3c", '#'));
        }
      }
      System.out.println();
    }
    for (int i = 0; i < simvols.length; i++) {
      System.out.print(String.format("%3c", simvols[i]));
    }
    System.out.println();
  }
  public static void main(String[] args) {
    short[] all_simvols_count = new short[max_size_array];

    try (Scanner scanner = new Scanner(System.in)) {

      String line = scanner.nextLine();
      all_simvols_count = calculation_simvol(line);
      char[] simvols = topRating(all_simvols_count);
      print_gistogram(all_simvols_count, simvols);

      scanner.close();
    }
    System.exit(0);
  }
}