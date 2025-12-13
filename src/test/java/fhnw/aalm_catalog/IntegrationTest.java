package fhnw.aalm_catalog;

import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class IntegrationTest {

    @LocalServerPort
    private int port;

    private RestClient restClient = RestClient.builder().build();

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

    @Test
    void whenBooksIsCalled_thenReturnAllBooks() {
        Book[] books = restClient.get().uri("http://localhost:" + port + "/api/books").retrieve().body(Book[].class);
        assertNotNull(books);
        assertEquals(12, books.length);
    }

    @Test
    void whenBookSearchIsCalled_thenReturnSearchedBooks_Exists() {
        Book[] books = restClient.get().uri("http://localhost:" + port + "/api/books/search?keyword=Harry").retrieve().body(Book[].class);
        assertNotNull(books);
        assertEquals(3, books.length);
        for (Book book: books) {
            assertTrue(book.getTitle().toUpperCase().contains("Harry".toUpperCase()));
        }
    }

    @Test
    void whenBookSearchIsCalled_thenReturnSearchedBooksKeywords_Exists() {
        Book[] books = restClient.get().uri("http://localhost:" + port + "/api/books/search?keyword=Harry&keyword=stone").retrieve().body(Book[].class);
        assertNotNull(books);
        assertEquals(1, books.length);
        for (Book book: books) {
            assertTrue(book.getTitle().toUpperCase().contains("Harry".toUpperCase()) && book.getTitle().toUpperCase().contains("Stone".toUpperCase()));
        }
    }

    @Test
    void whenBookSearchIsCalled_thenReturnSearchedBooks_Not_Existing() {
        Book[] books = restClient.get().uri("http://localhost:" + port + "/api/books/search?keyword=Java").retrieve().body(Book[].class);
        assertNotNull(books);
        assertEquals(0, books.length);
    }

    @Test
    void whenBookByIsbnIsCalled_thenReturnSpecificBook() {
        String expectedIsbn = "9780439554930";

        Book[] book = restClient.get()
                .uri("http://localhost:" + port + "/api/books/search?keyword=" + expectedIsbn)
                .retrieve()
                .body(Book[].class);

        assertNotNull(book);
        assertEquals(expectedIsbn, book[0].getIsbn());
        assertEquals("J.K. Rowling", book[0].getAuthor());
    }

}
