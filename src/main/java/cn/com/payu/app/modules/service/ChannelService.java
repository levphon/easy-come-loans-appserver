package cn.com.payu.app.modules.service;

import cn.com.payu.app.common.exception.AppServerException;
import cn.com.payu.app.modules.converter.ChannelConverter;
import cn.com.payu.app.modules.entity.Channel;
import cn.com.payu.app.modules.mapper.ChannelMapper;
import cn.com.payu.app.modules.mapper.EventClickingMapper;
import cn.com.payu.app.modules.mapper.EventTrackingMapper;
import cn.com.payu.app.modules.mapper.OrderMapper;
import cn.com.payu.app.modules.model.*;
import cn.com.payu.app.modules.model.export.MngChannelExport;
import cn.com.payu.app.modules.model.params.ChannelSearch;
import cn.com.payu.app.modules.model.params.MngChannelSettingBO;
import cn.com.payu.app.modules.utils.MngContextHolder;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.glsx.plat.common.utils.DateUtils;
import com.glsx.plat.common.utils.StringUtils;
import com.glsx.plat.core.enums.SysConstants;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChannelService {

    @Resource
    private ChannelMapper channelMapper;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private EventTrackingMapper eventTrackingMapper;

    @Resource
    private EventClickingMapper eventClickingMapper;

    public PageInfo<MngChannelDTO> search(ChannelSearch search) {
        Page page = PageHelper.startPage(search.getPageNumber(), search.getPageSize());
        if (!MngContextHolder.getUser().getIsAdmin()) {
            search.setChannelIds(MngContextHolder.getDeptIds());
        }
        List<MngChannelDTO> list = channelMapper.search(search);
        channelListAssembled(list, search);
        return new PageInfo<>(list);
    }

    public void channelListAssembled(List<MngChannelDTO> list, ChannelSearch search) {
        //如果没有搜索时间，默认当天
        if (StringUtils.isEmpty(search.getSDate()) && StringUtils.isEmpty(search.getEDate())) {
            String currDate = DateUtils.format(new Date(), "yyyy-MM-dd");
            search.setSDate(currDate);
            search.setEDate(currDate);
        }

        list.forEach(mcd -> {
            search.setChannel(mcd.getChannel());

            List<ChannelUniqueVisitor> uvTrackingCnts = eventTrackingMapper.selectByChannel(search);
            List<StatisticsModel> clickingCnts = eventClickingMapper.selectByChannel(mcd.getChannel());
            List<StatisticsModel> vipOrderCnts = orderMapper.selectByChannel(mcd.getChannel());

            Map<String, StatisticsModel> clickingMap = clickingCnts.stream().collect(Collectors.toMap(StatisticsModel::getLabel, d -> d));
            Map<String, StatisticsModel> vipOrderMap = vipOrderCnts.stream().collect(Collectors.toMap(StatisticsModel::getLabel, d -> d));

            StatisticsModel clickingModel = Optional.ofNullable(clickingMap.get(mcd.getChannel())).orElse(new StatisticsModel(mcd.getChannel(), 0));
            StatisticsModel vipOrderModel = Optional.ofNullable(vipOrderMap.get(mcd.getChannel())).orElse(new StatisticsModel(mcd.getChannel(), 0, BigDecimal.ZERO));

            mcd.setBuyVipCnt(vipOrderModel.getCnt());
            mcd.setBuyVipAmount(vipOrderModel.getAmount());

            //数据类型
            Integer dataType = MngContextHolder.getUser().getDataType();

            //打折率
            Float discountRate = mcd.getDiscountRate();
            //单价
            BigDecimal unitPrice = mcd.getUnitPrice();

            if (MngContextHolder.getUser().getIsAdmin()) {
                //UV
                mcd.setUv(uvTrackingCnts.size());
                //CPA=消费量/转化量=（CPC*点击量）/（CVR*点击量）=CPC/CVR
                if (vipOrderModel.getCnt() == null || mcd.getRegisterCnt() == 0) {
                    mcd.setCpa(0);
                } else {
                    mcd.setCpa(mcd.getRegisterCnt());
                }

//            结算注册登陆=注册数x打折率
                Integer settleRegisterCnt = (int) (discountRate * mcd.getRegisterCnt());
//            结算登陆率=下载数x打折率
                Integer settleRegisterRatio = (int) (discountRate * mcd.getDownloadCnt());
//            实际价格=单价x打折率
                BigDecimal actualPrice = unitPrice.multiply(BigDecimal.valueOf(discountRate));
//                结算数
                Integer settleCnt = (dataType == 1 ? mcd.getUv() : mcd.getRegisterCnt());
//            成本=单价x打折率x结算数
                BigDecimal cost = actualPrice.multiply(BigDecimal.valueOf(settleCnt));
//            UV成本=成本/UV数
                BigDecimal uvCost = mcd.getUv() == 0 ? BigDecimal.ZERO : cost.divide(BigDecimal.valueOf(mcd.getUv()), 2, RoundingMode.HALF_UP);
//            登陆产出比=下载登陆数(下载并登陆到app内的用户)/会员购买个数
                Float loginOutputRate = mcd.getUv() == 0 || vipOrderModel.getCnt() == 0 ? 0 : ((float) mcd.getUv()) / ((float) vipOrderModel.getCnt());

                mcd.setSettleRegisterCnt(settleRegisterCnt);
                mcd.setSettleRegisterRatio(settleRegisterRatio);
                mcd.setActualPrice(actualPrice.setScale(2, RoundingMode.HALF_UP));
                mcd.setCost(cost.setScale(2, RoundingMode.HALF_UP));
                mcd.setUvCost(uvCost);
                mcd.setLoginOutputRate(loginOutputRate);
            } else {
                if (dataType == 1) {
                    //UV
                    mcd.setUv((int) (uvTrackingCnts.size() * discountRate));
                } else if (dataType == 2) {
                    //CPA=消费量/转化量=（CPC*点击量）/（CVR*点击量）=CPC/CVR
                    if (vipOrderModel.getCnt() == null || mcd.getRegisterCnt() == 0) {
                        mcd.setCpa(0);
                    } else {
                        mcd.setCpa((int) (mcd.getRegisterCnt() * discountRate));
                    }
                }
            }
        });
    }

    public List<MngChannelExport> export(ChannelSearch search) {
        List<MngChannelExport> list = channelMapper.export(search);
        return list;
    }

    public List<SimpleChannelDTO> optionList() {
        return channelMapper.selectSimpleList();
    }

    public int uplow(Long id) {
        Channel channel = channelMapper.selectById(id);
        if (channel.getEnableStatus() == 1) {
            channelMapper.updateEnableStatusById(id, 2);
            return 2;
        } else if (channel.getEnableStatus() == 2) {
            channelMapper.updateEnableStatusById(id, 1);
            return 1;
        }
        return 0;
    }

    public MngChannelSettingDTO getSetting(Long channelId) {
        Channel channel = channelMapper.selectById(channelId);
        if (channel != null) {
            MngChannelSettingDTO settingDTO = new MngChannelSettingDTO();
            settingDTO.setChannelId(channel.getId());
            settingDTO.setDiscountRate(channel.getDiscountRate());
            settingDTO.setUnitPrice(channel.getUnitPrice());
            return settingDTO;
        }
        return null;
    }

    public void setting(MngChannelSettingBO settingBO) {
        Channel channel = channelMapper.selectById(settingBO.getChannelId());
        if (channel != null) {
            channel.setDiscountRate(settingBO.getDiscountRate());
            channel.setUnitPrice(settingBO.getUnitPrice());
            channel.setUpdatedDate(new Date());
            channelMapper.updateByPrimaryKey(channel);
        }
    }

    public void add(MngChannelBO channelBO) {
        int cnt = channelMapper.countByChannelCode(channelBO.getChannel());
        if (cnt > 0) {
            throw new AppServerException("该渠道已存在！");
        }
        Channel channel = ChannelConverter.INSTANCE.bo2do(channelBO);
        channel.setEnableStatus(SysConstants.EnableStatus.enable.getCode());
        channel.setCreatedDate(new Date());
        channelMapper.insert(channel);
    }

    public void edit(MngChannelBO channelBO) {
        Channel existChannel = channelMapper.selectByChannelCode(channelBO.getChannel());
        if (existChannel != null && !existChannel.getId().equals(channelBO.getId())) {
            throw new AppServerException(channelBO.getChannel() + "渠道已存在！");
        }

        Channel channel = ChannelConverter.INSTANCE.bo2do(channelBO);
        channel.setUpdatedDate(new Date());
        channelMapper.updateByPrimaryKeySelective(channel);
    }

    public MngChannelInfoDTO getInfoById(Long id) {
        Channel channel = channelMapper.selectById(id);
        MngChannelInfoDTO infoDTO = null;
        if (channel != null) {
            infoDTO = ChannelConverter.INSTANCE.do2infoDto(channel);
        }
        return infoDTO;
    }

}
