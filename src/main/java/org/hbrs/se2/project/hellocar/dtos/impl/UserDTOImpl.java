package org.hbrs.se2.project.hellocar.dtos.impl;

import org.hbrs.se2.project.hellocar.dtos.UserDTO;

import java.time.LocalDate;
import java.util.List;

public class UserDTOImpl implements UserDTO {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password; // TODO: make this more secure
    private String dateOfBirth;


    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) { this.email = email; }

    public void setPassword(String password) { this.password = password; }

    public void setFirstname(String firstname) {
        this.firstName = firstname;
    }

    public void setLastname(String lastname) {
        this.lastName = lastname;
    }

    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }



    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getFirstName() {
        return this.firstName;
    }

    @Override
    public String getLastName() {
        return this.lastName;
    }

    @Override
    public String getEmail() { return this.email; }

    @Override
    public String getPassword() { return this.password; }

    @Override
    public LocalDate getDateOfBirth() { return null; }

    @Override
    public String toString() {
        return "UserDTOImpl{" +
                "id=" + id +
                ", firstname='" + firstName + '\'' +
                ", lastname='" + lastName + '\'' +
                '}';
    }
}
