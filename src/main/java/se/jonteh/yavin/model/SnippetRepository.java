package se.jonteh.yavin.model;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface SnippetRepository extends CrudRepository<Snippet, UUID> {
}
