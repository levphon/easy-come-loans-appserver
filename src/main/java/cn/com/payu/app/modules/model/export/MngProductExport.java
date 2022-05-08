package cn.com.payu.app.modules.model.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class MngProductExport {

    @ExcelIgnore
    private Long id;

    @ExcelProperty(value = "产品代码", index = 0)
    private String product;

    @ExcelProperty(value = "产品名称", index = 1)
    private String productName;

    @ExcelProperty(value = "产品标签", index = 2)
    private String tags;

    @ExcelProperty(value = "产品期数", index = 3)
    private String periods;

    @ExcelProperty(value = "产品利率", index = 4)
    private Float dayInterestRate;

    @ExcelProperty(value = "最高额度", index = 5)
    private BigDecimal maxAmount;

    @ExcelProperty(value = "会员级别", index = 6)
    private String type;

    @ExcelProperty(value = "备注", index = 7)
    private String remark;

    @ExcelProperty(value = "上架状态", index = 8)
    private String enableStatus;

    @ExcelProperty(value = "创建时间", index = 9)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDate;

    @ExcelProperty(value = "点击量", index = 10)
    private Integer clickCnt;

}
