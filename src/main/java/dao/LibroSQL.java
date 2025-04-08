package dao;

import model.Libro;
import model.Categoria;
import util.BD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroSQL implements LibroDAO {
    
    @Override
    public void insertar(Libro libro) throws Exception {
        String sql = "INSERT INTO libros (titulo, autor, isbn, precio, stock, categoria_id) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = BD.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, libro.getTitulo());
            stmt.setString(2, libro.getAutor());
            stmt.setString(3, libro.getIsbn());
            stmt.setDouble(4, libro.getPrecio());
            stmt.setInt(5, libro.getStock());
            stmt.setInt(6, libro.getCategoria().getId());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    libro.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                throw new IllegalArgumentException("El ISBN ya existe", e);
            }
            throw e;
        }
    }
    
    @Override
    public void actualizar(Libro libro) throws Exception {
        String sql = "UPDATE libros SET titulo=?, autor=?, isbn=?, precio=?, stock=?, categoria_id=? WHERE id=?";
        
        try (Connection conn = BD.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, libro.getTitulo());
            stmt.setString(2, libro.getAutor());
            stmt.setString(3, libro.getIsbn());
            stmt.setDouble(4, libro.getPrecio());
            stmt.setInt(5, libro.getStock());
            stmt.setInt(6, libro.getCategoria().getId());
            stmt.setInt(7, libro.getId());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new IllegalArgumentException("No se encontró el libro con ID: " + libro.getId());
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                throw new IllegalArgumentException("El ISBN ya existe", e);
            }
            throw e;
        }
    }
    
    @Override
    public void eliminar(int id) throws Exception {
        String sql = "DELETE FROM libros WHERE id=?";
        
        try (Connection conn = BD.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new IllegalArgumentException("No se encontró el libro con ID: " + id);
            }
        }
    }
    
    @Override
    public List<Libro> obtenerTodo() throws Exception {
        String sql = "SELECT l.*, c.nombre as categoria_nombre FROM libros l LEFT JOIN categorias c ON l.categoria_id = c.id";
        
        try (Connection conn = BD.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            
            List<Libro> libros = new ArrayList<>();
            while (rs.next()) {
                libros.add(resultSetToLibro(rs));
            }
            return libros;
        }
    }
    
    @Override
    public Libro obtenerPorId(int id) throws Exception {
        String sql = "SELECT l.*, c.nombre as categoria_nombre FROM libros l LEFT JOIN categorias c ON l.categoria_id = c.id WHERE l.id=?";
        
        try (Connection conn = BD.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return resultSetToLibro(rs);
                }
            }
        }
        return null;
    }
    
    @Override
    public List<Libro> buscarPorIsbn(String isbn) throws Exception {
        String sql = "SELECT l.*, c.nombre as categoria_nombre FROM libros l LEFT JOIN categorias c ON l.categoria_id = c.id WHERE l.isbn LIKE ?";
        return buscarLibros(sql, "%" + isbn + "%");
    }
    
    @Override
    public List<Libro> buscarPorTitulo(String titulo) throws Exception {
        String sql = "SELECT l.*, c.nombre as categoria_nombre FROM libros l LEFT JOIN categorias c ON l.categoria_id = c.id WHERE l.titulo LIKE ?";
        return buscarLibros(sql, "%" + titulo + "%");
    }
    
    @Override
    public List<Libro> buscarPorAutor(String autor) throws Exception {
        String sql = "SELECT l.*, c.nombre as categoria_nombre FROM libros l LEFT JOIN categorias c ON l.categoria_id = c.id WHERE l.autor LIKE ?";
        return buscarLibros(sql, "%" + autor + "%");
    }
    
    private List<Libro> buscarLibros(String sql, String parametro) throws SQLException {
        try (Connection conn = BD.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, parametro);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Libro> libros = new ArrayList<>();
                while (rs.next()) {
                    libros.add(resultSetToLibro(rs));
                }
                return libros;
            }
        }
    }
    
    private Libro resultSetToLibro(ResultSet rs) throws SQLException {
        Libro libro = new Libro();
        libro.setId(rs.getInt("id"));
        libro.setTitulo(rs.getString("titulo"));
        libro.setAutor(rs.getString("autor"));
        libro.setIsbn(rs.getString("isbn"));
        libro.setPrecio(rs.getDouble("precio"));
        libro.setStock(rs.getInt("stock"));
        
        Categoria categoria = new Categoria();
        categoria.setId(rs.getInt("categoria_id"));
        categoria.setNombre(rs.getString("categoria_nombre"));
        libro.setCategoria(categoria);
        
        return libro;
    }
}