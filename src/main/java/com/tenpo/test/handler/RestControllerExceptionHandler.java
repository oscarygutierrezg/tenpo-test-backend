package com.tenpo.test.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenpo.test.dto.ApiResponseErrorDto;
import com.tenpo.test.dto.history.CalledHistoryDto;
import com.tenpo.test.model.enums.Status;
import com.tenpo.test.service.CalledHistoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
@AllArgsConstructor
public class RestControllerExceptionHandler {

	private final CalledHistoryService calledHistoryService;
	private final ObjectMapper objectMapper;
	private final HttpServletRequest httpServletRequest;


	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ApiResponseErrorDto> handleMethodConstraintViolationException(ConstraintViolationException exception){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(
						createApiResponseErrorDto(HttpStatus.BAD_REQUEST, exception.getConstraintViolations().stream()
								.map( error -> error.getPropertyPath()+" "+error.getMessage())
								.collect(Collectors.toList()))
						);

	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ApiResponseErrorDto> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception){
		log.error(exception.getMessage(),exception);
		var body = createApiResponseErrorDto(HttpStatus.BAD_REQUEST, List.of("Debe ingresar valores numéricos"));

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(body);

	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<ApiResponseErrorDto> handleException(Exception exception){
		log.error(exception.getMessage(),exception);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(
						createApiResponseErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, List.of(exception.getMessage()))
						);

	}

	private ApiResponseErrorDto createApiResponseErrorDto(HttpStatus httpStatus,List<String> errors) {
		var response = ApiResponseErrorDto.builder()
				.code(httpStatus.value())
				.message(httpStatus.getReasonPhrase())
				.errors(errors)
				.build();
		saveHistory(response,Status.FAILED);
		return response;
	}


	@Async
	public void saveHistory(ApiResponseErrorDto response, Status status) {
		try{
			calledHistoryService.create(CalledHistoryDto.builder()
					.response(objectMapper.writeValueAsString(response))
					.status(status)
					.url(httpServletRequest.getRequestURI())
					.build());
		} catch (Exception e){
			log.error(e.getMessage(),e);
		}

	}
}
