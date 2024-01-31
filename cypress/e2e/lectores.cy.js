// cypress/integration/lectores_spec.js

describe('Gestión de Lectores', () => {
  it('Visualizar lista de lectores', () => {
    cy.visit('localhost:8080/listadoLectores');
    cy.title('Listado lectores - Bibliotecas');
  });

  it('Agregar nuevo lector', () => {
    cy.visit('localhost:8080/anadirLector');
    cy.rellenarFormularioLector('Nuevo Apellido', 'Nuevo Nombre', 'NuevoDNI', '1990-01-01', '123456789');
    cy.get('input[type="submit"][value="Añadir"]').click();
    cy.title('Listado lectores - Bibliotecas');
    cy.get('table tbody tr').should('have.length.greaterThan', 0);
    cy.contains('Nuevo Apellido');
    cy.contains('Nuevo Nombre');
    cy.contains('NuevoDNI');
    cy.contains('01/01/1990');
    cy.contains('123456789');
  });

  it('Modificar lector existente', () => {
    cy.visit('localhost:8080/listadoLectores');
    cy.get('table tbody tr:last-child td:first-child').invoke('text').as('ultimoID');
    cy.get('@ultimoID').should('not.be.empty').then((ultimoID) => {
      cy.modificarLector(ultimoID, 'Nuevos Apellidos Modificados', 'Nuevo Nombre Modificado');
      cy.title('Listado lectores - Bibliotecas');
      cy.get('table tbody tr:last-child').should('contain', ultimoID);
      cy.get('table tbody tr:last-child').should('contain', 'Nuevos Apellidos Modificados');
      cy.get('table tbody tr:last-child').should('contain', 'Nuevo Nombre Modificado');
    });
  });

  it('Eliminar último lector', () => {
    cy.visit('localhost:8080/listadoLectores');
    cy.get('table tbody tr:last-child td:first-child').invoke('text').as('ultimoID');
    cy.get('@ultimoID').should('not.be.empty').then((ultimoID) => {
      cy.eliminarLector(ultimoID);
    });
  });
});