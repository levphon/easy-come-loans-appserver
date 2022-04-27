package cn.com.payu.app.modules.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LivenessDetectionResultBO {

    @NotBlank
    private String bizToken;

}
