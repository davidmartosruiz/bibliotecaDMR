// commands.js

Cypress.Commands.add('prestarLibro', (codigoLibro, codigoLector, fechaInicio) => {
  cy.visit('localhost:8080/prestarPrestamo');
  cy.get('#codigoLibro').type(codigoLibro);
  cy.get('#codigoLector').type(codigoLector);
  cy.get('#fechaInicio').type(fechaInicio);
  cy.get('input[type="submit"]').click();
});

Cypress.Commands.add('devolverPrestamo', (fechaFin) => {
  cy.visit('localhost:8080/listadoPrestamos');
  cy.getLastPrestamoID().then((ultimoID) => {
    cy.visit('localhost:8080/devolverPrestamo');
    cy.get('#codigoPrestamo').type(ultimoID);
    cy.get('#fechaFin').type(fechaFin);
    cy.get('button[type="submit"]').click();
  });
});

Cypress.Commands.add('anularPrestamo', () => {
  cy.getLastPrestamoID().then((ultimoID) => {
    cy.visit('localhost:8080/anularPrestamo');
    cy.get('#codigo').type(ultimoID);
    cy.get('button[type="submit"]').click();
  });
});

Cypress.Commands.add('getLastPrestamoID', () => {
  cy.get('table tbody tr:last-child td:first-child').invoke('text');
});
