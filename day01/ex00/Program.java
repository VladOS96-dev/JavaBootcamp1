package ex00;

public class Program {
    public static void main(String[] args) {
        User sergey = new User("Sergey", 15);
        User kirill = new User("Kirill", 150);
        System.out.println(sergey);
        System.out.println(kirill);
        Transaction transaction1 = new Transaction(kirill, sergey, TransferCategory.DEBIT, 5);
        System.out.println(transaction1);
        Transaction transaction2 = new Transaction(sergey, kirill, TransferCategory.CREDIT, -10);
        System.out.println(transaction2 + "\n");

        System.out.println(sergey);
        System.out.println(kirill + "\n");

        User dima = new User("Dima", -3);
        System.out.println("User with wrong balance:\n" + dima + "\n");
        User semen = new User("TIME", 0);
        Transaction transaction3 = new Transaction(sergey, semen, TransferCategory.DEBIT, 30);
        System.out.println("Wrong transaction:\n" + transaction3);
        System.out.println(semen);
    }
}
