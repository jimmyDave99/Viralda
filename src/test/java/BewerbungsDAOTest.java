//import org.hbrs.se2.project.hellocar.dao.BewerbungDAO;
//import org.hbrs.se2.project.hellocar.dtos.UserDTO;
//import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertNotEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//public class BewerbungsDAOTest {
//
//    BewerbungDAO bewerbungDAO ;
//    @BeforeEach
//    public void init(){
//        bewerbungDAO = new BewerbungDAO();
//    }
//    @Test
//    public void bewerbungDAOTest() throws DatabaseLayerException {
//        List<UserDTO> bewerbungs = bewerbungDAO.findAllJobApplicant();
//
//        // list is not null
//        assertNotNull(bewerbungs);
//
//        // list is not empty
//        assertNotEquals(bewerbungs.size(), 0);
//
//        for (UserDTO bewerbung : bewerbungs) {
//            // entry is not null
//            assertNotNull(bewerbung);
//            // entry is not empty
//            assertNotEquals(bewerbung.toString(), "");
//        }
//    }
//
//}
