package edu.school21.reflection.app;
import java.util.Scanner;
public class Program {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ReflectionHelper helper = new ReflectionHelper(scanner);

        System.out.println("Classes:\n  - User\n  - Car");
        System.out.println("---------------------\nEnter class name:");
        String className = scanner.nextLine();
        try {
            Class<?> clazz = Class.forName("edu.school21.reflection.classes." + className);
            helper.printClassInfo(clazz);
            Object obj = helper.createObject(clazz);
            System.out.println("Object created: " + obj);

            obj = helper.modifyField(clazz, obj);
            System.out.println("Object updated: " + obj);

            helper.callMethod(clazz, obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
