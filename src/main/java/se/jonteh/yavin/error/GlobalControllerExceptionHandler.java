package se.jonteh.yavin.error;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

  @ResponseStatus(HttpStatus.NOT_FOUND) // 404
  @ExceptionHandler(SnippetNotFound.class)
  public HttpError handleNotFound(HttpServletRequest req, Exception e) {
    if (e instanceof SnippetNotFound)
      return (SnippetNotFound) e;
    return new HttpError("", "");
  }

}
