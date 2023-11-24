package activeRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static Connection connection;

    private static final String userName = "root";

    private static final String password = "";
    private static final String serverName = "localhost";

    private static final String portNumber = "3306";

    private static String dbName = "testpersonne";

    private DBConnection() {
    }

    public static synchronized Connection getConnection() {

        String nomDB = null;
        if (connection != null) {

            try {
                nomDB = connection.getCatalog();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        if (connection == null || !dbName.equals(nomDB)) {
            // creation de la connection
            Properties connectionProps = new Properties();
            connectionProps.put("user", userName);
            connectionProps.put("password", password);
            String urlDB = "jdbc:mysql://" + serverName + ":";
            urlDB += portNumber + "/" + dbName;
            try {
                connection = DriverManager.getConnection(urlDB, connectionProps);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return connection;

    }

    /**
     * permet de changer le nom de la base
     * demandée. Après chaque appel de setNomDB, une nouvelle connexion devra être créée à l'appel
     * de getConnection
     * @param nomDB
     */
    public static void setNomDB(String nomDB) {

        dbName = nomDB;
    }


    public static String getDbName() {
        try {
            return connection.getCatalog();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
