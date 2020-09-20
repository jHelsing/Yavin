package se.jonteh.yavin.error;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import se.jonteh.yavin.error.client.BadRequestException;
import se.jonteh.yavin.error.client.ForbiddenException;
import se.jonteh.yavin.error.client.NotFoundException;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

  @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
  @ExceptionHandler(BadRequestException.class)
  public HttpErrorException handleBadRequest(HttpServletRequest req, Exception e) {
    if (e instanceof BadRequestException)
      return (BadRequestException) e;
    return new HttpErrorException("", "");
  }

  @ResponseStatus(HttpStatus.NOT_FOUND) // 404
  @ExceptionHandler(NotFoundException.class)
  public HttpErrorException handleNotFound(HttpServletRequest req, Exception e) {
    if (e instanceof NotFoundException)
      return (NotFoundException) e;
    return new HttpErrorException("", "");
  }

  @ResponseStatus(HttpStatus.FORBIDDEN) // 403
  @ExceptionHandler(ForbiddenException.class)
  public HttpErrorException handleForbiddenCaller(HttpServletRequest req, Exception e) {
    if (e instanceof ForbiddenException)
      return (ForbiddenException) e;
    return new HttpErrorException("", "");
  }

}
