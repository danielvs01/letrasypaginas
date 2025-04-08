GitHub:
https://github.com/danielvs01/letrasypaginas.git

Modelo
Categoria: Representa una categoría de libros con id y nombre.

Libro: Representa un libro con sus atributos (título, autor, ISBN, etc.) y su categoría asociada.

Acceso a Datos (DAO)
Interfaces:

CategoriaDAO: Define operaciones CRUD para categorías.

LibroDAO: Define operaciones CRUD para libros.

Implementaciones:

CategoriaSQL: Implementa CategoriaDAO para MySQL.

LibroSQL: Implementa LibroDAO para MySQL.

Utilidades
BD: Maneja la conexión con la base de datos y crea la estructura inicial si no existe.

Interfaz Gráfica
BibliotecaGUI: Ventana principal con pestañas para gestionar libros y categorías.

LibroTableModel: Modelo de tabla personalizado para mostrar libros.

Funcionalidades Principales
Gestión de Categorías
insertar(Categoria): Añade una nueva categoría a la base de datos.

actualizar(Categoria): Modifica una categoría existente.

eliminar(int id): Elimina una categoría (si no tiene libros asociados).

obtenerTodas(): Devuelve todas las categorías.

obtenerPorId(int id): Busca una categoría por su ID.

obtenerPorNombre(String nombre): Busca una categoría por nombre.

Gestión de Libros
insertar(Libro): Añade un nuevo libro a la base de datos.

actualizar(Libro): Modifica un libro existente.

eliminar(int id): Elimina un libro.

obtenerTodo(): Devuelve todos los libros.

buscarPor...(): Diversos métodos para buscar libros por ISBN, título o autor.

Interfaz de Usuario
Pestaña para gestionar libros con búsqueda, agregar, editar y eliminar.

Pestaña para gestionar categorías con agregar, editar y eliminar.

Validación de datos y manejo de errores.