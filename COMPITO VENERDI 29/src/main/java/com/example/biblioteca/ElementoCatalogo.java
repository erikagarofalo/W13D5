package com.example.biblioteca;

public abstract class ElementoCatalogo {
    private final String isbn;
    private String titolo;
    private int annoPubblicazione;
    private int numeroPagine;

    public ElementoCatalogo(String isbn, String titolo, int annoPubblicazione, int numeroPagine) {
        validaISBN(isbn);
        validaTitolo(titolo);
        validaAnno(annoPubblicazione);
        validaNumeroPagine(numeroPagine);

        this.isbn = isbn;
        this.titolo = titolo;
        this.annoPubblicazione = annoPubblicazione;
        this.numeroPagine = numeroPagine;
    }

    private void validaISBN(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN non può essere vuoto");
        }
        // Formato ISBN-13: 13 cifre
        String isbnPulito = isbn.replace("-", "").replace(" ", "");
        if (!isbnPulito.matches("\\d{13}")) {
            throw new IllegalArgumentException("ISBN deve essere composto da 13 cifre");
        }
    }

    private void validaTitolo(String titolo) {
        if (titolo == null || titolo.trim().isEmpty()) {
            throw new IllegalArgumentException("Titolo non può essere vuoto");
        }
        if (titolo.length() < 2) {
            throw new IllegalArgumentException("Titolo troppo corto (minimo 2 caratteri)");
        }
        if (titolo.length() > 100) {
            throw new IllegalArgumentException("Titolo troppo lungo (massimo 100 caratteri)");
        }
    }

    private void validaAnno(int anno) {
        int annoCorrente = java.time.Year.now().getValue();
        if (anno < 1000) {
            throw new IllegalArgumentException("Anno non valido (deve essere maggiore di 1000)");
        }
        if (anno > annoCorrente) {
            throw new IllegalArgumentException("Anno non può essere nel futuro");
        }
    }

    private void validaNumeroPagine(int numeroPagine) {
        if (numeroPagine <= 0) {
            throw new IllegalArgumentException("Il numero di pagine deve essere positivo");
        }
        if (numeroPagine > 5000) {
            throw new IllegalArgumentException("Numero di pagine non realistico (max 5000)");
        }
    }

    // Getters
    public String getIsbn() { return isbn; }
    public String getTitolo() { return titolo; }
    public int getAnnoPubblicazione() { return annoPubblicazione; }
    public int getNumeroPagine() { return numeroPagine; }

    // Setters con annotazioni
    @SuppressWarnings("unused")
    public void setTitolo(String titolo) {
        validaTitolo(titolo);
        this.titolo = titolo;
    }

    @SuppressWarnings("unused")
    public void setAnnoPubblicazione(int annoPubblicazione) {
        validaAnno(annoPubblicazione);
        this.annoPubblicazione = annoPubblicazione;
    }

    @SuppressWarnings("unused")
    public void setNumeroPagine(int numeroPagine) {
        validaNumeroPagine(numeroPagine);
        this.numeroPagine = numeroPagine;
    }
}