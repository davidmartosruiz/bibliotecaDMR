package org.example.bibliotecaDMR;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LibroTest {

  @Test
  public void testBuilder() {
    // Arrange
    String codigo = "123";
    String nombre = "El nombre del libro";
    String autor = "El autor del libro";
    String isbn = "ISBN123";
    String anyoPublicacion = "2021";
    String categoria = "Ficción";

    // Act
    Libro libro = new Libro.LibroBuilder()
            .codigo(codigo)
            .nombre(nombre)
            .autor(autor)
            .isbn(isbn)
            .anyoPublicacion(anyoPublicacion)
            .categoria(categoria)
            .build();

    // Assert
    assertEquals(codigo, libro.getCodigo());
    assertEquals(nombre, libro.getNombre());
    assertEquals(autor, libro.getAutor());
    assertEquals(isbn, libro.getIsbn());
    assertEquals(anyoPublicacion, libro.getAnyoPublicacion());
    assertEquals(categoria, libro.getCategoria());
  }

  @Test
  public void testPropiedadesRequeridas() {
    // Arrange
    String nombre = "El nombre del libro";
    String autor = "El autor del libro";

    // Act & Assert
    assertThrows(IllegalStateException.class, () -> {
      new Libro.LibroBuilder()
              .nombre(nombre)
              .autor(autor)
              .build();
    });
  }
}
