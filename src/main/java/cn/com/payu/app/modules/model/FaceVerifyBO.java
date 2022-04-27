package cn.com.payu.app.modules.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class FaceVerifyBO {

    @NotBlank
    private String photo;
    private String videoUrl;

}
