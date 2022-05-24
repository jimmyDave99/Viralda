package org.hbrs.se2.project.hellocar.control.factories;

import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.dtos.impl.UserDTOImpl;

public class UserFactory {
    public static UserDTOImpl createUser(UserDTO userDTO) {
        // Erzeuge ein User-Entity; die ID wird intern hochgezählt (@GeneratedValue auf ID)
        UserDTOImpl user = new UserDTOImpl();
        // ID könnte man ggf. noch mal anpassen: car.setID( xx );

        // Übernehme die Grundparameter aus dem DTO, also den Werten, die in der UI eingegeben wurden:
        user.setEmail( userDTO.getEmail() );
        user.setFirstName( userDTO.getFirstName() );
        user.setLastName( userDTO.getLastName() );
        user.setCompanyName(userDTO.getCompanyName());
        user.setPassword( userDTO.getPassword() );
        user.setRole( userDTO.getRole() );

        // und zurück das gute Stück:
        return user;
    }
}
