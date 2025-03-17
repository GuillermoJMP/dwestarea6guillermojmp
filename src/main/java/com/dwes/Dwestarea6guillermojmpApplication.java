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
			String url = "http://localhost:8081/inicio";
			System.out.println("\n\t\t\t\033[1;36mLink de Entrada --> " + url + "\033[0m");

			ProcessBuilder pb = new ProcessBuilder();
			String os = System.getProperty("os.name").toLowerCase();
			if (os.contains("win")) {
				pb.command("cmd", "/c", "start", url);
			} else if (os.contains("mac")) {
				pb.command("open", url);
			} else if (os.contains("nix") || os.contains("nux")) {
				pb.command("xdg-open", url);
			} else {
				throw new UnsupportedOperationException("Sistema operativo no soportado para abrir navegadores.");
			}
			pb.start();
			System.out.println("\n\t\t\t\033[1;32mAbriendo el navegador con el enlace...\033[0m");
		} catch (Exception e) {
			System.out.println("\n\t\t\t\033[1;31mError al intentar abrir el navegador:\033[0m");
			e.printStackTrace();
		}
	}
}
