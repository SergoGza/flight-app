package com.tokioschool.flightapp;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordVerificationTest {

    @Test
    public void verifyPasswords() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Hash del usuario 'user@bla.com'
        String userHash = "$2a$12$anCYkToMiWwQPym2paR6K.1NYCy2OJVyysp/ZCV1zL9oXoSALh7pa2";
        System.out.println("Verificando 'user' contra hash de user@bla.com:");
        System.out.println("¿'user' coincide? " + encoder.matches("user", userHash));

        // Hash del usuario 'admin@bla.com'
        String adminHash = "$2a$12$nl9E09h9DJs0aWxdPZlVr.rfCVeN.LFiKNBTRnRzQiCZuzGd7JQpe";
        System.out.println("\nVerificando 'admin' contra hash de admin@bla.com:");
        System.out.println("¿'admin' coincide? " + encoder.matches("admin", adminHash));

        // Generar nuevos hashes para verificar
        System.out.println("\nGenerando nuevos hashes:");
        System.out.println("Hash para 'user': " + encoder.encode("user"));
        System.out.println("Hash para 'admin': " + encoder.encode("admin"));
    }
}