package com.ioally.amoeba.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * 统一响应封装dto
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ResponseDto implements Serializable {

    private String statusCode;

    private String message;

    private Object content;

    private ResponseDto() {
        this.statusCode = "200";
        this.message = "success";
    }

    private ResponseDto(Object content) {
        this();
        this.content = content;
    }

    public static ResponseDto newInstance() {
        return new ResponseDto();
    }

    public static ResponseDto newInstance(Object content) {
        return new ResponseDto(content);
    }

    public static ResponseDto build(Object content) {
        return new ResponseDto(content);
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
