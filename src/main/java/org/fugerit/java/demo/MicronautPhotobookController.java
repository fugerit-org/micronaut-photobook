package org.fugerit.java.demo;

import io.micronaut.http.annotation.*;

@Controller("/micronaut-photobook")
public class MicronautPhotobookController {

    @Get(uri="/", produces="text/plain")
    public String index() {
        return "Example Response";
    }
}