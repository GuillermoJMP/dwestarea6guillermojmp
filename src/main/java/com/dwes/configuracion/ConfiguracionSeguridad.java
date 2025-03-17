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
		http.csrf().disable().authorizeHttpRequests(authorize -> authorize
				// Permitir acceso público a archivos estáticos (CSS, JS, imágenes)
				.requestMatchers("/css/**", "/js/**", "/images/**", "/registroCliente.css").permitAll()
				// Permitir acceso a rutas públicas
				.requestMatchers("/inicio", "/verPlantas", "/registroCliente", "/guardarCliente", "/login",
						"/autenticar")
				.permitAll()
				// Rutas restringidas a ADMIN
				.requestMatchers("/plantasAdmin", "/personaAdmin").hasRole("ADMIN")
				// Rutas accesibles para ADMIN y PERSONAL
				.requestMatchers("/ejemplaresAdmin", "/mensajesAdmin", "/stockAdmin").hasAnyRole("ADMIN", "PERSONAL")
				// Rutas accesibles solo para CLIENTES
				.requestMatchers("/zonaCliente", "/pedidoCliente", "/carrito", "/confirmarPedido", "/misPedidos")
				.hasRole("CLIENTE")
				// Cualquier otra petición requiere autenticación
				.anyRequest().authenticated())
				// Configurar login y logout
				.formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/inicio", true).permitAll())
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/inicio").permitAll())
				.sessionManagement(session -> session.maximumSessions(1));

		return http.build();
	}
}
