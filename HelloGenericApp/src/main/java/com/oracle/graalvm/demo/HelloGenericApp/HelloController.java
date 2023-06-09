package com.oracle.graalvm.demo.HelloGenericApp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/hello")

public class HelloController {


    private  static final Logger logger = LoggerFactory.getLogger(HelloController.class);
    //Tracer tracer = Configuration.fromEnv().getTracer();
    @Autowired
    private RestTemplate restTemplate;

    @Value("${spring.application.name}")
    private String applicationName;

    public HelloController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    /**
     * Call curl localhost:8080/hello?name=Nono => Default language => English
     * @param name
     * @param lang
     * @return
     */

    @GetMapping("/")
    @RequestMapping()
    public ResponseEntity hello(
            @RequestParam(value = "name", defaultValue = "World" ) String name,
            @RequestParam(value = "l", defaultValue = "en" ) String lang){

        logger.info("Incoming request "+ applicationName + " " +lang+" "+name);

        String url ="";


        switch (lang) {
            case "fr" -> url = "http://localhost:8080/hello/fr?name={name}";
            case "es" -> url = "http://localhost:8080/hello/es?name={name}";
            case "it" -> url = "http://localhost:8080/hello/it?name={name}";
            default -> url = "http://localhost:8080/hello/en?name={name}";
        }
        logger.info("Calling url {}", url);
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, null, String.class, name);
        String body = response.getBody();
        logger.info("Response {}",response.getBody());
        return ResponseEntity.ok(body);
    }

    @GetMapping("/en")
    public ResponseEntity helloEn(@RequestParam(value = "name", defaultValue = "World" ) String name) {
        logger.info("New Request  at {}/{} for {}", applicationName,"fr",name);
        return ResponseEntity.ok("Hello "+ name);
    }

    @GetMapping("/fr")
    public ResponseEntity helloFr(@RequestParam(value = "name", defaultValue = "World" ) String name) {
        logger.info("Nouvelle requête at {}/{} pour {}", applicationName,"fr",name);
        return ResponseEntity.ok("Bonjour "+ name);
    }

    @GetMapping("it")
    public ResponseEntity helloIt(@RequestParam(value = "name", defaultValue = "World" ) String name) {
        logger.info("Nuova query per  {}/{} pour {}", applicationName,"fr",name);
        return ResponseEntity.ok("Bongiorno "+ name);
    }

    @GetMapping("es")
    public ResponseEntity helloEs(@RequestParam(value = "name", defaultValue = "World" ) String name) {
        logger.info("Nueva consulta para {}/{} per {}", applicationName,"fr",name);
        return ResponseEntity.ok("Hola "+ name);
    }

}
