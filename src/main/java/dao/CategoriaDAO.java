package dao;

import model.Categoria;
import java.util.List;

public interface CategoriaDAO {
    void insertar(Categoria categoria) throws Exception;
    void actualizar(Categoria categoria) throws Exception;
    void eliminar(int id) throws Exception;
    List<Categoria> obtenerTodas() throws Exception;
    Categoria obtenerPorId(int id) throws Exception;
    Categoria obtenerPorNombre(String nombre) throws Exception;
}