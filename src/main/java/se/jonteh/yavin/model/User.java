package se.jonteh.yavin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Data @Getter @Setter
@Entity
public class User extends RepresentationModel<User> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  /**
   * Snippets that this user has created and owns.
   */
  @JsonManagedReference
  @JsonIgnore
  @OneToMany(orphanRemoval = true)
  Set<Snippet> snippets;

  public User() {
    this.snippets = new HashSet<>();
  }

  @JsonCreator
  public User(@JsonProperty("id") String id) {
    this.id = UUID.fromString(id);
    this.snippets = new HashSet<>();
  }

  public User(UUID id){
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
  public boolean equals(Object obj) {
    if (obj instanceof User) {
      return super.equals(obj) == this.id.equals(((User) obj).getId());
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
