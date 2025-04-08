package dao;

import model.Categoria;
import util.BD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaSQL implements CategoriaDAO {
    
    @Override
    public void insertar(Categoria categoria) throws Exception {
        String sql = "INSERT INTO categorias (nombre) VALUES (?)";
        
        try (Connection conn = BD.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre de la categoría no puede estar vacío");
            }
            
            stmt.setString(1, categoria.getNombre().trim());
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    categoria.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                throw new IllegalArgumentException("Ya existe una categoría con ese nombre", e);
            }
            throw e;
        }
    }
    
    @Override
    public void actualizar(Categoria categoria) throws Exception {
        String sql = "UPDATE categorias SET nombre=? WHERE id=?";
        
        try (Connection conn = BD.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (categoria.getId() <= 0) {
                throw new IllegalArgumentException("ID de categoría inválido");
            }
            if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre de la categoría no puede estar vacío");
            }
            
            stmt.setString(1, categoria.getNombre().trim());
            stmt.setInt(2, categoria.getId());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new IllegalArgumentException("No se encontró la categoría con ID: " + categoria.getId());
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                throw new IllegalArgumentException("Ya existe una categoría con ese nombre", e);
            }
            throw e;
        }
    }
    
    @Override
    public void eliminar(int id) throws Exception {
        String checkSql = "SELECT COUNT(*) FROM libros WHERE categoria_id = ?";
        String deleteSql = "DELETE FROM categorias WHERE id=?";
        
        try (Connection conn = BD.getConnection();
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
            
            conn.setAutoCommit(false);
            
            checkStmt.setInt(1, id);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new IllegalStateException("No se puede eliminar la categoría porque tiene libros asociados");
                }
            }
            
            deleteStmt.setInt(1, id);
            int affectedRows = deleteStmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new IllegalArgumentException("No se encontró la categoría con ID: " + id);
            }
            
            conn.commit();
        } catch (SQLException e) {
            throw new Exception("Error al eliminar categoría: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Categoria> obtenerTodas() throws Exception {
        String sql = "SELECT * FROM categorias ORDER BY nombre";
        
        try (Connection conn = BD.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            
            List<Categoria> categorias = new ArrayList<>();
            while (rs.next()) {
                categorias.add(resultSetToCategoria(rs));
            }
            return categorias;
        }
    }
    
    @Override
    public Categoria obtenerPorId(int id) throws Exception {
        String sql = "SELECT * FROM categorias WHERE id=?";
        
        try (Connection conn = BD.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return resultSetToCategoria(rs);
                }
            }
        }
        return null;
    }
    
    @Override
    public Categoria obtenerPorNombre(String nombre) throws Exception {
        String sql = "SELECT * FROM categorias WHERE nombre = ?";
        
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        try (Connection conn = BD.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombre.trim());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return resultSetToCategoria(rs);
                }
            }
        }
        return null;
    }
    
    private Categoria resultSetToCategoria(ResultSet rs) throws SQLException {
        Categoria categoria = new Categoria();
        categoria.setId(rs.getInt("id"));
        categoria.setNombre(rs.getString("nombre"));
        return categoria;
    }
}