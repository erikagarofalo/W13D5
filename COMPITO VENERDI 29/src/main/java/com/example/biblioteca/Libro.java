package com.example.biblioteca;

public class Libro extends ElementoCatalogo {
    private final String autore;
    private final String genere;

    public Libro(String isbn, String titolo, int annoPubblicazione, int numeroPagine, String autore, String genere) {
        super(isbn, titolo, annoPubblicazione, numeroPagine);
        this.autore = autore;
        this.genere = genere;
    }

    public String getAutore() { return autore; }

    public String getGenere() { return genere; }
}