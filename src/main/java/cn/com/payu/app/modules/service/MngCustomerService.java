package cn.com.payu.app.modules.service;

import cn.com.payu.app.modules.converter.CustomerServiceConfigConverter;
import cn.com.payu.app.modules.entity.CustomerServiceConfig;
import cn.com.payu.app.modules.mapper.CustomerServiceConfigMapper;
import cn.com.payu.app.modules.model.CustomerServiceConfigDTO;
import cn.com.payu.app.modules.model.params.MngCustomerServiceConfigBO;
import cn.com.payu.app.modules.utils.MngContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class MngCustomerService {

    @Resource
    private CustomerServiceConfigMapper customerServiceConfigMapper;

    public CustomerServiceConfigDTO getConfig() {
        CustomerServiceConfig config = customerServiceConfigMapper.selectById(1L);
        return CustomerServiceConfigConverter.INSTANCE.do2dto(config);
    }

    public void setConfig(MngCustomerServiceConfigBO configBO) {
        CustomerServiceConfig config = CustomerServiceConfigConverter.INSTANCE.bo2do(configBO);

        CustomerServiceConfig updateConfig = customerServiceConfigMapper.selectById(1L);
        if (updateConfig == null) {
            config.setCreatedDate(new Date());
            config.setCreatedBy(MngContextHolder.getUserId());
            customerServiceConfigMapper.insert(config);
        } else {
            config.setId(updateConfig.getId());
            config.setUpdatedDate(new Date());
            customerServiceConfigMapper.updateByPrimaryKeySelective(config);
        }
    }

}
