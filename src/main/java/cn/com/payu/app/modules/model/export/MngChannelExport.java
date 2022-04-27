package cn.com.payu.app.modules.model.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class MngChannelExport {

    @ExcelIgnore
    private Long id;

    @ExcelProperty(value = "渠道代码", index = 0)
    private String channel;

    @ExcelProperty(value = "渠道名称", index = 1)
    private String channelName;

    @ExcelProperty(value = "备注", index = 2)
    private String remark;

    @ExcelProperty(value = "上架状态", index = 3)
    private String enableStatus;

    @ExcelProperty(value = "创建时间", index = 4)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDate;

    @ExcelProperty(value = "注册量", index = 5)
    private Integer registerCnt;

    @ExcelProperty(value = "下载量", index = 6)
    private Integer downloadCnt;

}
