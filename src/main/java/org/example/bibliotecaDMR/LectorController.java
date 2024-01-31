package org.example.bibliotecaDMR;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
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
public class LectorController {
  @GetMapping("/listadoLectores")
  public ModelAndView mostrarLectores() throws IOException, CsvValidationException {

    List<Lector> listaLectores = new ArrayList<>();

    Reader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get("src/main/resources/lectores.csv"))));
    CSVReader csvReader = new CSVReader(reader);
    csvReader.skip(1);

    String[] fields = null;
    while ((fields = csvReader.readNext()) != null) {
      Lector lector = new Lector.LectorBuilder()
              .codigo(fields[0])
              .apellidos(fields[1])
              .nombre(fields[2])
              .DNI(fields[3])
              .fechaNacimiento(fields[4])
              .numeroTelefono(fields[5])
              .build();
      listaLectores.add(lector);
    }
    ModelAndView modelAndView = new ModelAndView();
    modelAndView.addObject("lectores", listaLectores);
    modelAndView.addObject("mensaje", "Lista de lectores:");
    modelAndView.setViewName("listadoLectores");
    return modelAndView;
  }

  @GetMapping("/anadirLector")
  public ModelAndView mostrarFormularioNuevoLector() {
    Lector lector = new Lector.LectorBuilder()
            .codigo("")
            .apellidos("")
            .nombre("")
            .DNI("")
            .fechaNacimiento("")
            .numeroTelefono("")
            .build();
    ModelAndView mav = new ModelAndView("formularioNuevoLector");
    mav.addObject("lector", lector);
    return mav;
  }



  @PostMapping("/anadirLectorCSV")
  public RedirectView anadirLector(@RequestParam String apellidos,
                                   @RequestParam String nombre,
                                   @RequestParam String DNI,
                                   @RequestParam String fechaNacimiento,
                                   @RequestParam String numeroTelefono) {
    try {
      FileWriter csvWriter = new FileWriter("src/main/resources/lectores.csv", true);
      int ultimoCodigoLector = ultimoCodigoLector(); // Obtener último código del archivo CSV
      int nuevoCodigoLector = ultimoCodigoLector + 1; // Incrementar el código para el nuevo libro

      csvWriter.append(Integer.toString(nuevoCodigoLector)); // Agrega el nuevo código de libro
      csvWriter.append(",");
      csvWriter.append(apellidos.replace(",", ";"));
      csvWriter.append(",");
      csvWriter.append(nombre.replace(",", ";"));
      csvWriter.append(",");
      csvWriter.append(DNI.replace(",", ";"));
      csvWriter.append(",");
      csvWriter.append(fechaNacimiento.replace(",", ";"));
      csvWriter.append(",");
      csvWriter.append(numeroTelefono.replace(",", ";"));
      csvWriter.append("\n");
      csvWriter.flush();
      csvWriter.close();
      setUltimoCodigoLector(nuevoCodigoLector); // Actualiza el valor del último código de libro
    } catch (IOException e) {
      e.printStackTrace();
    }

    return new RedirectView("/listadoLectores", true);
  }


  private static int ultimoCodigoLector;

  static {
    // Obtener último código del archivo CSV al inicio de la ejecución
    ultimoCodigoLector = getUltimoCodigoLector();
  }
  private int ultimoCodigoLector() {
    try {
      Reader readerLector = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get("src/main/resources/lectores.csv"))));
      CSVReader csvReader = new CSVReader(readerLector);
      String[] fields;
      csvReader.readNext();
      while ((fields = csvReader.readNext()) != null) {
        ultimoCodigoLector = Integer.parseInt(fields[0]);
      }
    } catch (IOException | CsvValidationException e) {
      e.printStackTrace();
    }
    return ultimoCodigoLector;
  }

  public static int getUltimoCodigoLector() {
    return ultimoCodigoLector;
  }

  public static void setUltimoCodigoLector(int nuevoCodigoLector) {
    ultimoCodigoLector = nuevoCodigoLector;
  }

  @GetMapping("/modificarLector")
  public ModelAndView mostrarFormularioModificarLector() {
    return new ModelAndView("formularioModificarLector");
  }

  @PostMapping("/modificarLectorPrecargado")
  public ModelAndView mostrarFormularioModificarLectorPrecargado(@RequestParam String codigo) throws IOException, CsvValidationException {
    ModelAndView modelAndView = new ModelAndView();

    try {
      Lector lector = null;

      Reader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get("src/main/resources/lectores.csv"))));
      CSVReader csvReader = new CSVReader(reader);
      csvReader.skip(1);

      String[] fields = null;
      while ((fields = csvReader.readNext()) != null) {
        if (fields[0].equals(codigo)) {
          lector = new Lector.LectorBuilder()
                  .codigo(fields[0])
                  .apellidos(fields[1])
                  .nombre(fields[2])
                  .DNI(fields[3])
                  .fechaNacimiento(fields[4])
                  .numeroTelefono(fields[5])
                  .build();
          break;
        }
      }

      modelAndView.addObject("lector", lector);
      modelAndView.addObject("mensaje", "Lector:");
      modelAndView.setViewName("formularioModificarLectorPrecargado");
    } catch (IOException | CsvValidationException e) {
      e.printStackTrace();
    }

    return modelAndView;
  }

  @PostMapping("/modificarLectorPrecargadoCSV")
  public RedirectView modificarLectorPrecargadoCSV(@RequestParam String codigo,
                                                   @RequestParam(required = false) String apellidos,
                                                   @RequestParam(required = false) String nombre,
                                                   @RequestParam(required = false) String DNI,
                                                   @RequestParam(required = false) String fechaNacimiento,
                                                   @RequestParam(required = false) String numeroTelefono) {
    try {
      File file = new File("src/main/resources/lectores.csv");
      File tempFile = new File("src/main/resources/lectores_temp.csv");

      BufferedReader reader = new BufferedReader(new FileReader(file));
      BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

      String line;
      while ((line = reader.readLine()) != null) {
        String[] fields = line.split(",");
        if (fields[0].equals(codigo)) {
          // Sobreescribe la línea con los nuevos datos del lector
          StringBuilder modifiedLine = new StringBuilder(codigo);

          if (apellidos != null) {
            modifiedLine.append(",").append(apellidos.replace(",", ";"));
          } else {
            modifiedLine.append(",").append(fields[1]);
          }

          if (nombre != null) {
            modifiedLine.append(",").append(nombre.replace(",", ";"));
          } else {
            modifiedLine.append(",").append(fields[2]);
          }

          if (DNI != null) {
            modifiedLine.append(",").append(DNI.replace(",", ";"));
          } else {
            modifiedLine.append(",").append(fields[3]);
          }

          if (fechaNacimiento != null) {
            modifiedLine.append(",").append(fechaNacimiento.replace(",", ";"));
          } else {
            modifiedLine.append(",").append(fields[4]);
          }

          if (numeroTelefono != null) {
            modifiedLine.append(",").append(numeroTelefono.replace(",", ";"));
          } else {
            modifiedLine.append(",").append(fields[5]);
          }

          line = modifiedLine.toString();
        }
        writer.write(line);
        writer.newLine();
      }

      reader.close();
      writer.close();

      // Renombra el archivo temporal al archivo original
      if (file.delete()) {
        tempFile.renameTo(file);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return new RedirectView("/listadoLectores", true);
  }

  @GetMapping("/eliminarLector")
  public ModelAndView mostrarFormularioEliminarLector() {
    return new ModelAndView("formularioEliminarLector");
  }

  @PostMapping("/eliminarLectorCSV")
  public RedirectView eliminarLector(@RequestParam String codigo) {
    try {
      Reader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get("src/main/resources/lectores.csv"))));
      CSVReader csvReader = new CSVReader(reader);
      List<String[]> lines = csvReader.readAll();
      csvReader.close();

      FileWriter writer = new FileWriter("src/main/resources/lectores.csv");
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
        throw new IllegalArgumentException("El código del lector a eliminar no existe");
      }

    } catch (IOException | CsvValidationException e) {
      e.printStackTrace();
    } catch (CsvException e) {
      throw new RuntimeException(e);
    }

    return new RedirectView("/listadoLectores", true);
  }

  @GetMapping("/exportarPDFLector")
  public void exportarPDFLector(HttpServletResponse response) throws FileNotFoundException, DocumentException, IOException, CsvValidationException {
    List<Lector> listaLectores = new ArrayList<>();
    Reader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get("src/main/resources/lectores.csv"))));
    CSVReader csvReader = new CSVReader(reader);
    csvReader.skip(1);
    String[] fields = null;
    while ((fields = csvReader.readNext()) != null) {
      Lector lector = new Lector.LectorBuilder()
              .codigo(fields[0])
              .apellidos(fields[1])
              .nombre(fields[2])
              .DNI(fields[3])
              .fechaNacimiento(fields[4])
              .numeroTelefono(fields[5])
              .build();
      listaLectores.add(lector);
    }


    response.setContentType("application/pdf");
    response.setHeader("Content-Disposition", "attachment; filename=\"lectores.pdf\"");
    Document document = new Document(PageSize.A4.rotate());
    PdfWriter.getInstance(document, response.getOutputStream());


    document.addTitle("Listado de lectores");
    document.addAuthor("Sistema de Gestión de Bibliotecas - David Martos Ruiz");
    document.open();

    // Añadir título
    Paragraph titulo = new Paragraph("Listado de lectores");
    titulo.setAlignment(Element.ALIGN_CENTER);
    document.add(titulo);

    // Crear tabla
    PdfPTable tabla = new PdfPTable(6);
    tabla.setWidthPercentage(100);
    tabla.setSpacingBefore(10f);
    tabla.setSpacingAfter(10f);

    // Añadir encabezado de tabla
    Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
    PdfPCell cell = new PdfPCell(new Phrase("Código", font));
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    tabla.addCell(cell);

    cell = new PdfPCell(new Phrase("Apellidos", font));
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    tabla.addCell(cell);

    cell = new PdfPCell(new Phrase("Nombre", font));
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    tabla.addCell(cell);

    cell = new PdfPCell(new Phrase("DNI", font));
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    tabla.addCell(cell);

    cell = new PdfPCell(new Phrase("Fecha nacimiento", font));
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    tabla.addCell(cell);

    cell = new PdfPCell(new Phrase("Número de teléfono", font));
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    tabla.addCell(cell);

    // Añadir filas de datos de la tabla
    for (Lector lector : listaLectores) {
      tabla.addCell(lector.getCodigo());
      tabla.addCell(lector.getApellidos());
      tabla.addCell(lector.getNombre());
      tabla.addCell(lector.getDNI());
      tabla.addCell(lector.getFechaNacimiento());
      tabla.addCell(lector.getNumeroTelefono());
    }

    // Añadir tabla al documento
    document.add(tabla);
    document.close();
  }

  @GetMapping("/exportarPDFCarnets")
  public void exportarPDFCarnets(HttpServletResponse response) throws FileNotFoundException, DocumentException, IOException, CsvValidationException {
    List<Lector> listaLectores = new ArrayList<>();
    Reader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get("src/main/resources/lectores.csv"))));
    CSVReader csvReader = new CSVReader(reader);
    csvReader.skip(1);
    String[] fields = null;
    while ((fields = csvReader.readNext()) != null) {
      Lector lector = new Lector.LectorBuilder()
              .codigo(fields[0])
              .apellidos(fields[1])
              .nombre(fields[2])
              .DNI(fields[3])
              .fechaNacimiento(fields[4])
              .numeroTelefono(fields[5])
              .build();
      listaLectores.add(lector);
    }

    response.setContentType("application/pdf");
    response.setHeader("Content-Disposition", "attachment; filename=\"carnets.pdf\"");
    Document document = new Document(PageSize.A7, 10, 10, 10, 10); // Tamaño de tarjeta de crédito
    PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
    document.open();
    PdfContentByte cb = writer.getDirectContent();

    for (Lector lector : listaLectores) {
      // Crear tabla de datos de carnet
      PdfPTable tabla = new PdfPTable(2);
      tabla.setWidthPercentage(100);
      tabla.setSpacingBefore(10f);
      tabla.setSpacingAfter(5f);

      // Añadir filas de datos de la tabla
      tabla.addCell(new Phrase("Nombre:"));
      tabla.addCell(new Phrase(lector.getNombre() + " " + lector.getApellidos()));
      tabla.addCell(new Phrase("DNI:"));
      tabla.addCell(new Phrase(lector.getDNI()));
      tabla.addCell(new Phrase("Fecha de nacimiento:"));
      tabla.addCell(new Phrase(lector.getFechaNacimiento()));
      tabla.addCell(new Phrase("Número de teléfono:"));
      tabla.addCell(new Phrase(lector.getNumeroTelefono()));

      // Agregar un nuevo párrafo con el título del documento
      Paragraph titulo = new Paragraph("CARNET BIBLIOTECARIO", new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL));
      titulo.setAlignment(Element.ALIGN_CENTER);
      document.add(titulo);

      // Agregar tabla de datos al documento
      document.add(tabla);

      // Agregar un nuevo párrafo con el código del lector centrado y en grande
      Paragraph codigoLector = new Paragraph(lector.getCodigo(), new Font(Font.FontFamily.HELVETICA, 70, Font.NORMAL));
      codigoLector.setAlignment(Element.ALIGN_CENTER);
      document.add(codigoLector);

      // Agregar una página nueva después de cada carnet
      document.newPage();
    }

    document.close();
  }
}
