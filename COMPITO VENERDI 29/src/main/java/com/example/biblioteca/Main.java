package com.example.biblioteca;

import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Archivio archivio = new Archivio();

    public static void main(String[] args) {
        boolean continua = true;
        while (continua) {
            stampaMenu();
            int scelta = leggiIntero("Inserisci la tua scelta: ");

            try {
                switch (scelta) {
                    case 1 -> aggiungiElemento();
                    case 2 -> cercaPerISBN();
                    case 3 -> rimuoviElemento();
                    case 4 -> cercaPerAnno();
                    case 5 -> cercaPerAutore();
                    case 6 -> aggiornaElemento();
                    case 7 -> archivio.stampaStatistiche();
                    case 8 -> cercaPerGenere();
                    case 9 -> cercaPerRangeAnni();
                    case 10 -> cercaPerPeriodicita();
                    case 11 -> ricercaCombinata();
                    case 12 -> archivio.stampaStatisticheAvanzate();
                    case 13 -> continua = false;
                    default -> System.out.println("Scelta non valida!");
                }
            } catch (Exception e) {
                System.out.println("Errore: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void stampaMenu() {
        System.out.println("\n=== MENU ===");
        System.out.println("1. Aggiungi elemento");
        System.out.println("2. Cerca per ISBN");
        System.out.println("3. Rimuovi elemento");
        System.out.println("4. Cerca per anno");
        System.out.println("5. Cerca per autore");
        System.out.println("6. Aggiorna elemento");
        System.out.println("7. Visualizza statistiche");
        System.out.println("8. Cerca per genere");
        System.out.println("9. Cerca per range di anni");
        System.out.println("10. Cerca per periodicità");
        System.out.println("11. Ricerca combinata (autore e anno)");
        System.out.println("12. Statistiche avanzate");
        System.out.println("13. Esci");
    }

    private static void aggiungiElemento() {
        System.out.println("Tipo di elemento (1=Libro, 2=Rivista): ");
        int tipo = leggiIntero("");

        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Titolo: ");
        String titolo = scanner.nextLine();
        int anno = leggiIntero("Anno pubblicazione: ");
        int pagine = leggiIntero("Numero pagine: ");

        try {
            if (tipo == 1) {
                System.out.print("Autore: ");
                String autore = scanner.nextLine();
                System.out.print("Genere: ");
                String genere = scanner.nextLine();
                archivio.aggiungiElemento(new Libro(isbn, titolo, anno, pagine, autore, genere));
            } else if (tipo == 2) {
                System.out.println("Periodicità (1=SETTIMANALE, 2=MENSILE, 3=SEMESTRALE): ");
                int per = leggiIntero("");
                Periodicita periodicita = switch (per) {
                    case 1 -> Periodicita.SETTIMANALE;
                    case 2 -> Periodicita.MENSILE;
                    case 3 -> Periodicita.SEMESTRALE;
                    default -> throw new IllegalArgumentException("Periodicità non valida");
                };
                archivio.aggiungiElemento(new Rivista(isbn, titolo, anno, pagine, periodicita));
            }
            System.out.println("Elemento aggiunto con successo!");
        } catch (IllegalArgumentException e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }

    private static void cercaPerISBN() {
        System.out.print("Inserisci ISBN: ");
        String isbn = scanner.nextLine();
        try {
            ElementoCatalogo elemento = archivio.cercaPerISBN(isbn);
            System.out.println("Elemento trovato: " + elemento.getTitolo());
        } catch (ElementoNonTrovatoException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void rimuoviElemento() {
        System.out.print("Inserisci ISBN dell'elemento da rimuovere: ");
        String isbn = scanner.nextLine();
        try {
            archivio.rimuoviPerISBN(isbn);
            System.out.println("Elemento rimosso con successo!");
        } catch (ElementoNonTrovatoException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void cercaPerAnno() {
        int anno = leggiIntero("Inserisci anno di pubblicazione: ");
        var elementi = archivio.cercaPerAnnoPubblicazione(anno);
        if (elementi.isEmpty()) {
            System.out.println("Nessun elemento trovato per l'anno " + anno);
        } else {
            System.out.println("Elementi trovati:");
            elementi.forEach(e -> System.out.println("- " + e.getTitolo()));
        }
    }

    private static void cercaPerAutore() {
        System.out.print("Inserisci nome autore: ");
        String autore = scanner.nextLine();
        var libri = archivio.cercaPerAutore(autore);
        if (libri.isEmpty()) {
            System.out.println("Nessun libro trovato per l'autore " + autore);
        } else {
            System.out.println("Libri trovati:");
            libri.forEach(l -> System.out.println("- " + l.getTitolo()));
        }
    }

    private static void cercaPerGenere() {
        System.out.print("Inserisci genere: ");
        String genere = scanner.nextLine();
        var libri = archivio.cercaPerGenere(genere);
        if (libri.isEmpty()) {
            System.out.println("Nessun libro trovato per il genere " + genere);
        } else {
            System.out.println("Libri trovati:");
            libri.forEach(l -> System.out.println("- " + l.getTitolo()));
        }
    }

    private static void cercaPerRangeAnni() {
        int annoInizio = leggiIntero("Inserisci anno iniziale: ");
        int annoFine = leggiIntero("Inserisci anno finale: ");
        var elementi = archivio.cercaPerRangeAnni(annoInizio, annoFine);
        if (elementi.isEmpty()) {
            System.out.println("Nessun elemento trovato nel periodo specificato");
        } else {
            System.out.println("Elementi trovati:");
            elementi.forEach(e -> System.out.println("- " + e.getTitolo() + " (" + e.getAnnoPubblicazione() + ")"));
        }
    }

    private static void cercaPerPeriodicita() {
        System.out.println("Periodicità (1=SETTIMANALE, 2=MENSILE, 3=SEMESTRALE): ");
        int scelta = leggiIntero("");
        Periodicita periodicita = switch (scelta) {
            case 1 -> Periodicita.SETTIMANALE;
            case 2 -> Periodicita.MENSILE;
            case 3 -> Periodicita.SEMESTRALE;
            default -> throw new IllegalArgumentException("Periodicità non valida");
        };

        var riviste = archivio.cercaPerPeriodicita(periodicita);
        if (riviste.isEmpty()) {
            System.out.println("Nessuna rivista trovata con periodicità " + periodicita);
        } else {
            System.out.println("Riviste trovate:");
            riviste.forEach(r -> System.out.println("- " + r.getTitolo()));
        }
    }

    private static void ricercaCombinata() {
        System.out.print("Inserisci nome autore: ");
        String autore = scanner.nextLine();
        int anno = leggiIntero("Inserisci anno: ");

        var libri = archivio.ricercaCombinata(autore, anno);
        if (libri.isEmpty()) {
            System.out.println("Nessun libro trovato per l'autore " + autore + " nell'anno " + anno);
        } else {
            System.out.println("Libri trovati:");
            libri.forEach(l -> System.out.println("- " + l.getTitolo()));
        }
    }

    private static void aggiornaElemento() throws ElementoNonTrovatoException {
        System.out.print("Inserisci ISBN dell'elemento da aggiornare: ");
        String isbn = scanner.nextLine();

        ElementoCatalogo vecchioElemento = archivio.cercaPerISBN(isbn);

        System.out.println("Inserisci i nuovi dati:");
        System.out.print("Titolo: ");
        String titolo = scanner.nextLine();
        int anno = leggiIntero("Anno pubblicazione: ");
        int pagine = leggiIntero("Numero pagine: ");

        if (vecchioElemento instanceof Libro) {
            System.out.print("Autore: ");
            String autore = scanner.nextLine();
            System.out.print("Genere: ");
            String genere = scanner.nextLine();
            archivio.aggiornaElemento(isbn, new Libro(isbn, titolo, anno, pagine, autore, genere));
        } else if (vecchioElemento instanceof Rivista) {
            System.out.println("Periodicità (1=SETTIMANALE, 2=MENSILE, 3=SEMESTRALE): ");
            int per = leggiIntero("");
            Periodicita periodicita = switch (per) {
                case 1 -> Periodicita.SETTIMANALE;
                case 2 -> Periodicita.MENSILE;
                case 3 -> Periodicita.SEMESTRALE;
                default -> throw new IllegalArgumentException("Periodicità non valida");
            };
            archivio.aggiornaElemento(isbn, new Rivista(isbn, titolo, anno, pagine, periodicita));
        }
        System.out.println("Elemento aggiornato con successo!");
    }

    private static int leggiIntero(String messaggio) {
        System.out.print(messaggio);
        while (true) {
            try {
                String input = scanner.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Inserisci un numero valido: ");
            }
        }
    }
}