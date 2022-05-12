package org.hbrs.se2.project.hellocar.dtos.impl;

import org.hbrs.se2.project.hellocar.dtos.RolleDTO;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;

import java.time.LocalDate;
import java.util.List;

public class UserDTOImpl implements UserDTO {

    private int id;
    private String firstname;
    private String lastname;
    private String email;
    private String password; // TODO: make this more secure
    private String dateofBirth;
    private List<RolleDTO> roles;

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) { this.email = email; }

    public void setPassword(String password) { this.password = password; }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setDateofBirth(String dateofBirth) { this.dateofBirth = dateofBirth; }

    public void setRoles(List<RolleDTO> roles) {
        this.roles = roles;
    }


    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getFirstName() {
        return this.firstname;
    }

    @Override
    public String getLastName() {
        return this.lastname;
    }

    @Override
    public List<RolleDTO> getRoles() {
        return this.roles;
    }

    @Override
    public String getEmail() { return this.email; }

    @Override
    public String getPassword() { return this.password; }

    @Override
    public LocalDate getDateofBirth() { return null; }

    @Override
    public String toString() {
        return "UserDTOImpl{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", roles=" + roles +
                '}';
    }
}
