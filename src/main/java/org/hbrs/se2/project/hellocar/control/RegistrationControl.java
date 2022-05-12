package org.hbrs.se2.project.hellocar.control;

import org.hbrs.se2.project.hellocar.control.factories.CarFactory;
import org.hbrs.se2.project.hellocar.control.factories.UserFactory;
import org.hbrs.se2.project.hellocar.dtos.CarDTO;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.entities.Car;
import org.hbrs.se2.project.hellocar.entities.User;
import org.hbrs.se2.project.hellocar.repository.CarRepository;
import org.hbrs.se2.project.hellocar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RegistrationControl {

    @Autowired
    private UserRepository repository;


    public void createUser( UserDTO userDTO ) {
        // Hier könnte man noch die Gültigkeit der Daten überprüfen
        // check( carDTO );

        //Erzeuge ein neues Car-Entity konsistent über eine Factory
        User userEntity = UserFactory.createUser(  userDTO  );

        // Abspeicherung des Entity in die DB
        this.repository.save( userEntity );
    }

}
