package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class BD {
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static final String DB_NAME = "biblioteca";
    
    private static Connection connection;
    
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            
            crearBaseDeDatosSiNoExiste(conn);
            
            connection = DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD);
            
            crearTablasSiNoExisten();
        }
        return connection;
    }
    
    private static void crearBaseDeDatosSiNoExiste(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
        }
    }
    
    private static void crearTablasSiNoExisten() throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD);
            Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS categorias (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "nombre VARCHAR(50) NOT NULL UNIQUE)");
            
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS libros (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "titulo VARCHAR(100) NOT NULL, " +
                "autor VARCHAR(100) NOT NULL, " +
                "isbn VARCHAR(20) NOT NULL UNIQUE, " +
                "precio DECIMAL(10, 2) NOT NULL, " +
                "stock INT NOT NULL DEFAULT 0, " +
                "categoria_id INT, " +
                "FOREIGN KEY (categoria_id) REFERENCES categorias(id))");
            
            
        }
    }
    
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}