package pl.edu.pw.ee.cookbookserver.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.edu.pw.ee.cookbookserver.dto.ErrorDto;
import pl.edu.pw.ee.cookbookserver.misc.Error;
import pl.edu.pw.ee.cookbookserver.misc.PayloadKey;

@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;

    @Autowired
    public WebSecurityConfiguration(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .anyRequest().authenticated()
                .and()
            .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> {
                    response.sendError(HttpStatus.UNAUTHORIZED.value());
                })
                .and()
            .formLogin()
                .loginProcessingUrl("/auth/login")
                .usernameParameter(PayloadKey.LOGIN.value())
                .passwordParameter(PayloadKey.PASSWORD.value())
                .successHandler((request, response, authentication) -> {
                    response.setStatus(HttpStatus.NO_CONTENT.value());
                })
                .failureHandler((request, response, exception) -> {
                    Error error = exception instanceof BadCredentialsException
                            ? Error.BAD_CREDENTIALS
                            : Error.USER_INACTIVE;
                    ErrorDto errorDto = new ErrorDto(error.code(), exception.getMessage());
                    response.setStatus(error.status().value());
                    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                    response.getWriter().write(errorDto.toString());
                })
                .and()
            .logout()
                .logoutUrl("/auth/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(HttpStatus.NO_CONTENT.value());
                })
                .and()
            .cors().and()
            .csrf().disable();
    }
}
