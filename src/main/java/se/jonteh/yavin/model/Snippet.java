package se.jonteh.yavin.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Data @Getter @Setter
@Entity
public class Snippet extends RepresentationModel<Snippet> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @ManyToOne
  @JsonBackReference
  private User owner;

  private String title;
  private String content;
  private LocalDateTime created;
  private LocalDateTime modified;

  public Snippet() {super();}

  @JsonCreator
  public Snippet(@JsonProperty("title") String title,
                 @JsonProperty("content") String content) {
    this.title = title;
    this.content = content;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    if (!super.equals(obj)) {
      return false;
    }
    Snippet snippet = (Snippet) obj;
    return Objects.equals(id, snippet.id) &&
        Objects.equals(title, snippet.title) &&
        Objects.equals(content, snippet.content) && super.equals(obj);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, content, title);
  }

  /**
   * Determines whether or not a user is authorized to act on this snippet.
   * @param caller The calling user. The user that want to modify this snippet.
   * @return True is the calling user owns this snippet.
   */
  public boolean canModify(User caller) {
    return caller.getId().equals(this.getOwner().getId());
  }

  /**
   * Determines if this snippet needs to be updated.
   * @param newSnippet The snippet that shall be compared to this instance
   * @return True if title or content differs between the two
   */
  public boolean requiresUpdate(Snippet newSnippet) {
    if (!this.getTitle().equals(newSnippet.getTitle()))
      return true;
    return !this.getContent().equals(newSnippet.getContent());
  }

  public void update(Snippet newSnippet) {
    this.setTitle(newSnippet.getTitle());
    this.setContent(newSnippet.getContent());
    this.setModified(LocalDateTime.now(Clock.systemUTC()));
  }
}
