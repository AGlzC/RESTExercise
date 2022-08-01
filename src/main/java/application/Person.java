package application;

import java.util.UUID;

public class Person implements Comparable<Person>{

    private String id;
    private String firstMame;
    private String lastName;
    private String email;
    private String gender;

    public Person() {
        //id = UUID.randomUUID();
    }

    public String getId() {
        return id;
    }

    public Person(String guid, String firstMame, String lastName, String email, String gender) {
        //id = UUID.randomUUID();
        this.id = guid;
        this.firstMame = firstMame;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
    }

    public String toInsert()
    {
        return "INSERT INTO PERSON VALUES('" + id + "','" + firstMame + "','" + lastName + "','" + email + "','" + gender +"')";
    }

    public String toUpdate()
    {
        return "UPDATE PERSON SET firstname='" + firstMame + "',lastname='" + lastName + "',email='" + email + "',gender='" + gender +"' WHERE id='" + id + "';";
    }

    public String getFirstMame() {
        return firstMame;
    }

    public void setFirstMame(String firstMame) {
        this.firstMame = firstMame;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public int compareTo(Person o) {
        return this.id.compareTo(o.id);
    }
}
