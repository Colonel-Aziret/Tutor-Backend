package com.example.tutor.model;

import lombok.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseResponse {
    private boolean success;
    private Object msg;
    private Object res;
    @Builder.Default
    private String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    @Builder.Default
    private String ver = "0.4.001";
}
