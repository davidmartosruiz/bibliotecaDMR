Cypress.Commands.add('rellenarFormularioLibro', (nombre, autor, isbn, anyoPublicacion, categoria) => {
  if (nombre) {
    cy.get('#nombre').type(nombre);
  }

  if (autor) {
    cy.get('#autor').type(autor);
  }

  if (isbn) {
    cy.get('#isbn').type(isbn);
  }

  if (anyoPublicacion) {
    cy.get('#anyoPublicacion').type(anyoPublicacion);
  }

  if (categoria) {
    cy.get('#categoria').type(categoria);
  }
});


Cypress.Commands.add('verificarLibroEnTabla', (nombre, autor, isbn, anyoPublicacion, categoria) => {
  cy.get('table tbody tr').should('have.length.greaterThan', 0);
  cy.contains(nombre);
  cy.contains(autor);
  cy.contains(isbn);
  cy.contains(anyoPublicacion);
  cy.contains(categoria);
});

Cypress.Commands.add('modificarLibro', (idLibro, nuevoNombre, nuevoAutor, nuevoISBN, nuevoAnyoPublicacion, nuevaCategoria) => {
  cy.visit('localhost:8080');
  cy.get('a[href="/modificarLibro"]').click();
  cy.get('#codigo').clear().type(idLibro);
  cy.get('button[type="submit"]').click();

  if (nuevoNombre) {
    cy.get('#nombre').clear().type(nuevoNombre);
  }

  if (nuevoAutor) {
    cy.get('#autor').clear().type(nuevoAutor);
  }

  if (nuevoISBN) {
    cy.get('#isbn').clear().type(nuevoISBN);
  }

  if (nuevoAnyoPublicacion) {
    cy.get('#anyoPublicacion').clear().type(nuevoAnyoPublicacion);
  }

  if (nuevaCategoria) {
    cy.get('#categoria').clear().type(nuevaCategoria);
  }

  cy.get('input[type="submit"]').click();
  cy.title('Listado libros - Bibliotecas');
  cy.get('table tbody tr:last-child').should('contain', idLibro);

  const nuevosDatos = [nuevoNombre, nuevoAutor, nuevoISBN, nuevoAnyoPublicacion, nuevaCategoria];

  nuevosDatos.forEach((valor) => {
    if (valor) {
      cy.get('table tbody').should('contain', valor);
    }
  });
});



Cypress.Commands.add('eliminarLibro', (idLibro) => {
  cy.visit('localhost:8080');
  cy.get('a[href="/eliminarLibro"]').click();
  cy.get('#codigo').clear().type(idLibro);
  cy.get('button[type="submit"]').click();
  cy.title('Listado libros - Bibliotecas');
  cy.get('table tbody tr:last-child td:first-child').should('not.contain', idLibro);
});
