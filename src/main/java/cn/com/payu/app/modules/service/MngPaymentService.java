package cn.com.payu.app.modules.service;

import cn.com.payu.app.modules.entity.PaymentConfig;
import cn.com.payu.app.modules.mapper.PaymentConfigMapper;
import cn.com.payu.app.modules.model.MngPaymentDTO;
import cn.com.payu.app.modules.model.params.MngPaymentSearch;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class MngPaymentService {

    @Resource
    private PaymentConfigMapper paymentConfigMapper;

    public PageInfo<MngPaymentDTO> search(MngPaymentSearch search) {
        Page page = PageHelper.startPage(search.getPageNumber(), search.getPageSize());
        List<MngPaymentDTO> list = paymentConfigMapper.search(search);
        list.forEach(mpd -> {

        });
        return new PageInfo<>(list);
    }

    public void switchPayment(Long id) {
        PaymentConfig config = paymentConfigMapper.selectUsedConfig();
        if (config != null) {
            if (config.getId().equals(id)) {
                return;
            } else {
                paymentConfigMapper.updateUsedStatusById(id, 0);
            }
        }
        paymentConfigMapper.updateUnusedStatus(0);
        paymentConfigMapper.updateUsedStatusById(id, 1);
    }

}
