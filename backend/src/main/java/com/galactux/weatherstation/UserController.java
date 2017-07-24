package com.galactux.weatherstation;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import org.apache.commons.logging.Log;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
public class UserController {

    private static final Log log = WeatherStationApplication.log;

    private final Map<String, List<Object>> userDb = new HashMap<>();

    public UserController() {
        userDb.put("raspi",
            Arrays.asList("raspi-passwd", Arrays.asList("user")));
        userDb.put("user",
            Arrays.asList("user-passwd", Arrays.asList("user")));
        userDb.put("admin",
            Arrays.asList("admin-passwd", Arrays.asList("admin")));
        userDb.put("superuser",
            Arrays.asList("superuser-passwd", Arrays.asList("user", "admin")));
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public Map<String, String> login(
            @RequestBody final Map<String, String> body) throws ServletException {

        log.info("Login: " + body);

        final String username = body.get("name");
        final String password = body.get("password");

        if (username == null || password == null ||
                !userDb.containsKey(username) ||
                !password.equals(userDb.get(username).get(0))) {
            throw new ServletException("Invalid login");
        }

        final Map<String, String> response = new HashMap<>();
        response.put("token", Jwts
            .builder()
            .setSubject(username)
            .claim("roles", userDb.get(username).get(1))
            .setIssuedAt(new Date())
            .signWith(
                SignatureAlgorithm.HS256,
                WeatherStationApplication.jwtSigningKey)
            .compact());
        return response;
    }

}
