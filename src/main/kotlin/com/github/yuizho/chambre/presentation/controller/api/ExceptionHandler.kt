package com.github.yuizho.chambre.presentation.controller.api

import com.github.yuizho.chambre.exception.BusinessException
import com.github.yuizho.chambre.presentation.controller.api.dto.ErrorResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.server.ServerWebInputException

@ControllerAdvice(basePackageClasses = [ExceptionHandler::class])
class ExceptionHandler {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(ExceptionHandler::class.java)
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    fun handleException(ex: Exception): ResponseEntity<ErrorResponse> {
        logger.error("unexpected error has occurred: ${ex.stackTrace}")
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse("unexpected error has occurred"))
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    fun handleServerWebInputException(ex: ServerWebInputException): ResponseEntity<ErrorResponse> {
        logger.warn("validation error has occurred: ${ex.reason}")
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse(ex.reason ?: "no message"))
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    fun handleBusinessException(ex: BusinessException): ResponseEntity<ErrorResponse> {
        logger.warn("business error has occurred: ${ex.message}")
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse(ex.message))
    }
}