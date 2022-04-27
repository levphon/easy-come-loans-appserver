package cn.com.payu.app.modules.service;

import cn.com.payu.app.modules.mapper.UserMapper;
import cn.com.payu.app.modules.mapper.UserProfileMapper;
import cn.com.payu.app.modules.model.CustUserRegisterListDTO;
import cn.com.payu.app.modules.model.export.CustUserRegisterExport;
import cn.com.payu.app.modules.model.params.CustUserSearch;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CustUserService {

    @Resource
    private UserMapper custUserMapper;

    @Resource
    private UserProfileMapper custUserProfileMapper;

    public PageInfo<CustUserRegisterListDTO> registerSearch(CustUserSearch search) {
        Page page = PageHelper.startPage(search.getPageNumber(), search.getPageSize());
        List<CustUserRegisterListDTO> list = custUserProfileMapper.registerSearch(search);
        list.forEach(curd -> {
            curd.setVipStatusStr("非会员");
            if (curd.getVipStatus() != null) {
                if ((curd.getVipStatus() == 1 || curd.getVipStatus() == 2) && curd.getVipExpireDays() <= 0) {
                    if (curd.getVipDays() <= 31) {
                        curd.setVipStatusStr("体验会员");
                    }
                    if (curd.getVipDays() >= 365) {
                        curd.setVipStatusStr("正式会员");
                    }
                    if (curd.getDelFlag() < 0) {
                        curd.setVipStatusStr("注销会员");
                    }
                } else {
                    curd.setVipStatusStr("过期会员");
                }
            }
        });
        return new PageInfo<>(list);
    }

    public List<CustUserRegisterExport> registerExport(CustUserSearch search) {
        List<CustUserRegisterExport> list = custUserProfileMapper.registerExport(search);
        list.forEach(curd -> {
            curd.setVipStatusStr("非会员");
            if (curd.getVipStatus() != null) {
                if ((curd.getVipStatus() == 1 || curd.getVipStatus() == 2) && curd.getVipExpireDays() <= 0) {
                    if (curd.getVipDays() <= 31) {
                        curd.setVipStatusStr("体验会员");
                    }
                    if (curd.getVipDays() >= 365) {
                        curd.setVipStatusStr("正式会员");
                    }
                    if (curd.getDelFlag() < 0) {
                        curd.setVipStatusStr("注销会员");
                    }
                } else {
                    curd.setVipStatusStr("过期会员");
                }
            }
        });
        return list;
    }

//    public CustUserDetailsDTO getCustUserDetails(Long cuserId) {
//        CustUserProfileModel profileDTO = this.getCustUserProfile(cuserId);
//
//        CustUserDetailsDTO detailsDTO = new CustUserDetailsDTO();
//        detailsDTO.setProfile(profileDTO);
//        return detailsDTO;
//    }
//
//    public CustUserProfileDTO getCustUserProfile(Long cuserId) {
//        CustUser custUser = custUserMapper.selectByCuserId(cuserId);
//
//        CustUserProfile custUserProfile = custUserProfileMapper.selectByCuserId(cuserId);
//
//
//        CustUserProfileDTO userProfileDTO = new CustUserProfileDTO();
//        BeanUtils.copyProperties(custUserProfile, userProfileDTO);
//        userProfileDTO.setCuserId(cuserId);
//        userProfileDTO.setUsername(custUserLocalAccount.getUsername());
//        userProfileDTO.setAvatar(custUserLocalAccount.getAvatar());
//        userProfileDTO.setUnionId(custUserLocalAccount.getUnionId());
//        userProfileDTO.setAccount(custUser.getAccount());
//        userProfileDTO.setAge(DateUtils.getAgeByBirth(userProfileDTO.getBirthday()));
//        userProfileDTO.setStatus(custUser.getBlacklist());
//        userProfileDTO.setRegisterDate(custUserLocalAccount.getCreatedDate());
//        return userProfileDTO;
//    }

}
