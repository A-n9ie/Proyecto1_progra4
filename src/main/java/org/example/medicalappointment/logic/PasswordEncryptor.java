package org.example.medicalappointment.logic;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncryptor {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String[] passwords = {"root", "111", "222", "333", "444", "555"};

        for (String password : passwords) {
            System.out.println(password + " -> " + encoder.encode(password));
        }
    }
}
