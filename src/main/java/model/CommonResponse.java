package model;

/**
 * Response body:
 * {
 * "code": 1,
 * "type": "error",
 * "message": "User not found"
 * }
 * or
 * {
 * "code": 200,
 * "type": "unknown",
 * "message": "logged in user session:1741344892966"
 * }
 */
public record CommonResponse(int code, String type, String message) implements IModel {
}
