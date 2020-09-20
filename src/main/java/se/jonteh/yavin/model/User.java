package se.jonteh.yavin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class User extends RepresentationModel<User> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  private UUID keycloakId;

  private String userName;

  /**
   * Snippets that this user has created and owns.
   */
  @OneToMany(orphanRemoval = true)
  Set<Snippet> snippets;

  public User() {}

  @JsonCreator
  public User(@JsonProperty("id") String id) {
    this.id = UUID.fromString(id);
    this.snippets = new HashSet<>();
  }

  @JsonCreator
  public User(@JsonProperty("id") UUID id) {
    this.id = id;
    this.snippets = new HashSet<>();
  }

  public void addSnippet(Snippet snippet) {
    this.snippets.add(snippet);
  }

  public void removeSnippet(Snippet snippet) {
    this.snippets.remove(snippet);
  }

  @Override
  public String toString() {
    return "{\"id\": \"" + id.toString() + "\"}";
  }
}
