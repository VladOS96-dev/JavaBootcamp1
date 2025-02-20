package edu.school21.models;
import edu.school21.annotations.*;

import java.util.Objects;

@OrmEntity(table = "cars")
public class Car {

    @OrmColumnId
    private Long id;

    @OrmColumn(name = "brand", length = 50)
    private String brand;

    @OrmColumn(name = "model", length = 50)
    private String model;

    @OrmColumn(name = "year")
    private Integer year;

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


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(id, car.id) && Objects.equals(brand, car.brand) && Objects.equals(model, car.model) && Objects.equals(year, car.year);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, brand, model, year);
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                '}';
    }
}
