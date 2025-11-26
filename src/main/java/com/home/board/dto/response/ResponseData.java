package com.home.board.dto.response;

import lombok.*;


@Data
public class ResponseData<T> {
    private @Getter Header header;
    private T body;

    @Builder
    public ResponseData(Header header, T body) {
        this.header = header;
        this.body = body;
    }

}
