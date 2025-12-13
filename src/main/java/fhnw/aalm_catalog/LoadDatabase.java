package fhnw.aalm_catalog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class LoadDatabase implements CommandLineRunner {

    private final BookRepository bookRepository;

    public LoadDatabase(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    private static final Logger log = LoggerFactory.getLogger((LoadDatabase.class));
    @Override
    public void run(String... args) throws Exception {
        log.info("Preloading " + this.bookRepository.save(new Book("9780439554930", "Harry Potter and the Sorcerer's Stone", "A young wizard discovers his magical heritage.", "J.K. Rowling")));
        log.info("Preloading " + this.bookRepository.save(new Book("9780439064873", "Harry Potter and the Chamber of Secrets", "The second year at Hogwarts brings new dangers.", "J.K. Rowling")));
        log.info("Preloading " + this.bookRepository.save(new Book("9780439136365", "Harry Potter and the Prisoner of Azkaban", "Harry learns the truth about his godfather.", "J.K. Rowling")));

        log.info("Preloading " + this.bookRepository.save(new Book("9780261103573", "The Lord of the Rings: The Fellowship of the Ring", "A quest begins to destroy the One Ring.", "J.R.R. Tolkien")));
        log.info("Preloading " + this.bookRepository.save(new Book("9780261102361", "The Lord of the Rings: The Two Towers", "The fellowship is broken but continues its mission.", "J.R.R. Tolkien")));
        log.info("Preloading " + this.bookRepository.save(new Book("9780261102378", "The Lord of the Rings: The Return of the King", "The final battle for Middle-earth.", "J.R.R. Tolkien")));

        log.info("Preloading " + this.bookRepository.save(new Book("9780553386790", "A Game of Thrones", "Noble families battle for power in Westeros.", "George R.R. Martin")));
        log.info("Preloading " + this.bookRepository.save(new Book("9780553579902", "A Clash of Kings", "The Seven Kingdoms descend into war.", "George R.R. Martin")));
        log.info("Preloading " + this.bookRepository.save(new Book("9780553573429", "A Storm of Swords", "Alliances shift as the war intensifies.", "George R.R. Martin")));

        log.info("Preloading " + this.bookRepository.save(new Book("9781982137274", "The Institute", "Children with special abilities are taken to a sinister facility.", "Stephen King")));
        log.info("Preloading " + this.bookRepository.save(new Book("9780307743657", "The Shining", "A family becomes isolated in a haunted hotel.", "Stephen King")));
        log.info("Preloading " + this.bookRepository.save(new Book("9781501142970", "IT", "A shape-shifting evil terrorizes a small town.", "Stephen King")));

        log.info("Preloading " + this.bookRepository.save(new Book("9780143127550", "The Alchemist", "A shepherd journeys to find his destiny.", "Paulo Coelho")));
        log.info("Preloading " + this.bookRepository.save(new Book("9780553296983", "Dune", "A young noble becomes a leader on a desert planet.", "Frank Herbert")));
        log.info("Preloading " + this.bookRepository.save(new Book("9780142403870", "The Giver", "A boy uncovers the dark secrets of his utopian society.", "Lois Lowry")));

        log.info("Preloading " + this.bookRepository.save(new Book("9780061122415", "To Kill a Mockingbird", "A lawyer defends a Black man falsely accused of a crime.", "Harper Lee")));
        log.info("Preloading " + this.bookRepository.save(new Book("9780743273565", "The Great Gatsby", "A mysterious millionaire and the pursuit of the American Dream.", "F. Scott Fitzgerald")));
        log.info("Preloading " + this.bookRepository.save(new Book("9780307474278", "The Road", "A father and son struggle to survive in a post-apocalyptic world.", "Cormac McCarthy")));

        log.info("Preloading " + this.bookRepository.save(new Book("9780441172719", "Do Androids Dream of Electric Sheep?", "A bounty hunter tracks down rogue androids.", "Philip K. Dick")));
        log.info("Preloading " + this.bookRepository.save(new Book("9780307387893", "The Girl with the Dragon Tattoo", "A journalist and hacker investigate a family's darkest secrets.", "Stieg Larsson")));

    }
}
