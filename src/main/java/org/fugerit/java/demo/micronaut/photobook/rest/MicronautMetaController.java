package org.fugerit.java.demo.micronaut.photobook.rest;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import org.fugerit.java.core.util.PropsIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

@ExecuteOn(TaskExecutors.BLOCKING)
@Controller("/api/meta")
public class MicronautMetaController {

    Logger log = LoggerFactory.getLogger(MicronautMetaController.class);

    @Get(uri="/version", produces="application/json")
    public HttpResponse<Properties> version() {
        return RestHelper.defaultHandle( () -> {
            Properties buildProps = PropsIO.loadFromClassLoader( "build.properties" );
            log.info( "buildProps : {}", buildProps );
            return HttpResponse.ok( buildProps );
        } );
    }

    private static final String[] ADD_PROPS = { "java.version", "java.vendor", "os.name", "os.version", "os.arch" };

    @Get(uri="/info", produces="application/json")
    public HttpResponse<String> info() {
        return RestHelper.defaultHandle( () -> {
            StringBuilder info = new StringBuilder();
            for ( String key : ADD_PROPS ) {
                info.append( key );
                info.append( " = " );
                info.append( System.getProperty( key ) );
                info.append( ", " );
            }
            String res = info.toString();
            return HttpResponse.ok( res );
        } );
    }

}