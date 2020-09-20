package se.jonteh.yavin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
@Entity
public class Snippet extends RepresentationModel<Snippet> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @OneToMany
  private User owner;

  private String title;
  private String content;

  @JsonCreator
  public Snippet(@JsonProperty("title") String title,
                 @JsonProperty("content") String content) {
    this.title = title;
    this.content = content;
    this.id = UUID.randomUUID();
  }

  @JsonCreator
  public Snippet(@JsonProperty("id") String id,
                 @JsonProperty("title") String title,
                 @JsonProperty("content") String content) {
    this.id = UUID.fromString(id);
    this.title = title;
    this.content = content;
  }

  @Override
  public String toString() {
    return "{" +
        "\"id\": \"" + id.toString() + "\"" +
        " \"owner\": " + owner.toString() +
        " \"title\": \"" + title + "\"" +
        " \"content\": \"" + content + "\"" +
        "}";
  }

  @Override
  public boolean equals(Object obj) {
    // TODO Create a proper equals method
    if (obj instanceof Snippet)
      return this.getId().equals(((Snippet) obj).getId());
    return false;
  }

  @Override
  public int hashCode() {
    if (id == null) {
      return title.hashCode() + content.hashCode();
    } else {
      return id.hashCode();
    }
  }
}
