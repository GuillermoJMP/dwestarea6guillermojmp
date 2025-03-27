package com.dwes.configuracion;

import com.dwes.seguridad.DetallesUsuarioServicio;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

@Configuration
public class ConfiguracionSeguridad {

	private final DetallesUsuarioServicio detallesUsuarioServicio;

	public ConfiguracionSeguridad(DetallesUsuarioServicio detallesUsuarioServicio) {
		this.detallesUsuarioServicio = detallesUsuarioServicio;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeHttpRequests(auth -> auth.requestMatchers("/css/**", "/js/**", "/images/**")
				.permitAll()
				.requestMatchers("/inicio", "/verPlantas", "/registroCliente", "/guardarCliente", "/login",
						"/autenticar")
				.permitAll().requestMatchers("/plantasAdmin", "/personaAdmin").hasAuthority("ADMIN")
				.requestMatchers("/ejemplaresAdmin", "/mensajesAdmin", "/stockAdmin")
				.hasAnyAuthority("ADMIN", "PERSONAL")
				.requestMatchers("/zonaCliente", "/pedidoCliente", "/carrito", "/confirmarPedido", "/misPedidos")
				.hasAnyAuthority("CLIENTE", "PERSONAL").anyRequest().authenticated())
				.formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/inicio", true).permitAll())
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/inicio").permitAll()
						.addLogoutHandler((request, response, authentication) -> SecurityContextHolder.clearContext()))
				.sessionManagement(session -> session.sessionFixation().migrateSession()
						.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
				.securityContext(
						security -> security.securityContextRepository(new HttpSessionSecurityContextRepository()))
				.authenticationProvider(authenticationProvider());
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(detallesUsuarioServicio);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}
}
