package utils;

public class SessionManager {
    private static int currentUserId;
    private static String currentUserType;

    // Getter dan Setter untuk currentUserId
    public static int getCurrentUserId() {
        return currentUserId;
    }

    public static void setCurrentUserId(int idPengguna) {
        currentUserId = idPengguna;
    }

    // Getter dan Setter untuk currentUserType (opsional)
    public static String getCurrentUserType() {
        return currentUserType;
    }

    public static void setCurrentUserType(String userType) {
        currentUserType = userType;
    }

    // Metode untuk membersihkan session
    public static void clearSession() {
        currentUserId = 0;
        currentUserType = null;
    }
}