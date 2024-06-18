package org.fugerit.java.demo.micronaut.photobook.rest;

import io.micronaut.http.HttpResponse;
import org.fugerit.java.core.function.UnsafeSupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestHelper {

    private RestHelper() {}

    static Logger log = LoggerFactory.getLogger(RestHelper.class);

    public static <T> HttpResponse<T> defaultHandle(UnsafeSupplier<HttpResponse<T>, Exception> fun) {
        try {
            return fun.get();
        } catch ( Exception e ) {
            log.error( String.format( "Error : %s", e.getMessage() ), e );
            return HttpResponse.serverError();
        }
    }

}
