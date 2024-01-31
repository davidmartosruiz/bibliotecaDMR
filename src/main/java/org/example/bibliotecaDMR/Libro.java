package org.example.bibliotecaDMR;

import org.springframework.stereotype.Component;

@Component
public class Libro {
  private final String codigo;
  private final String nombre;
  private final String autor;
  private final String isbn;
  private final String anyoPublicacion;
  private final String categoria;

  private Libro(LibroBuilder builder) {
    this.codigo = builder.codigo;
    this.nombre = builder.nombre;
    this.autor = builder.autor;
    this.isbn = builder.isbn;
    this.anyoPublicacion = builder.anyoPublicacion;
    this.categoria = builder.categoria;
  }

  // Métodos getters para acceder a las propiedades
  public String getCodigo() {
    return codigo;
  }

  public String getNombre() {
    return nombre;
  }

  public String getAutor() {
    return autor;
  }

  public String getIsbn() {
    return isbn;
  }

  public String getAnyoPublicacion() {
    return anyoPublicacion;
  }

  public String getCategoria() {
    return categoria;
  }

  // Clase Builder para construir objetos Libro de forma más legible
  public static class LibroBuilder {
    private String codigo;
    private String nombre;
    private String autor;
    private String isbn;
    private String anyoPublicacion;
    private String categoria;

    public LibroBuilder() {
      // Inicializa las propiedades opcionales con valores por defecto
      this.codigo = String.valueOf(LibroController.getUltimoCodigoLibro() + 1);
      this.anyoPublicacion = "";
      this.categoria = "";
    }

    public LibroBuilder codigo(String codigo) {
      this.codigo = codigo;
      return this;
    }

    public LibroBuilder nombre(String nombre) {
      this.nombre = nombre;
      return this;
    }

    public LibroBuilder autor(String autor) {
      this.autor = autor;
      return this;
    }

    public LibroBuilder isbn(String isbn) {
      this.isbn = isbn;
      return this;
    }

    public LibroBuilder anyoPublicacion(String anyoPublicacion) {
      this.anyoPublicacion = anyoPublicacion;
      return this;
    }

    public LibroBuilder categoria(String categoria) {
      this.categoria = categoria;
      return this;
    }

    public Libro build() {
      // Verifica que todas las propiedades obligatorias se han inicializado
      if (this.nombre == null || this.autor == null || this.isbn == null) {
        throw new IllegalStateException("Faltan propiedades obligatorias para construir el libro");
      }
      return new Libro(this);
    }
  }
}
