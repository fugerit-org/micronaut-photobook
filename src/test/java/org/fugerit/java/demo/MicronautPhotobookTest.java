package org.fugerit.java.demo;

import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.demo.micronaut.photobook.rest.MicronautMetaController;
import org.fugerit.java.demo.micronaut.photobook.rest.RestHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.MountableFile;

import java.io.File;
import java.util.Map;
import java.util.Objects;

@MicronautTest
class MicronautPhotobookTest {

    static Logger log = (Logger) LoggerFactory.getLogger(MicronautPhotobookTest.class);

    static final GenericContainer mongoDBContainer = new GenericContainer( "mongo:8.0.0-rc8" )
            .withCopyToContainer(MountableFile.forHostPath( new File( "src/test/resources/mongo-db/mongo-init.js" ).getPath() ), "/docker-entrypoint-initdb.d/mongo-init.js" )
            .withExposedPorts( 27017 );

    static Map<String, Object> config;

    @BeforeAll
    public static void startMongoDB() {
        if ( !mongoDBContainer.isRunning() ) {
            mongoDBContainer.start();
        }
        int mongoDbPort = mongoDBContainer.getFirstMappedPort();
        log.info( "mongoDB port : {}", mongoDbPort );
        String mongoDbUri = String.format( "mongodb://localhost:%s/photobook_demo", mongoDbPort );
        log.info( "mongoDbUri: {}", mongoDbUri );
        config = Map.of( "mongodb.uri", mongoDbUri );
    }

    @AfterAll
    public static void stopMongoDB() {
        if ( mongoDBContainer.isRunning() ) {
            mongoDBContainer.stop();
        }
    }

    @Inject
    EmbeddedApplication<?> application;

    @Test
    void testItWorks() {
        Assertions.assertTrue(application.isRunning());
    }

    @Test
    void testAlbumOk( RequestSpecification spec ) {
        try (EmbeddedServer server = ApplicationContext.run( EmbeddedServer.class, config )) {
            RestAssured.port = server.getPort();
            spec.given()
                    .when()
                    .get( "/photobook-demo/api/photobook/view/list" )
                    .then()
                    .statusCode(200);
            spec.given()
                    .when()
                    .get( "/photobook-demo/api/photobook/view/images/springio23" )
                    .then()
                    .statusCode(200);

            spec.given()
                    .when()
                    .get( "/photobook-demo/api/photobook/view/download/springio23_1000.jpg" )
                    .then()
                    .statusCode(200);
        }

    }

    @Test
    void testRestHelper() {
        HttpResponse<?> res = RestHelper.defaultHandle( () -> {
            if ( Boolean.TRUE.booleanValue() ) {
                throw new ConfigRuntimeException( "scenario exception" );
            }
            return null;
        } );
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), res.getStatus().getCode());
    }

    @Test
    void testMetaVersionOk( RequestSpecification spec ) {
            spec
                .given()
                .when()
                .get( "/photobook-demo/api/meta/version" )
                .then()
                .statusCode(200);
    }

    @Test
    void testMetaInfoOk( RequestSpecification spec ) {
            spec
                .given()
                .when()
                .get( "/photobook-demo/api/meta/info" )
                .then()
                .statusCode(200);
    }

}
