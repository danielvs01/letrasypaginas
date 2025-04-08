package view;

import model.Libro;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class LibroTableModel extends AbstractTableModel {
    private final String[] columnNames = {"ID", "Título", "Autor", "ISBN", "Precio", "Stock", "Categoría"};
    private List<Libro> libros;
    
    public LibroTableModel(List<Libro> libros) {
        this.libros = libros;
    }
    
    @Override
    public int getRowCount() {
        return libros.size();
    }
    
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
    
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Libro libro = libros.get(rowIndex);
        
        switch (columnIndex) {
            case 0: return libro.getId();
            case 1: return libro.getTitulo();
            case 2: return libro.getAutor();
            case 3: return libro.getIsbn();
            case 4: return libro.getPrecio();
            case 5: return libro.getStock();
            case 6: return libro.getCategoria().getNombre();
            default: return null;
        }
    }
    
    public Libro getLibroAt(int rowIndex) {
        return libros.get(rowIndex);
    }
}