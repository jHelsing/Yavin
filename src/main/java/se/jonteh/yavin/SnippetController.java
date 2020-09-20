package se.jonteh.yavin;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
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
import se.jonteh.yavin.error.HttpError;
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

  @RolesAllowed({"user"})
  @PutMapping("/snippets")
  public void createNewSnippet(@RequestBody Snippet newSnippet, HttpServletRequest request, HttpServletResponse response) {
    validate(newSnippet);

    // Identify if user exists in users table
    UUID callingUUID = UUID.fromString(request.getRemoteUser());
    Optional<User> optUser = userRepo.findById(callingUUID);
    User caller;
    if (optUser.isEmpty()) {
      // TODO log here that we have a first-time caller
      User newUser = new User(callingUUID);
      caller = userRepo.save(newUser);
    } else {
      caller = optUser.get();
    }

    // Prep the snippet
    newSnippet.setOwner(caller);
    newSnippet.setCreated(LocalDateTime.now(Clock.systemUTC()));
    snippetRepo.save(newSnippet);
    caller.addSnippet(newSnippet);
    userRepo.save(caller);
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
  public void updateSnippet(@RequestBody Snippet incoming, @PathVariable("id") UUID id,
                               HttpServletRequest request, HttpServletResponse response) {

    // Verify that the user exists
    Optional<User> optionalUser = userRepo.findById(UUID.fromString(request.getRemoteUser()));
    if (optionalUser.isEmpty()) throw new HttpError(); // TODO the user does not exist in the service and can thus not remove a value. 403 Forbidden
    User caller = optionalUser.get();

    // Verify that the snippet with the provided ID exists
    Optional<Snippet> optSnippet = snippetRepo.findById(id);
    String instance = "/snippets/" + id.toString();
    if (optSnippet.isEmpty()) throw new SnippetNotFound(instance, "");
    Snippet existing = optSnippet.get();


    // Check if user don't have access to modify this snippet
    if (existing.canModify(caller)) {
      throw new HttpError(); // TODO fix this error 403 Forbidden
    }

    // Since the user is allowed to modify this snippet, then update required properties
    if (!existing.requiresUpdate(incoming)) throw new HttpError(); // TODO Should be 400 Bad Request

    // Update the old snippet
    existing.update(incoming);
    snippetRepo.save(existing);

    // Status 204 No content
    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
  }

  @RolesAllowed("user")
  @GetMapping("/snippets")
  public List<Snippet> getAllSnippets() {
    List<Snippet> snippets = new LinkedList<>();
    for (Snippet snippet : snippetRepo.findAll()) {
      snippets.add(snippet);
    }
    return snippets;
  }

  @DeleteMapping("/snippets/{id}")
  public void deleteSnippet(@PathVariable("id") UUID id, HttpServletRequest request, HttpServletResponse response) {
    Optional<Snippet> optionalSnippet = snippetRepo.findById(id);
    optionalSnippet.ifPresent(snippet -> {
      snippetRepo.delete(snippet);
      response.setStatus(HttpServletResponse.SC_OK);
    });
    throw new SnippetNotFound("/snippets/" + id.toString(), "");
  }
}
