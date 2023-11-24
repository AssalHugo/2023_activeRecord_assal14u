package activeRecord;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Film {

    /**
     * titre du film
     */
    private String titre;

    /**
     * id du film
     */
    private int id;

    /**
     * attribut int id_real correspondant à l'id du réalisateur
     */
    private int id_real;

    /**
     * constructeur qui construit à partir du titre et du réalisateur
     */
    public Film(String titre, Personne realisateur) {
        this.titre = titre;
        this.id = -1;
        this.id_real = realisateur.getId();
    }

    /**
     * constructeur privé qui construit un Film en prenant en paramètre des ids et un titre.
     */
    private Film(int id, String titre, int id_real) {
        this.id = id;
        this.titre = titre;
        this.id_real = id_real;
    }

    /**
     * retourne l'objet Film correspondant au tuple avec l'id passé
     * en paramètre
     */
    public static Film findById(int id) throws SQLException {

        String SQLPrep = "SELECT * FROM Film WHERE id = ?;";
        PreparedStatement prep1 = DBConnection.getConnection().prepareStatement(SQLPrep);
        prep1.setInt(1, id);
        prep1.execute();
        ResultSet rs = prep1.getResultSet();

        // s'il y a un resultat
        if (rs.next()) {
            String titre = rs.getString("titre");
            int id_real = rs.getInt("id_rea");
            return new Film(id, titre, id_real);
        }

        return null;
    }

    /**
     * méthode getRealisateur() qui retourne l'objet Personne correspondant au
     * réalisateur du film (en utilisant l'active record Personne et l'attribut id_real)
     */
    public Personne getRealisateur() throws SQLException {
        return Personne.findById(id_real);
    }

    /**
     *  méthode findByRealisateur(Personne p) qui retourne l'ensemble des films
     * réalisé par la personne passée en paramètre
     */
    public static ArrayList<Film> findByRealisateur(Personne p) throws SQLException {

        ArrayList<Film> films = new ArrayList<>();

        String SQLPrep = "SELECT * FROM Film WHERE id_rea = ?;";
        PreparedStatement prep1 = DBConnection.getConnection().prepareStatement(SQLPrep);
        prep1.setInt(1, p.getId());
        prep1.execute();
        ResultSet rs = prep1.getResultSet();

        // s'il y a un resultat
        while (rs.next()) {
            String titre = rs.getString("titre");
            int id = rs.getInt("id");
            int id_real = rs.getInt("id_rea");
            Film f = new Film(id, titre, id_real);
            films.add(f);
        }

        return films;
    }

    /**
     * Méthode qui permet de créer la table Film dans la base de données testpersonne
     */
    public static void createTable() throws SQLException {

        String createString = "CREATE TABLE film ( " + "ID int(11)  AUTO_INCREMENT, "
                + "titre varchar(40) NOT NULL, " + "id_rea int(11) NOT NULL, " + "PRIMARY KEY (ID))";
        Statement stmt = DBConnection.getConnection().createStatement();
        stmt.executeUpdate(createString);
    }

    /**
     * Méthode qui permet de supprimer la table Personne dans la base de données testpersonne
     */
    public static void deleteTable() throws SQLException {

        String drop = "DROP TABLE film";
        Statement stmt = DBConnection.getConnection().createStatement();
        stmt.executeUpdate(drop);
    }

    /**
     * delete dans la classe Film chargée de supprimer la personne sur
     * laquelle la méthode est appelée. L'attribut id de cette personne est ensuite remis à -1 puisqu'elle
     * n'est plus présente dans la table
     */
    public void delete() throws SQLException {

        String SQLPrep = "DELETE FROM film WHERE id=?;";
        PreparedStatement prep1 = DBConnection.getConnection().prepareStatement(SQLPrep);
        prep1.setInt(1, this.id);
        prep1.execute();
        this.id = -1;
    }

    /**
     * méthode saveNew pour ajouter dans la table film
     */
    private void saveNew() throws SQLException, RealisateurAbsentException {


        String SQLPrep = "INSERT INTO film (titre, id_rea) VALUES (?,?);";
        PreparedStatement prep = DBConnection.getConnection().prepareStatement(SQLPrep, Statement.RETURN_GENERATED_KEYS);
        prep.setString(1, this.titre);
        prep.setInt(2, this.id_real);
        prep.executeUpdate();

        // recuperation de la derniere ligne ajoutee (auto increment)
        // recupere le nouvel id
        ResultSet rs = prep.getGeneratedKeys();
        if (rs.next()) {

            this.id = rs.getInt(1);
        }

        //si la clef étrangère id_real est égale à -1,
        //les méthodes doivent lever une exception RealisateurAbsentException
        if (this.id_real == -1) {
            throw new RealisateurAbsentException();
        }
    }

    /**
     * méthode update pour mettre à jour le tuple existant
     */
    private void update() throws SQLException, RealisateurAbsentException {

        //si la clef étrangère id_real est égale à -1,
        //les méthodes doivent lever une exception RealisateurAbsentException
        if (this.id_real == -1) {
            throw new RealisateurAbsentException();
        }

        String SQLPrep = "UPDATE film SET titre=?, id_rea=? WHERE id=?;";
        PreparedStatement prep = DBConnection.getConnection().prepareStatement(SQLPrep);
        prep.setString(1, this.titre);
        prep.setInt(2, this.id_real);
        prep.setInt(3, this.id);
        prep.executeUpdate();
    }

    /**
     * méthode save qui permet de sauvegarder le tuple correspondant à l'objet
     * film this dans la base de données. Si l'objet film n'est pas encore présent dans la base de données,
     * la méthode saveNew est appelée, sinon la méthode update est appelée.
     */
    public void save() throws SQLException {

        if (this.id == -1) {
            try {
                this.saveNew();
            } catch (RealisateurAbsentException e) {
                e.printStackTrace();
            }
        } else {
            try {
                this.update();
            } catch (RealisateurAbsentException e) {
                e.printStackTrace();
            }
        }
    }

    public String getTitre() {
        return titre;
    }

    public int getId() {
        return id;
    }

    public int getId_real() {
        return id_real;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    @Override
    public String toString() {
        return "Film{" +
                "titre='" + titre + '\'' +
                ", id=" + id +
                ", id_real=" + id_real +
                '}';
    }
}
