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
public class LibroController {
  @GetMapping("/")
  public ModelAndView mostrarLibros() throws IOException, CsvValidationException {

    List<Libro> listaLibros = new ArrayList<>();

    Reader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get("src/main/resources/libros.csv"))));
    CSVReader csvReader = new CSVReader(reader);
    csvReader.skip(1);

    String[] fields = null;
    while ((fields = csvReader.readNext()) != null) {
      Libro libro = new Libro.LibroBuilder()
              .codigo(fields[0])
              .nombre(fields[1])
              .autor(fields[2])
              .isbn(fields[3])
              .anyoPublicacion(fields[4])
              .categoria(fields[5])
              .build();
      listaLibros.add(libro);
    }

    ModelAndView modelAndView = new ModelAndView();
    modelAndView.addObject("libros", listaLibros);
    modelAndView.addObject("mensaje", "Lista de libros:");
    modelAndView.setViewName("index");
    return modelAndView;
  }

  @GetMapping("/anadirLibro")
  public ModelAndView mostrarFormularioNuevoLibro() {
    Libro libro = new Libro.LibroBuilder()
            .codigo("")
            .nombre("")
            .autor("")
            .isbn("")
            .anyoPublicacion("")
            .categoria("")
            .build();
    ModelAndView mav = new ModelAndView("formularioNuevoLibro");
    mav.addObject("libro", libro);
    return mav;
  }



  @PostMapping("/anadirLibroCSV")
  public RedirectView anadirLibro(@RequestParam String nombre,
                                  @RequestParam String autor,
                                  @RequestParam String isbn,
                                  @RequestParam String anyoPublicacion,
                                  @RequestParam String categoria) {
    try {
      FileWriter csvWriter = new FileWriter("src/main/resources/libros.csv", true);
      int ultimoCodigo = ultimoCodigoLibro(); // Obtener último código del archivo CSV
      int nuevoCodigo = ultimoCodigo + 1; // Incrementar el código para el nuevo libro

      csvWriter.append(Integer.toString(nuevoCodigo)); // Agrega el nuevo código de libro
      csvWriter.append(",");
      csvWriter.append(nombre.replace(",", ";"));
      csvWriter.append(",");
      csvWriter.append(autor.replace(",", ";"));
      csvWriter.append(",");
      csvWriter.append(isbn.replace(",", ";"));
      csvWriter.append(",");
      csvWriter.append(anyoPublicacion.replace(",", ";"));
      csvWriter.append(",");
      csvWriter.append(categoria.replace(",", ";"));
      csvWriter.append("\n");
      csvWriter.flush();
      csvWriter.close();
      setUltimoCodigoLibro(nuevoCodigo); // Actualiza el valor del último código de libro
    } catch (IOException e) {
      e.printStackTrace();
    }

    return new RedirectView("/", true);
  }

  private static int ultimoCodigoLibro;

  static {
    // Obtener último código del archivo CSV al inicio de la ejecución
    ultimoCodigoLibro = getUltimoCodigoLibro();
  }
  private int ultimoCodigoLibro() {
    try {
      Reader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get("src/main/resources/libros.csv"))));
      CSVReader csvReader = new CSVReader(reader);
      String[] fields;
      csvReader.readNext();
      while ((fields = csvReader.readNext()) != null) {
        ultimoCodigoLibro = Integer.parseInt(fields[0]);
      }
    } catch (IOException | CsvValidationException e) {
      e.printStackTrace();
    }
    return ultimoCodigoLibro;
  }

  public static int getUltimoCodigoLibro() {
    return ultimoCodigoLibro;
  }

  public static void setUltimoCodigoLibro(int nuevoCodigoLibro) {
    ultimoCodigoLibro = nuevoCodigoLibro;
  }


  @GetMapping("/modificarLibro")
  public ModelAndView mostrarFormularioModificarLibro() {
    return new ModelAndView("formularioModificarLibro");
  }

  @PostMapping("/modificarLibroPrecargado")
  public ModelAndView mostrarFormularioModificarLibroPrecargado(@RequestParam String codigo) throws IOException, CsvValidationException {
    ModelAndView modelAndView = new ModelAndView();

    try {
      Libro libro = null;

      Reader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get("src/main/resources/libros.csv"))));
      CSVReader csvReader = new CSVReader(reader);
      csvReader.skip(1);

      String[] fields = null;
      while ((fields = csvReader.readNext()) != null) {
        if (fields[0].equals(codigo)) {
          libro = new Libro.LibroBuilder()
                  .codigo(fields[0])
                  .nombre(fields[1])
                  .autor(fields[2])
                  .isbn(fields[3])
                  .anyoPublicacion(fields[4])
                  .categoria(fields[5])
                  .build();
          break;
        }
      }

      modelAndView.addObject("libro", libro);
      modelAndView.addObject("mensaje", "Libro:");
      modelAndView.setViewName("formularioModificarLibroPrecargado");
    } catch (IOException | CsvValidationException e) {
      e.printStackTrace();
    }

    return modelAndView;
  }

  @PostMapping("/modificarLibroPrecargadoCSV")
  public RedirectView modificarLibroPrecargadoCSV(@RequestParam String codigo,
                                                   @RequestParam(required = false) String nombre,
                                                   @RequestParam(required = false) String autor,
                                                   @RequestParam(required = false) String isbn,
                                                   @RequestParam(required = false) String anyoPublicacion,
                                                   @RequestParam(required = false) String categoria) {
    try {
      File file = new File("src/main/resources/libros.csv");
      File tempFile = new File("src/main/resources/libros_temp.csv");

      BufferedReader reader = new BufferedReader(new FileReader(file));
      BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

      String line;
      while ((line = reader.readLine()) != null) {
        String[] fields = line.split(",");
        if (fields[0].equals(codigo)) {
          // Sobreescribe la línea con los nuevos datos del libro
          StringBuilder modifiedLine = new StringBuilder(codigo);

          if (nombre != null) {
            modifiedLine.append(",").append(nombre.replace(",", ";"));
          } else {
            modifiedLine.append(",").append(fields[1]);
          }

          if (autor != null) {
            modifiedLine.append(",").append(autor.replace(",", ";"));
          } else {
            modifiedLine.append(",").append(fields[2]);
          }

          if (isbn != null) {
            modifiedLine.append(",").append(isbn.replace(",", ";"));
          } else {
            modifiedLine.append(",").append(fields[3]);
          }

          if (anyoPublicacion != null) {
            modifiedLine.append(",").append(anyoPublicacion.replace(",", ";"));
          } else {
            modifiedLine.append(",").append(fields[4]);
          }

          if (categoria != null) {
            modifiedLine.append(",").append(categoria.replace(",", ";"));
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

    return new RedirectView("/", true);
  }
















  @GetMapping("/eliminarLibro")
  public ModelAndView mostrarFormularioEliminarLibro() {
    return new ModelAndView("formularioEliminarLibro");
  }

  @PostMapping("/eliminarLibroCSV")
  public RedirectView eliminarLibro(@RequestParam String codigo) {
    try {
      Reader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get("src/main/resources/libros.csv"))));
      CSVReader csvReader = new CSVReader(reader);
      List<String[]> lines = csvReader.readAll();
      csvReader.close();

      FileWriter writer = new FileWriter("src/main/resources/libros.csv");
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
        throw new IllegalArgumentException("El código del libro a eliminar no existe");
      }

    } catch (IOException | CsvValidationException e) {
      e.printStackTrace();
    } catch (CsvException e) {
      throw new RuntimeException(e);
    }

    return new RedirectView("/", true);
  }

  @GetMapping("/exportarPDF")
  public void exportarPDF(HttpServletResponse response) throws FileNotFoundException, DocumentException, IOException, CsvValidationException {
    List<Libro> listaLibros = new ArrayList<>();
    Reader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get("src/main/resources/libros.csv"))));
    CSVReader csvReader = new CSVReader(reader);
    csvReader.skip(1);
    String[] fields = null;
    while ((fields = csvReader.readNext()) != null) {
      Libro libro = new Libro.LibroBuilder()
              .codigo(fields[0])
              .nombre(fields[1])
              .autor(fields[2])
              .isbn(fields[3])
              .anyoPublicacion(fields[4])
              .categoria(fields[5])
              .build();
      listaLibros.add(libro);
    }


    response.setContentType("application/pdf");
    response.setHeader("Content-Disposition", "attachment; filename=\"libros.pdf\"");
    Document document = new Document(PageSize.A4.rotate());
    PdfWriter.getInstance(document, response.getOutputStream());


    document.addTitle("Listado de libros");
    document.addAuthor("Sistema de Gestión de Bibliotecas - David Martos Ruiz");
    document.open();

    // Añadir título
    Paragraph titulo = new Paragraph("Listado de libros");
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

    cell = new PdfPCell(new Phrase("Nombre", font));
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    tabla.addCell(cell);

    cell = new PdfPCell(new Phrase("Autor", font));
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    tabla.addCell(cell);

    cell = new PdfPCell(new Phrase("ISBN", font));
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    tabla.addCell(cell);

    cell = new PdfPCell(new Phrase("Año Publicación", font));
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    tabla.addCell(cell);

    cell = new PdfPCell(new Phrase("Categoría", font));
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    tabla.addCell(cell);

    // Añadir filas de datos de la tabla
    for (Libro libro : listaLibros) {
      tabla.addCell(libro.getCodigo());
      tabla.addCell(libro.getNombre());
      tabla.addCell(libro.getAutor());
      tabla.addCell(libro.getIsbn());
      tabla.addCell(libro.getAnyoPublicacion());
      tabla.addCell(libro.getCategoria());
    }

    // Añadir tabla al documento
    document.add(tabla);
    document.close();
  }
}
