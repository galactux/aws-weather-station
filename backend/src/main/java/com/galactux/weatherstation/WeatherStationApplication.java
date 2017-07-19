package com.galactux.weatherstation;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

// http://www.mkyong.com/mongodb/


@RequestMapping("/rest")
@RestController
@SpringBootApplication
public class WeatherStationApplication extends SpringBootServletInitializer {

    private static final Log log = LogFactory.getLog(WeatherStationApplication.class);
    private static final DB weatherDB = (
        new Mongo("localhost", 27017)).getDB("weather");
    private static final DBCollection weatherEvent = weatherDB.getCollection(
        "weatherEvent");

    public static void main(String[] args) throws Exception {
        SpringApplication.run(WeatherStationApplication.class, args);
    }

    @RequestMapping(value="/weather", method = RequestMethod.POST)
    public Map<String, Object> createWeatherEvent(@RequestBody Map<String, Object> body) {
        log.info("createWeatherEvent: " + body);

        BasicDBObjectBuilder dbObj = BasicDBObjectBuilder.start()
            .add("location", body.get("location").toString())
            .add("temperature", Integer.parseInt(body.get("temperature").toString()))
            .add("time", body.get("time").toString());
        weatherEvent.insert(dbObj.get());

        Map<String, Object> response = new LinkedHashMap<String, Object>();
        response.put("message", "Created successfully");
        response.put("weatherEvent", dbObj.get());
        return response;
    }

}
