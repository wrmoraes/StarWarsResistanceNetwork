package resistance.star.wars.socialnetwork.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import resistance.star.wars.socialnetwork.model.dto.ErrorResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
  @ExceptionHandler(Exception.class)
  public final ResponseEntity<Object> handleAllExceptions(Exception exception, WebRequest request) {
    ErrorResponseDTO error = new ErrorResponseDTO("Server Error", List.of(exception.getLocalizedMessage()));
    return new ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(BusinessException.class)
  public final ResponseEntity<Object> handleBusinessExceptions(BusinessException exception, WebRequest request) {
    ErrorResponseDTO error = new ErrorResponseDTO("Business Error", List.of(exception.getLocalizedMessage()));
    return new ResponseEntity(error, HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler(NotFoundException.class)
  public final ResponseEntity<Object> handleNotFoundExceptions(NotFoundException exception, WebRequest request) {
    ErrorResponseDTO error = new ErrorResponseDTO("Not Found Error", List.of(exception.getLocalizedMessage()));
    return new ResponseEntity(error, HttpStatus.NOT_FOUND);
  }

  @Override
  public final ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
    List<String> invalidFields = exception.getAllErrors()
        .stream()
        .map(objectError -> objectError.getDefaultMessage())
        .collect(Collectors.toList());

    ErrorResponseDTO error = new ErrorResponseDTO("Validation Failed", invalidFields);
    return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
  }
}
