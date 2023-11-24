package activeRecord;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Personne {


    /**
     * id de la personne
     */
    private int id;

    /**
     * nom de la personne
     */
    private String nom;

    /**
     * prenom de la personne
     */
    private String prenom;

    /**
     * constructeur qui construit à partir du nom et du prenom
     */
    public Personne(String nom, String prenom) {

        this.id = -1;
        this.nom = nom;
        this.prenom = prenom;
    }

    /**
     * méthode findAll chargée de retourner l'ensemble des tuples de la table personne
     * sous la forme d'objets
     */
    public static ArrayList<Personne> findAll() throws SQLException {

        ArrayList<Personne> personnes = new ArrayList<>();

        String SQLPrep = "SELECT * FROM Personne;";
        PreparedStatement prep1 = DBConnection.getConnection().prepareStatement(SQLPrep);
        prep1.execute();
        ResultSet rs = prep1.getResultSet();

        // s'il y a un resultat
        while (rs.next()) {
            String nom = rs.getString("nom");
            String prenom = rs.getString("prenom");
            int id2 = rs.getInt("id");
            Personne p = new Personne(nom, prenom);
            p.id = id2;
            personnes.add(p);
        }

        return personnes;
    }

    /**
     *  retourne l'objet Personne correspondant au tuple avec l'id
     * passé en paramètre (null si l'objet n'existe pas)
     */
    public static Personne findById(int id) throws SQLException {

        String SQLPrep = "SELECT * FROM Personne WHERE id=?;";
        PreparedStatement prep1 = DBConnection.getConnection().prepareStatement(SQLPrep);
        prep1.setInt(1, id);
        prep1.execute();
        ResultSet rs = prep1.getResultSet();

        // s'il y a un resultat
        if (rs.next()) {
            String nom = rs.getString("nom");
            String prenom = rs.getString("prenom");
            int id2 = rs.getInt("id");
            Personne p = new Personne(nom, prenom);
            p.id = id2;
            return p;
        }

        return null;
    }

    /**
     *  retourne la liste des objets Personne correspondant aux
     * tuples dont le nom est passé en paramètre
     */
    public static ArrayList<Personne> findByName(String nom) throws SQLException {

        ArrayList<Personne> personnes = new ArrayList<>();

        String SQLPrep = "SELECT * FROM Personne WHERE nom=?;";
        PreparedStatement prep1 = DBConnection.getConnection().prepareStatement(SQLPrep);
        prep1.setString(1, nom);
        prep1.execute();
        ResultSet rs = prep1.getResultSet();

        // s'il y a un resultat
        while (rs.next()) {
            String prenom = rs.getString("prenom");
            int id2 = rs.getInt("id");
            Personne p = new Personne(nom, prenom);
            p.id = id2;
            personnes.add(p);
        }

        return personnes;
    }

    /**
     * Méthode qui permet de créer la table Personne dans la base de données testpersonne
     */
    public static void createTable() throws SQLException {

        String createString = "CREATE TABLE personne ( " + "ID INTEGER  AUTO_INCREMENT, "
                + "NOM varchar(40) NOT NULL, " + "PRENOM varchar(40) NOT NULL, " + "PRIMARY KEY (ID))";
        Statement stmt = DBConnection.getConnection().createStatement();
        stmt.executeUpdate(createString);
    }

    /**
     * Méthode qui permet de supprimer la table Personne dans la base de données testpersonne
     */
    public static void deleteTable() throws SQLException {

        String drop = "DROP TABLE Personne";
        Statement stmt = DBConnection.getConnection().createStatement();
        stmt.executeUpdate(drop);
    }

    /**
     * delete dans la classe Personne chargée de supprimer la personne sur
     * laquelle la méthode est appelée. L'attribut id de cette personne est ensuite remis à -1 puisqu'elle
     * n'est plus présente dans la table
     */
    public void delete() throws SQLException {

        String SQLPrep = "DELETE FROM Personne WHERE id=?;";
        PreparedStatement prep1 = DBConnection.getConnection().prepareStatement(SQLPrep);
        prep1.setInt(1, this.id);
        prep1.execute();
        this.id = -1;
    }

    /**
     * méthode saveNew pour ajouter dans la table
     */
    private void saveNew() throws SQLException {

        String SQLPrep = "INSERT INTO Personne (nom, prenom) VALUES (?,?)";
        PreparedStatement prep = DBConnection.getConnection().prepareStatement(SQLPrep, Statement.RETURN_GENERATED_KEYS);
        prep.setString(1, this.nom);
        prep.setString(2, this.prenom);
        prep.executeUpdate();

        // recuperation de la derniere ligne ajoutee (auto increment)
        // recupere le nouvel id
        ResultSet rs = prep.getGeneratedKeys();
        if (rs.next()) {
            this.id = rs.getInt(1);
        }
    }

    /**
     * méthode update pour mettre à jour le tuple existant
     */
    private void update() throws SQLException {

        String SQLPrep = "UPDATE Personne SET nom=?, prenom=? WHERE id=?";
        PreparedStatement prep = DBConnection.getConnection().prepareStatement(SQLPrep);
        prep.setString(1, this.nom);
        prep.setString(2, this.prenom);
        prep.setInt(3, this.id);
        prep.executeUpdate();
    }

    /**
     * Écrire la méthode save qui sauve l'objet Personne dans la table. Deux cas peuvent se
     * produire :
     *  si l'attribut id de l'objet Personne est de -1, elle insère l'objet dans la table personne et met à
     * jour l'attribut id de l'objet avec l'indice crée par la table grâce à l'auto-incrément.
     * Si l'attribut id de la personne est différent de -1, c'est que l'objet correspond à un tuple existant.
     * La méthode save met simplement à jour les champs de ce tuple.
     */
    public void save() throws SQLException {

        if (this.id == -1) {
            this.saveNew();
        } else {
            this.update();
        }
    }

    /**
     * ToString de la classe Personne qui affiche l'id, le nom et le prenom
     */
    @Override
    public String toString() {
        return "Personne{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                '}';
    }


    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
}
