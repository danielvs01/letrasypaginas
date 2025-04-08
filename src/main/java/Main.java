import java.sql.SQLException;

import util.BD;
import view.BibliotecaGUI;

public class Main {
    public static void main(String[] args) {
        try {
            BD.getConnection();
            BD.closeConnection();
        } catch (SQLException e) {
            System.err.println("Error al inicializar la base de datos: " + e.getMessage());
            return;
        }

        BibliotecaGUI.main(args);
    }
}