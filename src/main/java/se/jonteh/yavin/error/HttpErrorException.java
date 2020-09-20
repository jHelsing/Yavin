package se.jonteh.yavin.error;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Generic representation of an HTTP Error/exception.
 *
 * The implementation follows RFC-7807.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HttpErrorException extends Exception {

  /**
   * A URI reference that goes to a documentation notice on the error type.
   * Must be human readable.
   */
  private String type;

  /**
   * A short, human-readable summary of the problem type. It should not change
   * from occurrence to occurrence.
   */
  private String title;

  // Default constructor for Lombok.
  public HttpErrorException() {}

  public HttpErrorException(String type, String title) {
    this.type = type;
    this.title = title;
  }

}
