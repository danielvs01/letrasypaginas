package dao;

import model.Libro;
import java.util.List;

public interface LibroDAO {
    void insertar(Libro libro) throws Exception;
    void actualizar(Libro libro) throws Exception;
    void eliminar(int id) throws Exception;
    List<Libro> obtenerTodo() throws Exception;
    Libro obtenerPorId(int id) throws Exception;
    List<Libro> buscarPorIsbn(String isbn) throws Exception;
    List<Libro> buscarPorTitulo(String titulo) throws Exception;
    List<Libro> buscarPorAutor(String autor) throws Exception;
}