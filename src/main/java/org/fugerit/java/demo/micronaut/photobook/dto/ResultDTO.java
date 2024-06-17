package org.fugerit.java.demo.micronaut.photobook.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class ResultDTO<T> {

	public ResultDTO(T content) {
		this.content = content;
	}

	private T content;

	public T getContent() {
		return content;
	}

	public void setContent(T content) {
		this.content = content;
	}
}
