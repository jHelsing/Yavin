package se.jonteh.yavin.error.client;

import lombok.Data;
import lombok.EqualsAndHashCode;
import se.jonteh.yavin.error.HttpErrorException;

@EqualsAndHashCode(callSuper = true)
@Data
public class NotFoundException extends HttpErrorException {

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
  public NotFoundException() {
    super();
  }

  public NotFoundException(String instance, String detail) {
    super(TYPE_URI, TITLE);
    this.instance = instance;
    this.detail = detail;
  }
}
