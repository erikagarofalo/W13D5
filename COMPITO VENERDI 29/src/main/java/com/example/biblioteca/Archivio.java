package com.example.biblioteca;

import java.util.*;
import java.util.stream.Collectors;

public class Archivio {
    private final List<ElementoCatalogo> catalogo;

    public Archivio() {
        this.catalogo = new ArrayList<>();
    }

    public void aggiungiElemento(ElementoCatalogo elemento) throws IllegalArgumentException {
        if (catalogo.stream().anyMatch(e -> e.getIsbn().equals(elemento.getIsbn()))) {
            throw new IllegalArgumentException("Elemento con ISBN " + elemento.getIsbn() + " già presente");
        }
        catalogo.add(elemento);
    }

    public ElementoCatalogo cercaPerISBN(String isbn) throws ElementoNonTrovatoException {
        return catalogo.stream()
                .filter(e -> e.getIsbn().equals(isbn))
                .findFirst()
                .orElseThrow(() -> new ElementoNonTrovatoException("Elemento con ISBN " + isbn + " non trovato"));
    }

    public void rimuoviPerISBN(String isbn) throws ElementoNonTrovatoException {
        ElementoCatalogo elemento = cercaPerISBN(isbn);
        catalogo.remove(elemento);
    }

    public List<ElementoCatalogo> cercaPerAnnoPubblicazione(int anno) {
        return catalogo.stream()
                .filter(e -> e.getAnnoPubblicazione() == anno)
                .collect(Collectors.toList());
    }

    public List<Libro> cercaPerAutore(String autore) {
        return catalogo.stream()
                .filter(e -> e instanceof Libro)
                .map(e -> (Libro) e)
                .filter(l -> l.getAutore().equalsIgnoreCase(autore))
                .collect(Collectors.toList());
    }

    public void aggiornaElemento(String isbn, ElementoCatalogo nuovoElemento) throws ElementoNonTrovatoException {
        ElementoCatalogo elemento = cercaPerISBN(isbn);
        int index = catalogo.indexOf(elemento);
        catalogo.set(index, nuovoElemento);
    }

    public List<Libro> cercaPerGenere(String genere) {
        return catalogo.stream()
                .filter(e -> e instanceof Libro)
                .map(e -> (Libro) e)
                .filter(l -> l.getGenere().equalsIgnoreCase(genere))
                .collect(Collectors.toList());
    }

    public List<ElementoCatalogo> cercaPerRangeAnni(int annoInizio, int annoFine) {
        return catalogo.stream()
                .filter(e -> e.getAnnoPubblicazione() >= annoInizio &&
                        e.getAnnoPubblicazione() <= annoFine)
                .collect(Collectors.toList());
    }

    public List<Rivista> cercaPerPeriodicita(Periodicita periodicita) {
        return catalogo.stream()
                .filter(e -> e instanceof Rivista)
                .map(e -> (Rivista) e)
                .filter(r -> r.getPeriodicita() == periodicita)
                .collect(Collectors.toList());
    }

    public List<Libro> ricercaCombinata(String autore, int anno) {
        return catalogo.stream()
                .filter(e -> e instanceof Libro)
                .map(e -> (Libro) e)
                .filter(l -> l.getAutore().equalsIgnoreCase(autore) &&
                        l.getAnnoPubblicazione() == anno)
                .collect(Collectors.toList());
    }

    public void stampaStatistiche() {
        long numLibri = catalogo.stream().filter(e -> e instanceof Libro).count();
        long numRiviste = catalogo.stream().filter(e -> e instanceof Rivista).count();

        ElementoCatalogo elementoMaxPagine = catalogo.stream()
                .max(Comparator.comparingInt(ElementoCatalogo::getNumeroPagine))
                .orElse(null);

        double mediaPagine = catalogo.stream()
                .mapToInt(ElementoCatalogo::getNumeroPagine)
                .average()
                .orElse(0.0);

        System.out.println("Statistiche catalogo:");
        System.out.println("Numero totale libri: " + numLibri);
        System.out.println("Numero totale riviste: " + numRiviste);
        if (elementoMaxPagine != null) {
            System.out.println("Elemento con più pagine: " + elementoMaxPagine.getTitolo() +
                    " (" + elementoMaxPagine.getNumeroPagine() + " pagine)");
        }
        System.out.println("Media pagine: " + String.format("%.2f", mediaPagine));
    }

    public void stampaStatisticheAvanzate() {
        // Genere più presente
        Map<String, Long> generiCount = catalogo.stream()
                .filter(e -> e instanceof Libro)
                .map(e -> (Libro) e)
                .collect(Collectors.groupingBy(
                        Libro::getGenere,
                        Collectors.counting()
                ));

        String generePiuPresente = generiCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Nessun genere trovato");

        // Autore con più libri
        Map<String, Long> autoriCount = catalogo.stream()
                .filter(e -> e instanceof Libro)
                .map(e -> (Libro) e)
                .collect(Collectors.groupingBy(
                        Libro::getAutore,
                        Collectors.counting()
                ));

        String autorePiuPresente = autoriCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Nessun autore trovato");

        // Media pagine per tipo
        double mediaPagineLibri = catalogo.stream()
                .filter(e -> e instanceof Libro)
                .mapToInt(ElementoCatalogo::getNumeroPagine)
                .average()
                .orElse(0.0);

        double mediaPagineRiviste = catalogo.stream()
                .filter(e -> e instanceof Rivista)
                .mapToInt(ElementoCatalogo::getNumeroPagine)
                .average()
                .orElse(0.0);

        // Stampa risultati
        System.out.println("\nStatistiche Avanzate:");
        System.out.println("Genere più presente: " + generePiuPresente);
        System.out.println("Autore con più libri: " + autorePiuPresente);
        System.out.println("Media pagine libri: " + String.format("%.2f", mediaPagineLibri));
        System.out.println("Media pagine riviste: " + String.format("%.2f", mediaPagineRiviste));

        // Distribuzione pubblicazioni per anno
        System.out.println("\nDistribuzione pubblicazioni per anno:");
        Map<Integer, Long> pubblicazioniPerAnno = catalogo.stream()
                .collect(Collectors.groupingBy(
                        ElementoCatalogo::getAnnoPubblicazione,
                        Collectors.counting()
                ));

        pubblicazioniPerAnno.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    System.out.print(entry.getKey() + ": ");
                    for (int i = 0; i < entry.getValue(); i++) {
                        System.out.print("*");
                    }
                    System.out.println(" (" + entry.getValue() + ")");
                });
    }
}