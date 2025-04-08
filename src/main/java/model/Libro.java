package model;

public class Libro {
    private int id;
    private String titulo;
    private String autor;
    private String isbn;
    private double precio;
    private int stock;
    private Categoria categoria;

    public Libro() {}
    
    public Libro(String titulo, String autor, String isbn, double precio, int stock, Categoria categoria) {
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    
    @Override
    public String toString() {
        return titulo + " - " + autor + " (" + isbn + ")";
    }
}