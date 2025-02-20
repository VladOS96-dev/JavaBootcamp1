package edu.school21.reflection.app;



import java.lang.reflect.*;
import java.util.Scanner;



class ReflectionHelper {
    private final Scanner scanner;

    public ReflectionHelper(Scanner scanner) {
        this.scanner = scanner;
    }

    public void printClassInfo(Class<?> clazz) {
        System.out.println("---------------------\nfields:");
        for (Field field : clazz.getDeclaredFields()) {
            System.out.println("  " + field.getType().getSimpleName() + " " + field.getName());
        }

        System.out.println("methods:");
        for (Method method : clazz.getDeclaredMethods()) {
            System.out.print("  " + method.getReturnType().getSimpleName() + " " + method.getName() + "(");
            Class<?>[] params = method.getParameterTypes();
            for (int i = 0; i < params.length; i++) {
                System.out.print(params[i].getSimpleName());
                if (i < params.length - 1) System.out.print(", ");
            }
            System.out.println(")");
        }
    }

    public Object createObject(Class<?> clazz) throws Exception {
        System.out.println("---------------------\nLet’s create an object.");
        Constructor<?> constructor = clazz.getConstructor();
        Object obj = constructor.newInstance();

        for (Field field : clazz.getDeclaredFields()) {
            System.out.println(field.getName() + ":");
            String value = scanner.nextLine();
            field.setAccessible(true);
            field.set(obj, parseValue(value, field.getType()));
        }
        return obj;
    }

    public Object modifyField(Class<?> clazz, Object obj) throws Exception {
        System.out.println("---------------------\nEnter name of the field for changing:");
        String fieldName = scanner.nextLine();
        Field fieldToModify = clazz.getDeclaredField(fieldName);
        fieldToModify.setAccessible(true);

        System.out.println("Enter " + fieldToModify.getType().getSimpleName() + " value:");
        String newValue = scanner.nextLine();
        fieldToModify.set(obj, parseValue(newValue, fieldToModify.getType()));
        return obj;
    }

    public void callMethod(Class<?> clazz, Object obj) throws Exception {
        System.out.println("---------------------\nEnter name of the method for call:");
        String methodName = scanner.nextLine();

        Method methodToCall = clazz.getDeclaredMethod(methodName, int.class);

        System.out.println("Enter int value:");
        int paramValue = Integer.parseInt(scanner.nextLine());
        Object result = methodToCall.invoke(obj, paramValue);

        if (methodToCall.getReturnType() != void.class) {
            System.out.println("Method returned:\n" + result);
        }
    }

    private Object parseValue(String value, Class<?> type) {
        if (type == int.class) return Integer.parseInt(value);
        if (type == double.class) return Double.parseDouble(value);
        if (type == long.class) return Long.parseLong(value);
        if (type == boolean.class) return Boolean.parseBoolean(value);
        return value;
    }
}