// cypress/support/commands.js

Cypress.Commands.add('rellenarFormularioLector', (apellidos, nombre, DNI, fechaNacimiento, numeroTelefono) => {
  cy.get('#apellidos').type(apellidos);
  cy.get('#nombre').type(nombre);
  cy.get('#DNI').type(DNI);
  cy.get('#fechaNacimiento').type(fechaNacimiento);
  cy.get('#numeroTelefono').type(numeroTelefono);
});

Cypress.Commands.add('modificarLector', (ultimoID, nuevosApellidos, nuevoNombre) => {
  cy.get('a[href="/modificarLector"]').click();
  cy.get('#codigo').type(ultimoID);
  cy.get('button[type="submit"]').click();
  cy.get('#apellidos').clear().type(nuevosApellidos);
  cy.get('#nombre').clear().type(nuevoNombre);
  cy.get('input[type="submit"]').click();
});

Cypress.Commands.add('eliminarLector', (ultimoID) => {
  cy.get('a[href="/eliminarLector"]').click();
  cy.get('#codigo').clear().type(ultimoID);
  cy.get('button[type="submit"]').click();
  cy.title('Listado lectores - Bibliotecas');
  cy.get('table tbody tr:last-child td:first-child').should('not.contain', ultimoID);
});
