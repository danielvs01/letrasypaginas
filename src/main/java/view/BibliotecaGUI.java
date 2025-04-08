package view;

import dao.CategoriaDAO;
import dao.LibroDAO;
import dao.CategoriaSQL;
import dao.LibroSQL;
import model.Categoria;
import model.Libro;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BibliotecaGUI extends JFrame {
    private LibroDAO libroDAO;
    private CategoriaDAO categoriaDAO;
    private JTable librosTable;
    private JTable categoriasTable;
    private JTabbedPane tabbedPane;
    private JTextField buscarField;
    private JComboBox<String> filtroCombo;

    public BibliotecaGUI() {
        try {
            libroDAO = new LibroSQL();
            categoriaDAO = new CategoriaSQL();
            initComponents();
            cargarLibros();
            cargarCategorias();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al conectar con la base de datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void initComponents() {
        setTitle("Librería Letras y Páginas");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();

        JPanel librosPanel = new JPanel(new BorderLayout());
        configurarPanelLibros(librosPanel);
        tabbedPane.addTab("Libros", librosPanel);

        JPanel categoriasPanel = new JPanel(new BorderLayout());
        configurarPanelCategorias(categoriasPanel);
        tabbedPane.addTab("Categorías", categoriasPanel);

        add(tabbedPane);
    }

    private void configurarPanelLibros(JPanel panel) {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        buscarField = new JTextField(25);
        searchPanel.add(new JLabel("Buscar:"));
        searchPanel.add(buscarField);

        filtroCombo = new JComboBox<>(new String[] { "ISBN", "Título", "Autor" });
        searchPanel.add(filtroCombo);

        JButton buscarButton = new JButton("Buscar");
        buscarButton.addActionListener(e -> buscarLibros());
        searchPanel.add(buscarButton);

        panel.add(searchPanel, BorderLayout.NORTH);

        librosTable = new JTable();
        librosTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(librosTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new BorderLayout());

        JPanel buttonsLeftPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton agregarButton = new JButton("Agregar Libro");
        agregarButton.setPreferredSize(new Dimension(120, 30));
        agregarButton.addActionListener(e -> mostrarDialogoLibro(null));
        buttonsLeftPanel.add(agregarButton);

        JButton editarButton = new JButton("Editar Libro");
        editarButton.setPreferredSize(new Dimension(120, 30));
        editarButton.addActionListener(e -> editarLibroSeleccionado());
        buttonsLeftPanel.add(editarButton);

        JButton eliminarButton = new JButton("Eliminar Libro");
        eliminarButton.setPreferredSize(new Dimension(120, 30));
        eliminarButton.addActionListener(e -> eliminarLibro());
        buttonsLeftPanel.add(eliminarButton);

        buttonPanel.add(buttonsLeftPanel, BorderLayout.WEST);

        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/logo.png"));
            Image scaledLogo = logoIcon.getImage().getScaledInstance(100, 50, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
            logoLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            buttonPanel.add(logoLabel, BorderLayout.EAST);
        } catch (Exception e) {
            System.err.println("Error al cargar el logo: " + e.getMessage());
        }

        panel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void configurarPanelCategorias(JPanel panel) {
        categoriasTable = new JTable();
        categoriasTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(categoriasTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new BorderLayout());

        JPanel buttonsLeftPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton agregarCatButton = new JButton("Agregar Categoría");
        agregarCatButton.setPreferredSize(new Dimension(150, 30));
        agregarCatButton.addActionListener(e -> mostrarDialogoCategoria(null));
        buttonsLeftPanel.add(agregarCatButton);

        JButton editarCatButton = new JButton("Editar Categoría");
        editarCatButton.setPreferredSize(new Dimension(150, 30));
        editarCatButton.addActionListener(e -> editarCategoriaSeleccionada());
        buttonsLeftPanel.add(editarCatButton);

        JButton eliminarCatButton = new JButton("Eliminar Categoría");
        eliminarCatButton.setPreferredSize(new Dimension(150, 30));
        eliminarCatButton.addActionListener(e -> eliminarCategoria());
        buttonsLeftPanel.add(eliminarCatButton);

        buttonPanel.add(buttonsLeftPanel, BorderLayout.WEST);

        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/logo.png"));
            Image scaledLogo = logoIcon.getImage().getScaledInstance(100, 50, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
            logoLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            buttonPanel.add(logoLabel, BorderLayout.EAST);
        } catch (Exception e) {
            System.err.println("Error al cargar el logo: " + e.getMessage());
        }

        panel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void cargarLibros() {
        try {
            List<Libro> libros = libroDAO.obtenerTodo();
            actualizarTablaLibros(libros);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar libros: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarCategorias() {
        try {
            List<Categoria> categorias = categoriaDAO.obtenerTodas();
            actualizarTablaCategorias(categorias);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar categorías: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarLibros() {
        try {
            String texto = buscarField.getText().trim();
            String filtro = (String) filtroCombo.getSelectedItem();
            List<Libro> libros;

            switch (filtro) {
                case "ISBN":
                    libros = libroDAO.buscarPorIsbn(texto);
                    break;
                case "Título":
                    libros = libroDAO.buscarPorTitulo(texto);
                    break;
                case "Autor":
                    libros = libroDAO.buscarPorAutor(texto);
                    break;
                default:
                    libros = libroDAO.obtenerTodo();
            }

            actualizarTablaLibros(libros);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al buscar libros: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarTablaLibros(List<Libro> libros) {
        String[] columnNames = { "ID", "Título", "Autor", "ISBN", "Precio", "Stock", "Categoría" };
        Object[][] data = new Object[libros.size()][7];

        for (int i = 0; i < libros.size(); i++) {
            Libro libro = libros.get(i);
            data[i][0] = libro.getId();
            data[i][1] = libro.getTitulo();
            data[i][2] = libro.getAutor();
            data[i][3] = libro.getIsbn();
            data[i][4] = libro.getPrecio();
            data[i][5] = libro.getStock();
            data[i][6] = libro.getCategoria().getNombre();
        }

        librosTable.setModel(new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }

    private void actualizarTablaCategorias(List<Categoria> categorias) {
        String[] columnNames = { "ID", "Nombre" };
        Object[][] data = new Object[categorias.size()][2];

        for (int i = 0; i < categorias.size(); i++) {
            data[i][0] = categorias.get(i).getId();
            data[i][1] = categorias.get(i).getNombre();
        }

        categoriasTable.setModel(new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }

    private void mostrarDialogoLibro(Libro libro) {
        JDialog dialog = new JDialog(this, libro == null ? "Agregar Libro" : "Editar Libro", true);
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 10, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField tituloField = new JTextField(libro != null ? libro.getTitulo() : "", 20);
        JTextField autorField = new JTextField(libro != null ? libro.getAutor() : "", 20);
        JTextField isbnField = new JTextField(libro != null ? libro.getIsbn() : "", 20);
        JTextField precioField = new JTextField(libro != null ? String.valueOf(libro.getPrecio()) : "", 20);
        JTextField stockField = new JTextField(libro != null ? String.valueOf(libro.getStock()) : "", 20);
        JComboBox<Categoria> categoriaCombo = new JComboBox<>();

        try {
            List<Categoria> categorias = categoriaDAO.obtenerTodas();
            for (Categoria cat : categorias) {
                categoriaCombo.addItem(cat);
            }
            if (libro != null) {
                categoriaCombo.setSelectedItem(libro.getCategoria());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dialog, "Error al cargar categorías: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        agregarCampo(formPanel, gbc, "Título:", tituloField, 0);
        agregarCampo(formPanel, gbc, "Autor:", autorField, 1);
        agregarCampo(formPanel, gbc, "ISBN:", isbnField, 2);
        agregarCampo(formPanel, gbc, "Precio:", precioField, 3);
        agregarCampo(formPanel, gbc, "Stock:", stockField, 4);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Categoría:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        categoriaCombo.setPreferredSize(new Dimension(200, 25));
        formPanel.add(categoriaCombo, gbc);

        panel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        JButton cancelarButton = new JButton("Cancelar");
        cancelarButton.setPreferredSize(new Dimension(100, 30));
        cancelarButton.addActionListener(e -> dialog.dispose());

        JButton guardarButton = new JButton("Guardar");
        guardarButton.setPreferredSize(new Dimension(100, 30));
        guardarButton.addActionListener(e -> {
            try {
                Libro nuevoLibro = new Libro();
                if (libro != null) {
                    nuevoLibro.setId(libro.getId());
                }
                nuevoLibro.setTitulo(tituloField.getText());
                nuevoLibro.setAutor(autorField.getText());
                nuevoLibro.setIsbn(isbnField.getText());
                nuevoLibro.setPrecio(Double.parseDouble(precioField.getText()));
                nuevoLibro.setStock(Integer.parseInt(stockField.getText()));
                nuevoLibro.setCategoria((Categoria) categoriaCombo.getSelectedItem());

                if (libro == null) {
                    libroDAO.insertar(nuevoLibro);
                    JOptionPane.showMessageDialog(dialog, "Libro agregado exitosamente",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    libroDAO.actualizar(nuevoLibro);
                    JOptionPane.showMessageDialog(dialog, "Libro actualizado exitosamente",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }

                cargarLibros();
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Precio y stock deben ser números válidos",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(cancelarButton);
        buttonPanel.add(guardarButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void mostrarDialogoCategoria(Categoria categoria) {
        JDialog dialog = new JDialog(this, categoria == null ? "Agregar Categoría" : "Editar Categoría", true);
        dialog.setSize(350, 200);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField nombreField = new JTextField(categoria != null ? categoria.getNombre() : "", 20);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Nombre:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(nombreField, gbc);

        panel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        JButton cancelarButton = new JButton("Cancelar");
        cancelarButton.setPreferredSize(new Dimension(100, 30));
        cancelarButton.addActionListener(e -> dialog.dispose());

        JButton guardarButton = new JButton("Guardar");
        guardarButton.setPreferredSize(new Dimension(100, 30));
        guardarButton.addActionListener(e -> {
            try {
                Categoria nuevaCategoria = new Categoria();
                if (categoria != null) {
                    nuevaCategoria.setId(categoria.getId());
                }
                nuevaCategoria.setNombre(nombreField.getText());

                if (categoria == null) {
                    categoriaDAO.insertar(nuevaCategoria);
                    JOptionPane.showMessageDialog(dialog, "Categoría agregada exitosamente",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    categoriaDAO.actualizar(nuevaCategoria);
                    JOptionPane.showMessageDialog(dialog, "Categoría actualizada exitosamente",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }

                cargarCategorias();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(cancelarButton);
        buttonPanel.add(guardarButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, String label, JComponent field, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(field, gbc);
    }

    private void editarLibroSeleccionado() {
        int selectedRow = librosTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un libro para editar",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) librosTable.getModel().getValueAt(selectedRow, 0);
        try {
            Libro libro = libroDAO.obtenerPorId(id);
            mostrarDialogoLibro(libro);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al obtener libro: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarCategoriaSeleccionada() {
        int selectedRow = categoriasTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una categoría para editar",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) categoriasTable.getModel().getValueAt(selectedRow, 0);
        try {
            Categoria categoria = categoriaDAO.obtenerPorId(id);
            mostrarDialogoCategoria(categoria);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al obtener categoría: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarLibro() {
        int selectedRow = librosTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un libro para eliminar",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) librosTable.getModel().getValueAt(selectedRow, 0);
        String titulo = (String) librosTable.getModel().getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea eliminar el libro '" + titulo + "'?",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                libroDAO.eliminar(id);
                cargarLibros();
                JOptionPane.showMessageDialog(this, "Libro eliminado exitosamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar libro: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarCategoria() {
        int selectedRow = categoriasTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una categoría para eliminar",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) categoriasTable.getModel().getValueAt(selectedRow, 0);
        String nombre = (String) categoriasTable.getModel().getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea eliminar la categoría '" + nombre + "'?",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                categoriaDAO.eliminar(id);
                cargarCategorias();
                JOptionPane.showMessageDialog(this, "Categoría eliminada exitosamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar categoría: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new BibliotecaGUI().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}