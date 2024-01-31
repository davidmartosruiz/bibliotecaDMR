package org.example.bibliotecaDMR;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.stereotype.Component;

@Component
public class Prestamo {
  private final String codigoPrestamo;
  private final String codigoLibro;
  private final String codigoLector;
  private final String fechaInicio;
  private final String fechaFin;

  private Prestamo(PrestamoBuilder builder) {
    this.codigoPrestamo = builder.codigoPrestamo;
    this.codigoLibro = builder.codigoLibro;
    this.codigoLector = builder.codigoLector;
    this.fechaInicio = builder.fechaInicio;
    this.fechaFin = builder.fechaFin;
  }

  public String getCodigoPrestamo() {
    return codigoPrestamo;
  }

  public String getCodigoLibro() {
    try (CSVReader reader = new CSVReader(new FileReader("src/main/resources/libros.csv"))) {
      List<String[]> rows = reader.readAll();
      for (String[] row : rows) {
        if (row[0].equals(codigoLibro)) {
          return row[0] + " - " + row[1];
        }
      }
      return "No se encontró el libro";
    } catch (IOException e) {
      return "Error al leer el archivo";
    } catch (CsvException e) {
      throw new RuntimeException(e);
    }
  }

  public String getCodigoLector() {
    try (CSVReader reader = new CSVReader(new FileReader("src/main/resources/lectores.csv"))) {
      List<String[]> rows = reader.readAll();
      for (String[] row : rows) {
        if (row[0].equals(codigoLector)) {
          return row[0] + " - " + row[1] + ", " + row[2];
        }
      }
      return "No se encontró el lector";
    } catch (IOException e) {
      return "Error al leer el archivo";
    } catch (CsvException e) {
      throw new RuntimeException(e);
    }
  }

  public String getFechaInicio() {
    SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
    Date fecha = null;
    try {
      fecha = formatoFecha.parse(fechaInicio);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    formatoFecha.applyPattern("dd/MM/yyyy");
    return formatoFecha.format(fecha);
  }

  public String getFechaFin() {
    if (fechaFin.equals("")) {
      return "No devuelto";
    } else {
      SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
      Date fecha = null;
      try {
        fecha = formatoFecha.parse(fechaFin);
      } catch (ParseException e) {
        e.printStackTrace();
      }
      formatoFecha.applyPattern("dd/MM/yyyy");
      return formatoFecha.format(fecha);
    }
  }

  public static class PrestamoBuilder {
    private String codigoPrestamo;
    private String codigoLibro;
    private String codigoLector;
    private String fechaInicio;
    private String fechaFin;

    public PrestamoBuilder() {
      // Inicializa las propiedades opcionales con valores por defecto
      this.codigoPrestamo = String.valueOf(PrestamoController.getUltimoCodigoPrestamo() + 1);
    }

    public PrestamoBuilder codigoPrestamo(String codigoPrestamo) {
      this.codigoPrestamo = codigoPrestamo;
      return this;
    }

    public PrestamoBuilder codigoLibro(String codigoLibro) {
      this.codigoLibro = codigoLibro;
      return this;
    }

    public PrestamoBuilder codigoLector(String codigoLector) {
      this.codigoLector = codigoLector;
      return this;
    }

    public PrestamoBuilder fechaInicio(String fechaInicio) {
      this.fechaInicio = fechaInicio;
      return this;
    }

    public PrestamoBuilder fechaFin(String fechaFin) {
      this.fechaFin = fechaFin;
      return this;
    }

    public Prestamo build() {
      // Verifica que todas las propiedades obligatorias se han inicializado
      if (this.codigoLibro == null || this.codigoLector == null) {
        throw new IllegalStateException("Faltan propiedades obligatorias para construir el prestamo");
      }
      return new Prestamo(this);
    }
  }
}
