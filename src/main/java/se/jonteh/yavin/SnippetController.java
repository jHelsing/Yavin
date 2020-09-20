package se.jonteh.yavin;

import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se.jonteh.yavin.error.SnippetNotFound;
import se.jonteh.yavin.model.Snippet;
import se.jonteh.yavin.model.SnippetRepository;
import se.jonteh.yavin.model.User;
import se.jonteh.yavin.model.UserRepository;

@RestController
public class SnippetController {

  @Autowired
  private SnippetRepository snippetRepo;

  @Autowired
  private UserRepository userRepo;

  @GetMapping("/snippets/{id}")
  @Cacheable
  public Snippet getSnippet(@PathVariable("id") UUID id, HttpServletResponse response) {
    Optional<Snippet> snippet = snippetRepo.findById(id);
    if (snippet.isEmpty()) throw new SnippetNotFound("/snippets/" + id.toString(), "");
    return snippet.get();
  }

  @PutMapping("/snippets")
  public void createNewSnippet(@RequestBody Snippet newSnippet, HttpServletResponse response) {
    validate(newSnippet);
    // TODO solve how we should handle users in the snippets repo. How are they added?
    //  How do we check they are correct?
    //  Maybe helps if we check how to handle auth?
    // Prep the snippet
    newSnippet.setOwner(new User(UUID.randomUUID()));
    response.setStatus(HttpServletResponse.SC_CREATED);
  }

  /**
   * Check that the supplied snippet is valid
   * @param snippet
   */
  private void validate(Snippet snippet) {
    // TODO how should we validate the contents of a snippet?
  }

  @PostMapping("/snippets/{id}")
  public Snippet updateSnippet(@RequestBody Snippet incoming, @PathVariable("id") UUID id) {
    Optional<Snippet> optSnippet = snippetRepo.findById(id);
    String instance = "/snippets/" + id.toString();
    if (optSnippet.isEmpty()) throw new SnippetNotFound(instance, "");

    snippetRepo.save(incoming);

    // TODO check what should be returned from an update request
    return null;
  }

  @DeleteMapping("/snippets/{id}")
  public void deleteSnippet(@PathVariable("id") UUID id, HttpServletResponse response) {
    Optional<Snippet> optionalSnippet = snippetRepo.findById(id);
    optionalSnippet.ifPresent(snippet -> {
      snippetRepo.delete(snippet);
      response.setStatus(HttpServletResponse.SC_OK);
    });
    throw new SnippetNotFound("/snippets/" + id.toString(), "");
  }
}
