import org.junit.jupiter.api.Test;
import activeRecord.DBConnection;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test1 {

    @Test
    public void testConnexion(){

        Connection connection1 = DBConnection.getConnection();
        Connection connection2 = DBConnection.getConnection();

        //Verifie que les deux connexions sont les mêmes
        assertEquals(connection1, connection2);

        DBConnection.setNomDB("testpersonne2");

        Connection connection3 = DBConnection.getConnection();

        assertEquals("testpersonne2", DBConnection.getDbName());
    }
}
