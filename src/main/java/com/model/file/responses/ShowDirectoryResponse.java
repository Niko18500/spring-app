package com.model.file.responses;

import com.model.Response;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class ShowDirectoryResponse extends Response {

    private String body;

    public ShowDirectoryResponse(String body) {
        super(true, "ok");
        this.body = body;
    }
}