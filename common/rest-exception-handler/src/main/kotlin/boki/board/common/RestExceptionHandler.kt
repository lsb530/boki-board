package boki.board.common

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class RestExceptionHandler {

    private val log = LoggerFactory.getLogger(RestExceptionHandler::class.java)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(
        ex: MethodArgumentNotValidException, request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        val errors = mutableMapOf<String, String>()
        ex.bindingResult.allErrors.forEach { error ->
            val fieldName = (error as FieldError).field
            val errorMessage = error.defaultMessage
            errors[fieldName] = errorMessage ?: "Not Exception Message"
        }
        val customMsg = errors.entries.joinToString(separator = ", ") {
            "${it.key}=${it.value}"
        }
        return createErrorResponse(
            type = "Invalid_Request",
            status = HttpStatus.BAD_REQUEST,
            ex = ex,
            request = request,
            customMsg = customMsg,
        )
    }

    private fun createErrorResponse(
        type: String,
        status: HttpStatus,
        ex: Exception,
        request: HttpServletRequest,
        customMsg: String? = null,
    ): ResponseEntity<ErrorResponse> {
        log.error(
            """==== Error occurred ====
           Exception: ${ex::class.java},
           Type: ${type},
           Path: ${request.requestURI},
           Method: ${request.method},
           Message: ${customMsg ?: ex.message},
           trace: ${ex.stackTrace[0]}
           """.trimIndent(),
        )
        val errorResponse = ErrorResponse(
            type = type,
            status = status.value(),
            message = customMsg ?: ex.message,
            path = request.requestURI,
            method = request.method,
        )
        return ResponseEntity(errorResponse, status)
    }
}