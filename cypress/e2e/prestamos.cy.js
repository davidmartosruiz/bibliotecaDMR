// prestamos.cy.js

describe('Gestión de Préstamos', () => {
  it('Prestar libro', () => {
    cy.prestarLibro('1', '1', '2000-01-01');
    cy.title('Listado prestamos - Bibliotecas');
    cy.get('table tbody tr').should('have.length.greaterThan', 0);
    cy.contains('1 - La Casa de Bernalda Alba');
    cy.contains('1 - Fernández Sánchez, Daniel');
    cy.contains('01/01/2000');
  });

  it('Devolver libro', () => {
    cy.devolverPrestamo('2000-12-31');
    cy.title('Listado prestamos - Bibliotecas');
    cy.getLastPrestamoID().then((ultimoID) => {
      cy.get('table tbody tr:last-child td:first-child').should('contain', ultimoID);
    });
    cy.contains('31/12/2000');
  });


  it('Anular préstamo', () => {
    cy.visit('localhost:8080/listadoPrestamos');
    cy.getLastPrestamoID().then((ultimoID) => {
      cy.anularPrestamo();
      cy.title('Listado prestamos - Bibliotecas');
      cy.get('table tbody tr:last-child td:first-child').should('not.contain', ultimoID);
    });
  });
});
