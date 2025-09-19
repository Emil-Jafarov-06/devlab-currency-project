package com.emil.devlabcurrencyproject.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InfoResponse<T> {

    private boolean success;
    private String message;
    private T data;

}
