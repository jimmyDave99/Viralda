package org.hbrs.se2.project.hellocar.control.factories;

import org.hbrs.se2.project.hellocar.dtos.CarDTO;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.entities.Car;
import org.hbrs.se2.project.hellocar.entities.User;

public class UserFactory {
    public static User createUser(UserDTO userDTO) {
        // Erzeuge ein User-Entity; die ID wird intern hochgezählt (@GeneratedValue auf ID)
        User user = new User();
        // ID könnte man ggf. noch mal anpassen: car.setID( xx );

        // Übernehme die Grundparameter aus dem DTO, also den Werten, die in der UI eingegeben wurden:
        user.setEmail( userDTO.getEmail() );
        user.setFirstName( userDTO.getFirstName() );
        user.setLastName( userDTO.getLastName() );
        user.setPassword( userDTO.getPassword() );

        // und zurück das gute Stück:
        return user;
    }
}
