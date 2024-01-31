package org.example.bibliotecaDMR;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Component
public class Lector {
  private final String codigo;
  private final String apellidos;
  private final String nombre;
  private final String DNI;
  private final String fechaNacimiento;
  private final String numeroTelefono;

  private static final List<Lector> lectores = new ArrayList<>(); // lista de lectores creados

  private Lector(LectorBuilder builder) {
    this.codigo = builder.codigo;
    this.apellidos = builder.apellidos;
    this.nombre = builder.nombre;
    this.DNI = builder.DNI;
    this.fechaNacimiento = builder.fechaNacimiento;
    this.numeroTelefono = builder.numeroTelefono;
    lectores.add(this); // añadir el lector a la lista de lectores creados
  }

  // Métodos getters para acceder a las propiedades
  public String getCodigo() {
    return codigo;
  }

  public String getApellidos() {
    return apellidos;
  }

  public String getNombre() {
    return nombre;
  }

  public String getDNI() {
    return DNI;
  }

  public String getFechaNacimiento() {
    SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
    Date fecha = null;
    try {
      fecha = formatoFecha.parse(fechaNacimiento);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    formatoFecha.applyPattern("dd/MM/yyyy");
    return formatoFecha.format(fecha);
  }

  public String getNumeroTelefono() {
    return numeroTelefono;
  }

  // Clase Builder para construir objetos Lector de forma más legible
  public static class LectorBuilder {
    private String codigo;
    private String apellidos;
    private String nombre;
    private String DNI;
    private String fechaNacimiento;
    private String numeroTelefono;

    public LectorBuilder() {
      // Inicializa las propiedades opcionales con valores por defecto
      this.codigo = String.valueOf(lectores.size() + 1);
      this.fechaNacimiento = "";
      this.numeroTelefono = "";
    }

    public LectorBuilder codigo(String codigo) {
      this.codigo = codigo;
      return this;
    }

    public LectorBuilder apellidos(String apellidos) {
      this.apellidos = apellidos;
      return this;
    }

    public LectorBuilder nombre(String nombre) {
      this.nombre = nombre;
      return this;
    }

    public LectorBuilder DNI(String DNI) {
      this.DNI = DNI;
      return this;
    }

    public LectorBuilder fechaNacimiento(String fechaNacimiento) {
      this.fechaNacimiento = fechaNacimiento;
      return this;
    }

    public LectorBuilder numeroTelefono(String numeroTelefono) {
      this.numeroTelefono = numeroTelefono;
      return this;
    }

    public Lector build() {
      // Verifica que todas las propiedades obligatorias se han inicializado
      if (this.apellidos == null || this.nombre == null || this.DNI == null) {
        throw new IllegalStateException("Faltan propiedades obligatorias para construir el lector");
      }
      return new Lector(this);
    }
  }
}