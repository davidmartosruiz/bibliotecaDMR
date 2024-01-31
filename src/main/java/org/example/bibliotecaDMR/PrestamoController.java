package org.example.bibliotecaDMR;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PrestamoController {
  @GetMapping("/listadoPrestamos")
  public ModelAndView mostrarPrestamos() throws IOException, CsvValidationException {

    List<Prestamo> listaPrestamos = new ArrayList<>();

    Reader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get("src/main/resources/prestamos.csv"))));
    CSVReader csvReader = new CSVReader(reader);
    csvReader.skip(1);

    String[] fields = null;
    while ((fields = csvReader.readNext()) != null) {
      Prestamo prestamo = new Prestamo.PrestamoBuilder()
              .codigoPrestamo(fields[0])
              .codigoLibro(fields[1])
              .codigoLector(fields[2])
              .fechaInicio(fields[3])
              .fechaFin(fields[4])
              .build();
      listaPrestamos.add(prestamo);
    }

    ModelAndView modelAndView = new ModelAndView();
    modelAndView.addObject("prestamos", listaPrestamos);
    modelAndView.addObject("mensaje", "Lista de prestamos:");
    modelAndView.setViewName("listadoPrestamos");
    return modelAndView;
  }

  @GetMapping("/prestarPrestamo")
  public ModelAndView mostrarFormularioNuevoPrestamo() {
    Prestamo prestamo = new Prestamo.PrestamoBuilder()
            .codigoPrestamo("")
            .codigoLibro("")
            .codigoLector("")
            .fechaInicio("")
            .build();
    ModelAndView mav = new ModelAndView("formularioNuevoPrestamo");
    mav.addObject("prestamo", prestamo);
    return mav;
  }

  @PostMapping("/añadirPrestamoCSV")
  public RedirectView añadirPrestamo(@RequestParam String codigoLibro,
                                     @RequestParam String codigoLector,
                                     @RequestParam String fechaInicio) {
    try {
      FileWriter csvWriter = new FileWriter("src/main/resources/prestamos.csv", true);
      int ultimoCodigoPrestamo = ultimoCodigoPrestamo(); // Obtener último código del archivo CSV
      int nuevoCodigoPrestamo = ultimoCodigoPrestamo + 1; // Incrementar el código para el nuevo libro

      csvWriter.append(Integer.toString(nuevoCodigoPrestamo)); // Agrega el nuevo código de libro
      csvWriter.append(",");
      csvWriter.append(codigoLibro.replace(",", ";"));
      csvWriter.append(",");
      csvWriter.append(codigoLector.replace(",", ";"));
      csvWriter.append(",");
      csvWriter.append(fechaInicio.replace(",", ";"));
      csvWriter.append(",");
      csvWriter.append("\n");
      csvWriter.flush();
      csvWriter.close();
      setUltimoCodigoPrestamo(nuevoCodigoPrestamo); // Actualiza el valor del último código de libro
    } catch (IOException e) {
      e.printStackTrace();
    }

    return new RedirectView("/listadoPrestamos", true);
  }

  private static int ultimoCodigoPrestamo;

  static {
    // Obtener último código del archivo CSV al inicio de la ejecución
    ultimoCodigoPrestamo = getUltimoCodigoPrestamo();
  }
  private int ultimoCodigoPrestamo() {
    try {
      Reader readerPrestamo = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get("src/main/resources/prestamos.csv"))));
      CSVReader csvReader = new CSVReader(readerPrestamo);
      String[] fields;
      csvReader.readNext();
      while ((fields = csvReader.readNext()) != null) {
        ultimoCodigoPrestamo = Integer.parseInt(fields[0]);
      }
    } catch (IOException | CsvValidationException e) {
      e.printStackTrace();
    }
    return ultimoCodigoPrestamo;
  }

  public static int getUltimoCodigoPrestamo() {
    return ultimoCodigoPrestamo;
  }

  public static void setUltimoCodigoPrestamo(int nuevoCodigoPrestamo) {
    ultimoCodigoPrestamo = nuevoCodigoPrestamo;
  }



  @GetMapping("/devolverPrestamo")
  public ModelAndView mostrarFormularioDevolverPrestamo() {
    return new ModelAndView("formularioDevolverPrestamo");
  }

  @PostMapping("/devolverPrestamoCSV")
  public RedirectView devolverPrestamo(@RequestParam String codigoPrestamo,
                                       @RequestParam String fechaFin) {

    try {
      // Leer todo el archivo prestamos.csv
      List<String> lineas = new ArrayList<>();
      BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/prestamos.csv"));
      String linea = null;
      while ((linea = reader.readLine()) != null) {
        String[] campos = linea.split(",");
        if (campos[0].equals(codigoPrestamo)) {
          linea += fechaFin; // Concatenar la fechaFin al final de la línea
        }
        lineas.add(linea);
      }
      reader.close();

      // Escribir de nuevo todo el archivo prestamos.csv
      BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/prestamos.csv"));
      for (String lineaModificada : lineas) {
        writer.write(lineaModificada);
        writer.newLine();
      }
      writer.close();

    } catch (IOException e) {
      e.printStackTrace();
    }

    return new RedirectView("/listadoPrestamos", true);
  }

  @GetMapping("/anularPrestamo")
  public ModelAndView mostrarFormularioAnularPrestamo() {
    return new ModelAndView("formularioAnularPrestamo");
  }

  @PostMapping("/anularPrestamoCSV")
  public RedirectView anularPrestamo(@RequestParam String codigo) {
    try {
      Reader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get("src/main/resources/prestamos.csv"))));
      CSVReader csvReader = new CSVReader(reader);
      List<String[]> lines = csvReader.readAll();
      csvReader.close();

      FileWriter writer = new FileWriter("src/main/resources/prestamos.csv");
      boolean encontrado = false;
      for (String[] line : lines) {
        if (line.length > 0 && line[0].equals(codigo)) {
          encontrado = true;
        } else {
          writer.append(String.join(",", line));
          writer.append("\n");
        }
      }
      writer.flush();
      writer.close();

      if (!encontrado) {
        throw new IllegalArgumentException("El código del préstamo a anular no existe");
      }

    } catch (IOException | CsvValidationException e) {
      e.printStackTrace();
    } catch (CsvException e) {
      throw new RuntimeException(e);
    }

    return new RedirectView("/listadoPrestamos", true);
  }
}
