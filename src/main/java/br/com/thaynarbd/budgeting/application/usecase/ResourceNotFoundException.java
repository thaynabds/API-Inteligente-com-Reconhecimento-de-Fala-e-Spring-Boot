package br.com.thaynarbd.budgeting.application.usecase;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) { super(message); }
}
