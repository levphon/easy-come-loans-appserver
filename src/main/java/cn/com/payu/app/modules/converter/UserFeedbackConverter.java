package cn.com.payu.app.modules.converter;

import cn.com.payu.app.modules.entity.UserFeedback;
import cn.com.payu.app.modules.model.UserFeedbackBO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserFeedbackConverter {

    UserFeedbackConverter INSTANCE = Mappers.getMapper(UserFeedbackConverter.class);

    UserFeedback bo2do(UserFeedbackBO feedbackBO);

}
