package cn.com.payu.app.modules.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class DoubleSerialize extends JsonSerializer<Double> {

    private DecimalFormat df = new DecimalFormat("0.00");

    @Override
    public void serialize(Double value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException, JsonProcessingException {
        if (value != null) {
            df.setMaximumFractionDigits(2);//显示几位修改几
            df.setGroupingSize(0);
            df.setRoundingMode(RoundingMode.FLOOR);
            //根据实际情况选择使用
            // gen.writeString(df.format(value));  // 返回出去是字符串
            gen.writeNumber(df.format(value));  // 返回出去是数字形式

        }
    }
}
