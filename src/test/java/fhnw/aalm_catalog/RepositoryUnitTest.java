package fhnw.aalm_catalog;

import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class RepositoryUnitTest {

    @Autowired
    private BookRepository bookRepository;


    private static Logger log = LoggerFactory.getLogger(RepositoryUnitTest.class);



    @PostConstruct
    void init() {
        // Das Repository wird über den statischen Setter injiziert
        log.info("--- Initialisiere Datenbank mit Testdaten ---");

        // Harry Potter Reihe
        log.info("Preloading " + bookRepository.save(new Book("9780439554930", "Harry Potter and the Sorcerer's Stone", "A young wizard discovers his magical heritage.", "J.K. Rowling")));
        log.info("Preloading " + bookRepository.save(new Book("9780439064873", "Harry Potter and the Chamber of Secrets", "The second year at Hogwarts brings new dangers.", "J.K. Rowling")));
        log.info("Preloading " + bookRepository.save(new Book("9780439136365", "Harry Potter and the Prisoner of Azkaban", "Harry learns the truth about his godfather.", "J.K. Rowling")));

        // Lord of the Rings Reihe
        log.info("Preloading " + bookRepository.save(new Book("9780261103573", "The Lord of the Rings: The Fellowship of the Ring", "A quest begins to destroy the One Ring.", "J.R.R. Tolkien")));
        log.info("Preloading " + bookRepository.save(new Book("9780261102361", "The Lord of the Rings: The Two Towers", "The fellowship is broken but continues its mission.", "J.R.R. Tolkien")));
        log.info("Preloading " + bookRepository.save(new Book("9780261102378", "The Lord of the Rings: The Return of the King", "The final battle for Middle-earth.", "J.R.R. Tolkien")));

        // Stephen King Bücher
        log.info("Preloading " + bookRepository.save(new Book("9781982137274", "The Institute", "Children with special abilities are taken to a sinister facility.", "Stephen King")));
        log.info("Preloading " + bookRepository.save(new Book("9780307743657", "The Shining", "A family becomes isolated in a haunted hotel.", "Stephen King")));
        log.info("Preloading " + bookRepository.save(new Book("9781501142970", "IT", "A shape-shifting evil terrorizes a small town.", "Stephen King")));

        // Weitere Bücher
        log.info("Preloading " + bookRepository.save(new Book("9780553386790", "A Game of Thrones", "Noble families battle for power in Westeros.", "George R.R. Martin")));
        log.info("Preloading " + bookRepository.save(new Book("9780061122415", "To Kill a Mockingbird", "A lawyer defends a Black man falsely accused of a crime.", "Harper Lee")));
        log.info("Preloading " + bookRepository.save(new Book("9780553296983", "Dune", "A young noble becomes a leader on a desert planet.", "Frank Herbert")));

        log.info("--- Initialisierung abgeschlossen ---");
    }

    /**
     * Testet die Funktion findByIsbn, die ein spezifisches Buch anhand der ISBN findet.
     */
    @Test
    void testFindByIsbn() {
        log.info("Teste findByIsbn...");
        // Vorhandene ISBN
        String isbn = "9780439554930"; // Harry Potter 1
        Optional<Book> foundBook = bookRepository.findBookByIsbn(isbn);

        assertTrue(foundBook.isPresent(), "Das Buch mit ISBN " + isbn + " sollte gefunden werden.");
        assertEquals("Harry Potter and the Sorcerer's Stone", foundBook.get().getTitle());
        assertEquals("Harry Potter and the Sorcerer's Stone", foundBook.get().getTitle());
        assertEquals("A young wizard discovers his magical heritage.", foundBook.get().getDescription());
        assertEquals("J.K. Rowling", foundBook.get().getAuthor());

        // Nicht-vorhandene ISBN
        String nonExistingIsbn = "9999999999999";
        Optional<Book> notFoundBook = bookRepository.findBookByIsbn(nonExistingIsbn);
        assertFalse(notFoundBook.isPresent(), "Ein Buch mit nicht existierender ISBN sollte nicht gefunden werden.");
    }

    /**
     * Testet die Funktion findByAuthorContainingIgnoreCase, die alle Bücher eines Autors findet.
     */
    @Test
    void testFindByAuthor() {
        log.info("Teste findByAuthorContainingIgnoreCase...");
        // Suche nach Autor (J.K. Rowling sollte 3 Bücher haben)
        List<Book> rowlingBooks = bookRepository.findBookByAuthorContainingIgnoreCase("J.K. Rowling");
        assertEquals(3, rowlingBooks.size(), "Es sollten 3 Bücher von J.K. Rowling gefunden werden.");

        // Suche mit anderer Schreibweise (Stephen King sollte 3 Bücher haben)
        List<Book> kingBooks = bookRepository.findBookByAuthorContainingIgnoreCase("stephen king");
        assertEquals(3, kingBooks.size(), "Es sollten 3 Bücher von Stephen King gefunden werden.");

        // Suche mit einem Teil des Namens (Tolkien sollte 3 Bücher haben)
        List<Book> tolkienBooks = bookRepository.findBookByAuthorContainingIgnoreCase("Tolkien");
        assertEquals(3, tolkienBooks.size(), "Es sollten 3 Bücher von J.R.R. Tolkien gefunden werden.");

        // Suche nach einem nicht existierenden Autor
        List<Book> nonExistingBooks = bookRepository.findBookByAuthorContainingIgnoreCase("Max Mustermann");
        assertTrue(nonExistingBooks.isEmpty(), "Es sollten keine Bücher von 'Max Mustermann' gefunden werden.");
    }

    /**
     * Testet die Funktion findByTitleContainingIgnoreCase, die Bücher nach einem Teil des Titels findet.
     */
    @Test
    void testFindByTitleContaining() {
        log.info("Teste findByTitleContainingIgnoreCase...");
        // Suche nach Teil-Titel ("Harry Potter")
        List<Book> hpBooks = bookRepository.findBookByTitleContainingIgnoreCase("Harry Potter");
        assertEquals(3, hpBooks.size(), "Es sollten 3 'Harry Potter' Bücher gefunden werden.");

        // Suche mit anderer Schreibweise ("the ring")
        List<Book> ringBooks = bookRepository.findBookByTitleContainingIgnoreCase("the ring");
        // LOTR 1, 2, 3 + The Shining (vom Titel, falls vorhanden)
        // Anhand der init()-Daten: Lord of the Rings: The Fellowship of the Ring, The Two Towers, The Return of the King
        // -> 3 Bücher
        assertEquals(3, ringBooks.size(), "Es sollten 3 'Lord of the Rings' Bücher gefunden werden.");

        // Suche nach einem eindeutigen Titelteil
        List<Book> duneBook = bookRepository.findBookByTitleContainingIgnoreCase("Dune");
        assertEquals(1, duneBook.size(), "Es sollte genau 1 Buch mit dem Titel 'Dune' gefunden werden.");

        // Suche, die nichts findet
        List<Book> nonExistingTitle = bookRepository.findBookByTitleContainingIgnoreCase("Galactic Hitchhiker");
        assertTrue(nonExistingTitle.isEmpty(), "Es sollten keine Bücher mit diesem Titel gefunden werden.");
    }

    /**
     * Testet die Zählung aller Bücher im Repository.
     */
    @Test
    void testCountAll() {
        log.info("Teste count()...");
        // Zählen Sie die Anzahl der Bücher, die in init() eingefügt wurden (3+3+3+1+1+1=12 Bücher).
        long expectedCount = 12;
        long actualCount = bookRepository.count();
        assertEquals(expectedCount, actualCount, "Die Gesamtzahl der Bücher sollte " + expectedCount + " betragen.");
    }

}
