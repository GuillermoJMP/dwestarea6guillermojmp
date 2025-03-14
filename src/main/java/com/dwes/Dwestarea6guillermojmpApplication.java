package com.dwes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
public class Dwestarea6guillermojmpApplication {

    public static void main(String[] args) {
        SpringApplication.run(Dwestarea6guillermojmpApplication.class, args);

        try {
            // Obtener la IP local
            String ip = InetAddress.getLocalHost().getHostAddress();
            String port = "8081";  // Cambia si usas otro puerto
            
            // Imprimir enlace clickeable
            System.out.println("\n--------------------------------------------------");
            System.out.println("🚀 Aplicación iniciada en:");
            System.out.println("🌍 Local:     \u001B[34mhttp://localhost:" + port + "\u001B[0m"); // Azul
            System.out.println("🌎 Red:       \u001B[34mhttp://" + ip + ":" + port + "\u001B[0m"); // Azul
            System.out.println("--------------------------------------------------\n");

        } catch (UnknownHostException e) {
            System.err.println("⚠ No se pudo obtener la IP local.");
        }
    }
}
