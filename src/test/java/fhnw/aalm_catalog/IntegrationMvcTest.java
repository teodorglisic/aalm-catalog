package fhnw.aalm_catalog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(BookRestController.class)
public class IntegrationMvcTest {


    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private BookRepository bookRepository;

    @Test
    void shouldReturnAllBooks() throws Exception {
        // GIVEN
        Book book1 = new Book(
                "123",
                "Clean Code",
                "Software craftsmanship",
                "Robert Martin"
        );

        Book book2 = new Book(
                "456",
                "Effective Java",
                "Java best practices",
                "Joshua Bloch"
        );

        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));

        // WHEN + THEN
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Clean Code"))
                .andExpect(jsonPath("$[1].author").value("Joshua Bloch"));
    }

    @Test
    void shouldReturnEmptyListWhenNoBooksExist() throws Exception {
        // GIVEN
        when(bookRepository.findAll()).thenReturn(List.of());

        // WHEN + THEN
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void shouldFindBookByKeywordInTitle() throws Exception {
        // GIVEN
        Book book1 = new Book(
                "123",
                "Clean Code",
                "Software craftsmanship",
                "Robert Martin"
        );

        Book book2 = new Book(
                "456",
                "Harry Potter",
                "Wizard story",
                "J. K. Rowling"
        );

        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));

        // WHEN + THEN
        mockMvc.perform(get("/api/books/search")
                        .param("keyword", "clean"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Clean Code"));
    }

    @Test
    void shouldFindBookWithMultipleKeywords() throws Exception {
        // GIVEN
        Book book = new Book(
                "123",
                "Clean Code",
                "Software craftsmanship principles",
                "Robert Martin"
        );

        Book book1 = new Book(
                "456",
                "Java Code",
                "Java craftsmanship principles",
                "Robert Reynolds"
        );

        when(bookRepository.findAll()).thenReturn(List.of(book, book1));

        // WHEN + THEN
        mockMvc.perform(get("/api/books/search")
                        .param("keyword", "code").param("keyword", "reynolds"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Java Code"))
                .andExpect(jsonPath("$[0].author").value("Robert Reynolds"));
    }

    @Test
    void shouldReturnEmptyListWhenNoBookMatchesSearch() throws Exception {
        // GIVEN
        Book book = new Book(
                "123",
                "Clean Code",
                "Software craftsmanship",
                "Robert Martin"
        );

        when(bookRepository.findAll()).thenReturn(List.of(book));

        // WHEN + THEN
        mockMvc.perform(get("/api/books/search")
                        .param("keyword", "python"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void searchShouldBeCaseInsensitive() throws Exception {
        // GIVEN
        Book book = new Book(
                "123",
                "Clean Code",
                "Software craftsmanship",
                "Robert Martin"
        );

        when(bookRepository.findAll()).thenReturn(List.of(book));

        // WHEN + THEN
        mockMvc.perform(get("/api/books/search")
                        .param("keyword", "cLeAn"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void shouldReturnBadRequestWhenKeywordIsMissing() throws Exception {
        mockMvc.perform(get("/api/books/search"))
                .andExpect(status().isBadRequest());
    }

}
