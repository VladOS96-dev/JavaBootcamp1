package edu.school21.reflection.classes;

import java.util.StringJoiner;

public class Car {
    private String brand;
    private String model;
    private int year;

    public Car() {
        this.brand = "Default Brand";
        this.model = "Default Model";
        this.year = 2000;
    }

    public Car(String brand, String model, int year) {
        this.brand = brand;
        this.model = model;
        this.year = year;
    }

    public String upgradeModel(String newModel) {
        this.model = newModel;
        return model;
    }

    public void updateYear(int newYear) {
        this.year = newYear;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Car.class.getSimpleName() + "[", "]")
                .add("brand='" + brand + "'")
                .add("model='" + model + "'")
                .add("year=" + year)
                .toString();
    }
}