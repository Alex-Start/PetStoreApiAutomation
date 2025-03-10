package model;

public enum PetStatus {
    AVAILABLE("AVAILABLE"),
    PENDING("PENDING"),
    SOLD("SOLD");

    private final String status;

    PetStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return status;
    }
}
