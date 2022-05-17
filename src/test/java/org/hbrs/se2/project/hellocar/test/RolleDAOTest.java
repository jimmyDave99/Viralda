package org.hbrs.se2.project.hellocar.test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;

import org.hbrs.se2.project.hellocar.dao.RolleDAO;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.dtos.RolleDTO;
import org.hbrs.se2.project.hellocar.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.hellocar.dtos.impl.RolleDTOImpl;



class RolleDAOTest {

    @Test
    void getRolesOfUser() {
        RolleDAO rolleDAO = new RolleDAO();

        //create new user instance
        UserDTO user = new UserDTOImpl();
        user.setUserId(0);
        user.setFirstName("Hans");
        user.setLastName("Peter");

        List<RolleDTO> roles = rolleDAO.getRolesOfUser(user);

        assertEquals(roles.get(0).getBezeichhnung(), "admin");
    }
}