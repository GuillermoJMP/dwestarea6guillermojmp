package com.dwes.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableWebSecurity
public class ConfiguracionSeguridad {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(authorize -> authorize
				// Rutas públicas para invitados
				.requestMatchers("/inicio", "/verPlantas", "/registroCliente", "/guardarCliente", "/login",
						"/autenticar")
				.permitAll()
				// Rutas para administración (ADMIN)
				.requestMatchers("/plantasAdmin", "/personaAdmin").hasRole("ADMIN")
				// Rutas para gestión de ejemplares, mensajes y stock (ADMIN y PERSONAL)
				.requestMatchers("/ejemplaresAdmin", "/mensajesAdmin", "/stockAdmin").hasAnyRole("ADMIN", "PERSONAL")
				// Rutas para clientes
				.requestMatchers("/zonaCliente", "/pedidoCliente", "/carrito", "/confirmarPedido", "/misPedidos")
				.hasRole("CLIENTE").anyRequest().authenticated()).formLogin(form -> form.loginPage("/login")
						// Redirigir según rol: se hace en el controlador de autenticación
						// (CredencialesController)
						.defaultSuccessUrl("/inicio", true).permitAll())
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/inicio").permitAll())
				.sessionManagement(session -> session.maximumSessions(1));
		return http.build();
	}
}
