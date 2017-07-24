package com.galactux.weatherstation;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import io.jsonwebtoken.Claims;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class ApiController {

    private static final Log log = WeatherStationApplication.log;

    private static final DB weatherDB = (
        new Mongo("localhost", 27017)).getDB("weather");

    private static final DBCollection weatherEvents = weatherDB.getCollection(
        "weatherEvent");

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "role/{role}", method = RequestMethod.GET)
    public Boolean hasRole(
            @PathVariable final String role,
            final HttpServletRequest request) {

        final Claims claims = (Claims) request.getAttribute("claims");
        log.info("hasRole: " + role + " (" + claims.get("sub") + ")");
        return ((List<String>) claims.get("roles")).contains(role);
    }

    @RequestMapping(value="/weather", method = RequestMethod.POST)
    public Map<String, String> createWeatherEvent(
            @RequestBody Map<String, Object> body) {

        log.info("createWeatherEvent: " + body);

        // Date-time in ISO 8601 format
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        final BasicDBObjectBuilder dbObj = BasicDBObjectBuilder
            .start()
            .add("location", body.get("location").toString())
            .add("temperature", Integer.parseInt(body.get("temperature").toString()))
            .add("datetime", dateFormat.format(new Date()));
        weatherEvents.insert(dbObj.get());

        final Map<String, String> response = new HashMap<>();
        response.put("id", dbObj.get().get("_id").toString());
        return response;
    }

}
