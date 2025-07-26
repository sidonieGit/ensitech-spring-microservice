package com.project.user_service.exception;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Gère de manière centralisée les exceptions pour toute l'application.
 * Transforme les exceptions en réponses HTTP structurées (JSON) pour le client.
 * Intègre le logging avec Log4j2 pour tracer les erreurs.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends RuntimeException {
    private final String defaultErrorMessage;
    // Spring voit @Value et sait qu'il doit chercher la propriété 'error.default-message'
    // et injecter sa valeur ici, plutôt que de chercher un bean String.
    public GlobalExceptionHandler(@Value("${error.default-message}") String defaultErrorMessage) {
        this.defaultErrorMessage = defaultErrorMessage;
        System.out.println("Message d'erreur par défaut chargé : " + this.defaultErrorMessage);
    }

    // 1. On initialise le logger Log4j2 pour cette classe.
    private static final Logger log = LogManager.getLogger(GlobalExceptionHandler.class);

    /**
     * Gère l'exception quand une ressource n'est pas trouvée (ex: Teacher avec un ID inconnu).
     * C'est l'exception que nous lançons manuellement depuis nos services.
     * @return ResponseEntity avec un statut 404 et un corps JSON.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        // On logue l'événement avec un niveau WARN, car ce n'est pas une erreur système critique.
        log.warn("Resource not found at path [{}]: {}", path, ex.getMessage());

        Map<String, Object> body = createErrorBody(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), path);
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    /**
     * Gère les erreurs de validation pour les DTO annotés avec @Valid dans les @RequestBody.
     * C'est le cas le plus courant pour les API REST.
     * @return ResponseEntity avec un statut 400 et un corps JSON détaillé.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");

        // On extrait tous les messages d'erreur des champs.
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        // On logue l'erreur de validation avec les détails.
        log.warn("Validation error for request at path [{}]: {}", path, String.join(", ", errors));

        Map<String, Object> body = createErrorBody(HttpStatus.BAD_REQUEST, "Validation Error", "Erreur de validation des données d'entrée", path);
        body.put("errors", errors); // On ajoute le détail des erreurs pour le front-end.

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gère les erreurs de validation pour les paramètres de méthode (ex: @PathVariable, @RequestParam).
     * Moins courant pour les DTO, mais utile pour la robustesse.
     * @return ResponseEntity avec un statut 400 et un corps JSON détaillé.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");

        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        log.warn("Constraint violation at path [{}]: {}", path, String.join(", ", errors));

        Map<String, Object> body = createErrorBody(HttpStatus.BAD_REQUEST, "Constraint Violation", "Une contrainte a été violée", path);
        body.put("errors", errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gère toutes les autres exceptions non prévues (erreurs 500).
     * C'est notre "filet de sécurité" pour éviter de fuiter des détails techniques.
     * @return ResponseEntity avec un statut 500 et un message générique.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(Exception ex, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");

        // Erreur critique ! On la logue avec le niveau ERROR et on inclut la stack trace complète.
        // Le second argument 'ex' dans le logger fait cela automatiquement.
        log.error("Unhandled internal server error at path [{}]:", path, ex);

        Map<String, Object> body = createErrorBody(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "Une erreur interne est survenue. Veuillez contacter l'administrateur.", path);

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Méthode utilitaire pour créer le corps de la réponse d'erreur de base.
     * Évite la répétition de code.
     */
    private Map<String, Object> createErrorBody(HttpStatus status, String error, String message, String path) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        body.put("path", path);
        return body;
    }
}
