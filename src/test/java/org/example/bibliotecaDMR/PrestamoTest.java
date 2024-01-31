package org.example.bibliotecaDMR;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PrestamoTest {

  @Autowired
  private Prestamo.PrestamoBuilder prestamoBuilder;

  @Test
  public void testGetCodigoLibro() {
    // Arrange
    String codigoLibro = "1";
    String codigoLector = "1";

    // Act
    Prestamo prestamo = prestamoBuilder
            .codigoLibro(codigoLibro)
            .codigoLector(codigoLector)
            .build();

    String resultado = prestamo.getCodigoLibro();

    // Assert
    assertEquals("1 - La Casa de Bernalda Alba", resultado);
  }

  @Test
  public void testGetCodigoLector() {
    // Arrange
    String codigoLector = "3";
    String codigoLibro = "1";

    // Act
    Prestamo prestamo = prestamoBuilder
            .codigoLector(codigoLector)
            .codigoLector(codigoLector)
            .build();

    String resultado = prestamo.getCodigoLector();

    // Assert
    assertEquals("3 - Martos Ruiz, David", resultado);
  }

}
