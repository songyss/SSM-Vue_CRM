package com.csi.util;

import de.mkammerer.argon2.Argon2Factory;
import de.mkammerer.argon2.Argon2;

public class PasswordUtil {
    private static final Argon2 argon2 = Argon2Factory.create();
    
    public static String encode(String password) {
        return argon2.hash(10, 65536, 1, password.toCharArray());
    }
    
    public static boolean verify(String hashedPassword, String password) {
        return argon2.verify(hashedPassword, password.toCharArray());
    }
}
