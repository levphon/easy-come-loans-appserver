package cn.com.payu.app.modules.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class OcrIdcardStatelessBO {

    @NotBlank
    private String url;

    @NotBlank
    private String side;

}
