package model;

import java.util.Objects;
import java.util.regex.Pattern;

public class Customer {
    private String firstName;
    private String lastName;
    private final String email;

    private static final String EMAIL_REGEX = "^(.+)@(.+)\\.(.+)$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public Customer(String firstName, String lastName, String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "First Name: " + firstName + ", Last Name: " + lastName + ", Email: " + email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(email, customer.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
