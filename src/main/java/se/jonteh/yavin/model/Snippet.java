package se.jonteh.yavin.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
}
