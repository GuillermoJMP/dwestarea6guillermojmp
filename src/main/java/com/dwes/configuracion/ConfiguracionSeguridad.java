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
		http.csrf().disable() // Deshabilitar CSRF para pruebas; en producción, habilítalo y configúralo
								// correctamente.
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers("/inicio", "/verPlantas", "/registroCliente", "/guardarCliente", "/login",
								"/autenticar")
						.permitAll().requestMatchers("/plantasAdmin", "/personaAdmin").hasRole("ADMIN")
						.requestMatchers("/ejemplaresAdmin", "/mensajesAdmin", "/stockAdmin")
						.hasAnyRole("ADMIN", "PERSONAL")
						.requestMatchers("/zonaCliente", "/pedidoCliente", "/carrito", "/confirmarPedido",
								"/misPedidos")
						.hasRole("CLIENTE").anyRequest().authenticated())
				.formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/inicio", true).permitAll())
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/inicio").permitAll())
				.sessionManagement(session -> session.maximumSessions(1));
		return http.build();
	}
}
