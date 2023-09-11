package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/*@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
		.csrf(csrf -> csrf.disable())
		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
		.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(HttpMethod.GET, "/usuario/view/cadastrar").permitAll()
				.requestMatchers(HttpMethod.POST, "/usuario/view/cadastrar").permitAll()
				.requestMatchers(HttpMethod.GET, "/inicio").permitAll()
				.requestMatchers(HttpMethod.GET, "/error").permitAll()
				.requestMatchers(HttpMethod.GET, "/livro/view/listar").hasAuthority("USER")
				.requestMatchers(HttpMethod.GET, "/livro/**").hasAuthority("FUNCIONARIO")
				.requestMatchers(HttpMethod.POST, "/livro/**").hasAuthority("FUNCIONARIO")
				.requestMatchers(HttpMethod.GET, "/emprestimo/**").hasAuthority("FUNCIONARIO")
				.requestMatchers(HttpMethod.POST, "/emprestimo/**").hasAuthority("FUNCIONARIO")
				.requestMatchers(HttpMethod.GET, "/usuario/view/listar").hasAuthority("FUNCIONARIO")
				.requestMatchers(HttpMethod.POST, "/**").hasAuthority("ADMIN")
				.requestMatchers(HttpMethod.GET, "/**").hasAuthority("ADMIN")
				
				
				
					.anyRequest().authenticated());
		
		httpSecurity.formLogin(form -> form
				.defaultSuccessUrl("/inicio")
				.permitAll());
		httpSecurity.logout(logout -> logout
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.permitAll());
		return httpSecurity.build();
	}*/

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
			.passwordEncoder(passwordEncoder);
	}
}