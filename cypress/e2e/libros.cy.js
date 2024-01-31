// cypress/integration/libros_spec.js

describe('Gestión de Libros', () => {
  it('Visualizar lista de libros', () => {
    cy.visit('localhost:8080');
    cy.title('Listado libros - Bibliotecas');
  });

  it('Agregar nuevo libro', () => {
    cy.visit('localhost:8080/anadirLibro');
    cy.rellenarFormularioLibro('Nuevo Nombre', 'Nuevo Autor', 'Nuevo ISBN', '2000', 'Nueva Categoría');
    cy.get('input[type="submit"][value="Añadir"]').click();
    cy.title('Listado libros - Bibliotecas');
    cy.verificarLibroEnTabla('Nuevo Nombre', 'Nuevo Autor', 'Nuevo ISBN', '2000', 'Nueva Categoría');
  });

  it('Modificar libro existente', () => {
    cy.visit('localhost:8080');
    cy.get('table tbody tr:last-child td:first-child').invoke('text').as('ultimoID');
    cy.get('@ultimoID').should('not.be.empty').then((ultimoID) => {
      cy.modificarLibro(ultimoID, 'Nuevo Nombre Modificado', 'Nuevo Autor Modificado');
      cy.title('Listado libros - Bibliotecas');
      cy.verificarLibroEnTabla('Nuevo Nombre Modificado', 'Nuevo Autor Modificado', 'Nuevo ISBN', '2000', 'Nueva Categoría');
    });
  });

  it('Eliminar último libro', () => {
    cy.visit('localhost:8080');
    cy.get('table tbody tr:last-child td:first-child').invoke('text').as('ultimoID');
    cy.get('@ultimoID').should('not.be.empty').then((ultimoID) => {
      cy.eliminarLibro(ultimoID);
    });
  });
});
