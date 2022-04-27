package cn.com.payu.app.modules.converter;

import cn.com.payu.app.modules.entity.User;
import cn.com.payu.app.modules.entity.UserProfile;
import cn.com.payu.app.modules.model.AppUser;
import cn.com.payu.app.modules.model.UserDTO;
import cn.com.payu.app.modules.model.UserProfileDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserConverter {

    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    UserDTO do2dto(User user);

    UserDTO do2dto(AppUser user);

    UserProfileDTO do2dto(UserProfile profile);

}
