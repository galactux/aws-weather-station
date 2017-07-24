package com.galactux.weatherstation;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.GenericFilterBean;


@SpringBootApplication
public class WeatherStationApplication extends SpringBootServletInitializer {

    protected static final Log log = LogFactory.getLog(
        WeatherStationApplication.class);

    protected static final String jwtSigningKey = UUID.randomUUID().toString();

    private class JwtFilter extends GenericFilterBean {
        @Override
        public void doFilter(
                final ServletRequest request,
                final ServletResponse response,
                final FilterChain chain) throws IOException, ServletException {

            final String authHeader = (
                (HttpServletRequest) request).getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new ServletException("Missing or invalid Authorization header.");
            }
            try {
                request.setAttribute("claims", Jwts
                    .parser()
                    .setSigningKey(jwtSigningKey)
                    .parseClaimsJws(authHeader.substring(7)) // The part after "Bearer "
                    .getBody());
            }
            catch (final SignatureException e) {
                throw new ServletException("Invalid token.");
            }
            chain.doFilter(request, response);
        }
    }

    @Bean
    public FilterRegistrationBean jwtFilter() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new JwtFilter());
        registrationBean.addUrlPatterns("/api/*");
        return registrationBean;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(WeatherStationApplication.class, args);
        log.info("JWT signing key: " + jwtSigningKey);
    }

}
