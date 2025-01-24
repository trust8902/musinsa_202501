package com.musinsa.shop.common.advice

import com.musinsa.shop.common.exception.BrandResponseException
import com.musinsa.shop.common.exception.CategoryResponseException
import com.musinsa.shop.common.exception.ProductResponseException
import com.musinsa.shop.common.response.ApiResponse
import org.hibernate.exception.ConstraintViolationException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.MethodParameter
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpResponse
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@RestControllerAdvice
class ApiResponseWrapper : ResponseBodyAdvice<Any> {

    private val logger: Logger = LoggerFactory.getLogger(ApiResponseWrapper::class.java)

    // 적용할 컨트롤러 응답 조건 설정
    override fun supports(
        returnType: MethodParameter,
        converterType: Class<out HttpMessageConverter<*>>
    ): Boolean = true

    // 응답 래핑 처리
    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any? {
        val servletResponse = (response as? ServletServerHttpResponse)?.servletResponse
        val statusCode = servletResponse?.status ?: HttpStatus.OK.value()

        if (body is ApiResponse<*>) {
            return body
        }

        return ApiResponse(
            statusCode = statusCode,
            data = body
        )
    }

    @ExceptionHandler(value = [
        Exception::class,
        NoSuchElementException::class,
        IllegalArgumentException::class,
        ConstraintViolationException::class,
    ])
    fun handleGenericException(e: Exception, request: WebRequest): ResponseEntity<ApiResponse<ErrorDetails>> {
        val httpStatus = when (e) {
            is NoSuchElementException -> HttpStatus.NOT_FOUND
            is IllegalArgumentException -> HttpStatus.BAD_REQUEST
            is ConstraintViolationException -> HttpStatus.BAD_REQUEST
            else -> HttpStatus.INTERNAL_SERVER_ERROR
        }

        return buildErrorResponse(e, httpStatus)
    }

    @ExceptionHandler(CategoryResponseException::class)
    fun handleCategoryResponseException(e: CategoryResponseException, request: WebRequest): ResponseEntity<ApiResponse<ErrorDetails>> {
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST, e.status.id)
    }

    @ExceptionHandler(BrandResponseException::class)
    fun handleBrandResponseException(e: BrandResponseException, request: WebRequest): ResponseEntity<ApiResponse<ErrorDetails>> {
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST, e.status.id)
    }

    @ExceptionHandler(ProductResponseException::class)
    fun handleProductResponseException(e: ProductResponseException, request: WebRequest): ResponseEntity<ApiResponse<ErrorDetails>> {
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST, e.status.id)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(
        e: MethodArgumentNotValidException,
        request: WebRequest
    ): ResponseEntity<ApiResponse<ErrorDetails>> {
        val fieldErrors = e.bindingResult.allErrors
            .filterIsInstance<FieldError>().associate {
            it.field to (it.defaultMessage ?: "Invalid value")
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ApiResponse(
                statusCode = HttpStatus.BAD_REQUEST.value(),
                data = ErrorDetails(
                    error = "Validation Error",
                    message = "Validation faild for request",
                    fieldErrors = fieldErrors,
                )
            )
        )
    }

    private fun buildErrorResponse(
        e: Exception,
        httpStatus: HttpStatus,
        customErrorId: Int? = null
    ): ResponseEntity<ApiResponse<ErrorDetails>> {
        logger.error("Exception: ${e.message}", e)

        val errorDetails = ErrorDetails(
            error = (customErrorId ?: httpStatus.reasonPhrase).toString(),
            message = e.message ?: "Unexpected error occurred",
        )

        return ResponseEntity.status(httpStatus).body(
            ApiResponse(
                statusCode = httpStatus.value(),
                data = errorDetails,
            )
        )
    }

    data class ErrorDetails(
        val error: String,
        val message: String,
        val fieldErrors: Map<String, String>? = null,
    )
}