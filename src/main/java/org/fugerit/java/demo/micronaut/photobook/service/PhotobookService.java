package org.fugerit.java.demo.micronaut.photobook.service;

import com.mongodb.client.*;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.bson.Document;
import org.fugerit.java.demo.micronaut.photobook.service.mongodb.PhotobookDownloadAggregation;
import org.fugerit.java.demo.micronaut.photobook.service.mongodb.PhotobookImagesAggregation;
import org.fugerit.java.demo.micronaut.photobook.service.mongodb.PhotobookListAggregation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;

@Singleton
public class PhotobookService {

	Logger log = (Logger) LoggerFactory.getLogger(PhotobookService.class);

	@Inject
	MongoClient mongoClient;

	public PhotobookService(MongoClient mongoClient) {
		this.mongoClient = mongoClient;
	}

	private MongoDatabase getDatabase() {
		return this.mongoClient.getDatabase( "photobook_demo" );
	}

	public Document listPhotobooks( String langCode, int perPage, int currentPage ) {
		MongoCollection<Document> collection = this.getDatabase().getCollection( "photobook_meta" );
		AggregateIterable<Document> result = collection.aggregate( PhotobookListAggregation.getAggregation(langCode, perPage, currentPage) );
		Document doc = null;
		try (MongoCursor<Document> cursor = result.iterator()) {
			if ( cursor.hasNext() ) {
				doc = cursor.next();
			}
		}
		log.info( "listPhotobooks, langCode : {}, perPage : {}, currentPage : {}", langCode, perPage, currentPage );
		return doc;
	}
	
	public Document listImages( String photobookId, String langCode, int perPage, int currentPage ) {
		MongoCollection<Document> collection = this.getDatabase().getCollection( "photobook_images" );
		AggregateIterable<Document> result = collection.aggregate( PhotobookImagesAggregation.getAggregation(photobookId, langCode, perPage, currentPage) );
		Document doc = null;
		try (MongoCursor<Document> cursor = result.iterator()) {
			if ( cursor.hasNext() ) {
				doc = cursor.next();
			}
		}
		log.info( "listImages, photobookId : {}, langCode : {}", photobookId, langCode );
		return doc;
	}
	

	public byte[] downloadImage( String photobookId, String imageId ) {
		MongoCollection<Document> collection = this.getDatabase().getCollection( "photobook_images" );
		AggregateIterable<Document> result = collection.aggregate( PhotobookDownloadAggregation.getAggregation(photobookId, imageId) );
		byte[] data = null;
		try (MongoCursor<Document> cursor = result.iterator()) {
			if ( cursor.hasNext() ) {
				Document doc = cursor.next();
				String base64 = (String)doc.get( "base64" );
				data = Base64.getDecoder().decode( base64 );
				log.debug( "found! {}, {}, size: {}", photobookId, imageId, data.length );
			} else {
				log.debug( "not found! {}, {}", photobookId, imageId );
			}
		}
		log.info( "downloadImage photobookId : {}, imageId : {}", photobookId, imageId );
		return data;
	}
	
}
