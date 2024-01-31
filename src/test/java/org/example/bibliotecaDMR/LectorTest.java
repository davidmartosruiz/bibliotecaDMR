package org.example.bibliotecaDMR;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LectorTest {

  @Autowired
  private Lector.LectorBuilder lectorBuilder;

  @BeforeEach
  public void setup() {
    lectorBuilder = new Lector.LectorBuilder();
  }

  @AfterEach
  public void limpiarLectorBuilder() {
    lectorBuilder = null;
  }

  @Test
  public void testBuilder() {
    // Arrange
    String codigo = "123";
    String apellidos = "Apellidos";
    String nombre = "Nombre";
    String DNI = "12345678A";
    String fechaNacimiento = "2000-01-01";
    String numeroTelefono = "123456789";

    // Act
    Lector lector = lectorBuilder
            .codigo(codigo)
            .apellidos(apellidos)
            .nombre(nombre)
            .DNI(DNI)
            .fechaNacimiento(fechaNacimiento)
            .numeroTelefono(numeroTelefono)
            .build();

    // Assert
    // Realiza las aserciones necesarias para verificar las propiedades del lector
    assertEquals(codigo, lector.getCodigo());
    assertEquals(apellidos, lector.getApellidos());
    assertEquals(nombre, lector.getNombre());
    assertEquals(DNI, lector.getDNI());
    assertEquals("01/01/2000", lector.getFechaNacimiento());
    assertEquals(numeroTelefono, lector.getNumeroTelefono());
  }


  @Test
  public void testFaltanValores() {
    // Arrange
    String nombre = "Nombre";
    String apellidos = "Apellidos";

    // Act & Assert

    Exception exception;
    exception = Assertions.assertThrows(IllegalStateException.class, () -> lectorBuilder
            .nombre(nombre)
            .apellidos(apellidos)
            .build());

    // Verificar el mensaje de la excepción si es necesario
    assertEquals("Faltan propiedades obligatorias para construir el lector", exception.getMessage());
  }
}