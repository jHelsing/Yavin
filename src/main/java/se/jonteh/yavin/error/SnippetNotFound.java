package se.jonteh.yavin.error;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SnippetNotFound extends HttpError {

  private static final String TYPE_URI = "";
  private static final String TITLE = "Snippet not found with given id";

  /**
   * A human-readable explanation specific to this occurrence of the problem.
   */
  private String detail;

  /**
   * A reference that identifies the specific occurrence of the problem.
   */
  private String instance;

  // Default constructor for Lombok.
  public SnippetNotFound() {
    super();
  }

  public SnippetNotFound(String instance, String detail) {
    super(TYPE_URI, TITLE);
    this.instance = instance;
    this.detail = detail;
  }
}
