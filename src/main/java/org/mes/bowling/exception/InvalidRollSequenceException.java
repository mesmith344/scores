package org.mes.bowling.exception;

public class InvalidRollSequenceException extends RuntimeException {
    public InvalidRollSequenceException(String message) {
        super(message);
    }
}
