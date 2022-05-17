package org.hbrs.se2.project.hellocar.control;

import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class RegistrationControl {

    public void createUser( UserDTO userDTO ) {
        // Hier könnte man noch die Gültigkeit der Daten überprüfen
        // check( carDTO );

        //Erzeuge ein neues Car-Entity konsistent über eine Factory
        ;

        // Abspeicherung des Entity in die DB
        ;
    }

    private boolean check(UserDTO userDTO){
        return true;
    }

}
