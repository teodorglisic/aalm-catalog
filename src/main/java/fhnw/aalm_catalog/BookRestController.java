package fhnw.aalm_catalog;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController()
@RequestMapping("/api")
public class BookRestController {

    private BookRepository bookRepository;

    public BookRestController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping(path = "/books", produces = "application/json")
    public List<Book> allBooks() {
        return this.bookRepository.findAll();
    }

    @GetMapping(path = "/books/search", produces = "application/json")
    public List<Book> searchBooks(@RequestParam(name = "keyword") String query) {
        String[] querySplit = query.toUpperCase().split(",");
        List<Book> results = new ArrayList<>();


        for (Book b: this.bookRepository.findAll()) {

            boolean matchesAll = true;

            for (String keyword: querySplit) {
                boolean matchesOne =
                        b.getAuthor().toUpperCase().contains(keyword) ||
                        b.getTitle().toUpperCase().contains(keyword) ||
                        b.getDescription().toUpperCase().contains(keyword) ||
                        b.getIsbn().contains(keyword);

                if (!matchesOne) {
                    matchesAll = false;
                    break;
                }
            }

            if (matchesAll) results.add(b);
        }
        return results;
    }
}
