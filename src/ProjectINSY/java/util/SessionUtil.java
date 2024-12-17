/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProjectINSY.java.util;

import static ProjectINSY.java.Main.setUserSessionEmail;
import static ProjectINSY.java.Main.setUserSessionID;

/**
 *
 * @author admin
 */

public class SessionUtil {

    public static boolean isLoggedIn() {
        return ProjectINSY.java.Main.getUserSessionID() > 0;
    }

    public static void updateUserSession(int id, String email) {
        setUserSessionID(id);
        setUserSessionEmail(email);
    }
}
