package cn.com.payu.app.modules.model.params;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushNotifyBO {

    @NotNull(message = "用户Id不能为空")
    private Long userId;

    @NotBlank(message = "消息类型不能为空")
    private String type;

    @NotBlank(message = "标题不能为空")
    private String title;

    @NotBlank(message = "消息不能为空")
    private String content;

    private String extra;

    private String payload;
    private String intent;
    private String url;
    private String channelId;

    private List<String> targetIds;

}
