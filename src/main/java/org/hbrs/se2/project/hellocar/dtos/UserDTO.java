package org.hbrs.se2.project.hellocar.dtos;

import java.time.LocalDate;
import java.util.List;

public interface UserDTO {
    public int getId();
    public String getFirstName();
    public String getLastName();
    public String getEmail();
    public String getPassword();
    public LocalDate getDateOfBirth();
}
