package com.nkh.productservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nkh.productservice.exception.ErrorCode;
import lombok.*;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {
    private int code;
    private String message;
    private T data;

    public static <T> BaseResponse<T> success(T data) {
        return BaseResponse.<T>builder()
                .code(200)
                .message("Success")
                .data(data)
                .build();
    }

    public static <T> BaseResponse<T> error(ErrorCode errorCode) {
        return BaseResponse.<T>builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
    }
}
