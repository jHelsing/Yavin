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
import se.jonteh.yavin.error.client.BadRequestException;
import se.jonteh.yavin.error.HttpErrorException;
import se.jonteh.yavin.error.client.ForbiddenException;
import se.jonteh.yavin.error.client.NotFoundException;
import se.jonteh.yavin.model.Snippet;
import se.jonteh.yavin.model.SnippetRepository;
import se.jonteh.yavin.model.User;
import se.jonteh.yavin.model.UserRepository;

@RestController
public class SnippetController {

  public static final String SNIPPETS = "/snippets/";
  @Autowired
  private SnippetRepository snippetRepo;

  @Autowired
  private UserRepository userRepo;

  @GetMapping("/snippets/{id}")
  @Cacheable
  public Snippet getSnippet(@PathVariable("id") UUID id, HttpServletResponse response)
      throws NotFoundException {
    Optional<Snippet> snippet = snippetRepo.findById(id);
    if (snippet.isEmpty()) throw new NotFoundException(SNIPPETS + id.toString(), "");
    return snippet.get();
  }

  @RolesAllowed({"user"})
  @PutMapping("/snippets")
  public void createNewSnippet(@RequestBody Snippet newSnippet, HttpServletRequest request,
                               HttpServletResponse response) throws HttpErrorException {
    // Identify if user exists in users table
    User caller;
    try {
      caller = getCaller(request.getRemoteUser());
    } catch (ForbiddenException e) {
      User newUser = new User(request.getRemoteUser());
      caller = userRepo.save(newUser);
    }

    // The sent snippet is not valid
    if (newSnippet.isValid()) throw new BadRequestException();

    // Prep the snippet
    newSnippet.setOwner(caller);
    newSnippet.setCreated(LocalDateTime.now(Clock.systemUTC()));
    snippetRepo.save(newSnippet);
    caller.addSnippet(newSnippet);
    userRepo.save(caller);
    response.setStatus(HttpServletResponse.SC_CREATED);
  }

  @RolesAllowed("user")
  @PostMapping("/snippets/{id}")
  public void updateSnippet(@RequestBody Snippet incoming, @PathVariable("id") UUID id,
                               HttpServletRequest request, HttpServletResponse response)
      throws HttpErrorException {

    User caller = getCaller(request.getRemoteUser());
    Snippet existing = getSnippetFromId(id, SNIPPETS + id.toString());

    // Check if user don't have access to modify this snippet
    if (!existing.canModify(caller)) throw new ForbiddenException();

    // Since the snippet requires updating, then update required properties
    // Maybe not correct Exception thrown here?
    if (!existing.requiresUpdate(incoming)) throw new BadRequestException();

    // Update the old snippet
    existing.update(incoming);
    snippetRepo.save(existing);

    // Status 204 No content. Request successful
    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
  }

  @RolesAllowed("user")
  @GetMapping("/snippets")
  public List<Snippet> getAllSnippets(HttpServletRequest request) throws ForbiddenException {
    User caller = getCaller(request.getRemoteUser());
    return new LinkedList<>(caller.getSnippets());
  }

  @DeleteMapping("/snippets/{id}")
  public void deleteSnippet(@PathVariable("id") UUID id, HttpServletRequest request, HttpServletResponse response)
      throws HttpErrorException {
    User caller = getCaller(request.getRemoteUser());
    Snippet existing = getSnippetFromId(id, SNIPPETS + id.toString());

    // Check if user don't have access to delete this snippet
    if (!existing.canModify(caller)) throw new ForbiddenException();

    snippetRepo.delete(existing);
    response.setStatus(HttpServletResponse.SC_OK);
  }

  /**
   * Reads a snippet from the database with the provided id.
   *
   * @param id The Snippet id that we want to fetch from the database
   * @param path The path that should be returned in the possible 404 error
   * @return The identified snippet instance
   * @throws BadRequestException Thrown if the supplied id is null
   * @throws NotFoundException Thrown if the supplied id does not match an existing snippet
   */
  private Snippet getSnippetFromId(UUID id, String path)
      throws BadRequestException, NotFoundException {
    // If the id is null the request was bad
    if (id == null) throw new BadRequestException();

    // Verify that the snippet with the provided ID exists
    Optional<Snippet> optSnippet = snippetRepo.findById(id);
    if (optSnippet.isEmpty()) throw new NotFoundException(path, "");
    return optSnippet.get();
  }

  /**
   * Checks a calling user if it exists within this service.
   *
   * @param callingUserId The UUID of the calling user (the user id from Keycloak)
   * @return The calling user if it exists
   * @throws ForbiddenException Thrown if the user is not found
   */
  private User getCaller(String callingUserId) throws ForbiddenException {
    // Verify that the user exists
    Optional<User> optionalUser = userRepo.findById(UUID.fromString(callingUserId));
    if (optionalUser.isEmpty()) throw new ForbiddenException();
    return optionalUser.get();
  }
}
