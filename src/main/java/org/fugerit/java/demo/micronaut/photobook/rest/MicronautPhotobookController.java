package org.fugerit.java.demo.micronaut.photobook.rest;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import org.bson.Document;
import org.fugerit.java.demo.micronaut.photobook.dto.ResultDTO;
import org.fugerit.java.demo.micronaut.photobook.service.PhotobookService;
import org.fugerit.java.demo.micronaut.photobook.util.SanitizeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

@ExecuteOn(TaskExecutors.BLOCKING)
@Controller("/api/photobook/view")
public class MicronautPhotobookController {

    private static final Integer DEF_PAGE_SIZE = 10;

    PhotobookService photobookService;

    public MicronautPhotobookController(PhotobookService photobookService) {
        this.photobookService = photobookService;
    }

    Logger log = LoggerFactory.getLogger(MicronautPhotobookController.class);

    @Get(uri="/list", produces="application/json")
    public HttpResponse<ResultDTO<Document>> list() {
        return RestHelper.defaultHandle( () -> {
            Document doc =  this.photobookService.listPhotobooks(Locale.ITALY.getCountry(), 10, 1);
            ResultDTO<Document> dto = new ResultDTO<>( doc );
            log.info( "doc : {}", doc );
            return HttpResponse.ok( dto );
        } );
    }

    @Get(uri="/images/{photobookId}/language/{language}/current_page/{currentPage}/page_size/{pageSize}", produces="application/json")
    public HttpResponse<ResultDTO<Document>> images( @PathVariable String photobookId, @PathVariable String language, @PathVariable Integer currentPage, @PathVariable Integer pageSize ) {
        return RestHelper.defaultHandle( () -> {
            Document doc =  this.photobookService.listImages( photobookId, language, pageSize, currentPage);
            ResultDTO<Document> dto = new ResultDTO<>( doc );
            return HttpResponse.ok( dto );
        } );
    }


    @Get(uri="/images/{photobookId}", produces="application/json")
    public HttpResponse<ResultDTO<Document>> images( @PathVariable String photobookId ) {
        return this.images(photobookId, "def", 1, DEF_PAGE_SIZE);
    }

    @Get(uri="/download/{imagePath}", produces="image/jpeg")
    public byte[] downloadImage( @PathVariable String imagePath ) {
        imagePath = imagePath.substring( 0, imagePath.indexOf( '.' ) );
        String[] split = imagePath.split( "_" );
        String photobookIdParam = SanitizeUtil.sanitizeInputParameter( split[0] );
        String imageIdParam = SanitizeUtil.sanitizeInputParameter( split[1] );
        log.info( "photobookId : {}, imageId : {}", photobookIdParam, imageIdParam );
        return this.photobookService.downloadImage( photobookIdParam, imageIdParam );
    }

}