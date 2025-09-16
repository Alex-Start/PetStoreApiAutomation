package utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Store IDs to clean-up after execution
 */
public class CleanUpManager {

    private static final List<Object> listPetId = new ArrayList<>();
    private static final List<String> listUsername = new ArrayList<>();
    private static final List<Object> listOrders = new ArrayList<>();

    public static void addPetId(Object id) {
        if (id == null) {
            return;
        }
        listPetId.add(id);
    }

    public static List<Object> getListPetId() {
        return new ArrayList<>(listPetId);
    }

    public static void cleanPets() {
        listPetId.clear();
    }

    public static void addUsername(String username) {
        addUsernames(List.of(username));
    }

    public static void addUsernames(List<String> usernames) {
        if (usernames == null) {
            return;
        }
        listUsername.addAll(usernames);
    }

    public static List<String> getListUsername() {
        return new ArrayList<>(listUsername);
    }

    public static void cleanUsers() {
        listUsername.clear();
    }

    public static void addOrder(Object id) {
        if (id == null) {
            return;
        }
        listOrders.add(id);
    }

    public static List<Object> getOrders() {
        return new ArrayList<>(listOrders);
    }

    public static void cleanOrders() {
        listOrders.clear();
    }
}
