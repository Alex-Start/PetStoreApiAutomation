package model;

import verify.IVerifier;

// contains interfaces to verify actual values. It helps to compare as String, Number, Regexp, etc.
public record CommonResponseComparator(IVerifier code, IVerifier type, IVerifier message) implements IModel {
    @Override
    public String toString() {
        return "CommonResponseComparator{" +
                "code=" + code.getValue() +
                ", type=" + type.getValue() +
                ", message=" + message.getValue() +
                '}';
    }
}
