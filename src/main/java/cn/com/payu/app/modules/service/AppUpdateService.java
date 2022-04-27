package cn.com.payu.app.modules.service;

import cn.com.payu.app.modules.converter.AppUpdateConverter;
import cn.com.payu.app.modules.entity.AppUpdate;
import cn.com.payu.app.modules.mapper.AppUpdateMapper;
import cn.com.payu.app.modules.model.AppUpdateDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class AppUpdateService {

    @Resource
    private AppUpdateMapper appUpdateMapper;

    public AppUpdateDTO info(String name) {
        AppUpdate appUpdate = appUpdateMapper.selectByName(name);
        return AppUpdateConverter.INSTANCE.do2dto(appUpdate);
    }

}
