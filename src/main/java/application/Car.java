package application;

import org.json.JSONObject;

public class Car implements Comparable<Car>{
    private String vin;
    private String brand;
    private String model;
    private Integer year;
    private String color;
    private String personId;

    public Car(String vin, String personId, String brand, String model, Integer year, String color) {
        this.vin = vin;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.color = color;
        this.personId = personId;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String toInsert()
    {
        return "INSERT INTO CAR VALUES('" + vin + "','', '" + brand + "','" + model + "','" + year + "','" + color +"')";
    }

    public String toUpdate(boolean person)
    {
        if (person){
            return "UPDATE CAR SET personId='" + personId + "', brand='" + brand + "',model='" + model + "',year='" + year + "',color='" + color + "' WHERE vin='" + vin + "';";
        } else {
            return "UPDATE CAR SET personId='', brand='" + brand + "',model='" + model + "',year='" + year + "',color='" + color + "' WHERE vin='" + vin + "';";
        }
    }


    @Override
    public int compareTo(Car o) {
        return this.vin.compareTo(o.vin);
    }
}
