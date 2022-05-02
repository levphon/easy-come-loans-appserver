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
public class MngCustUserService {

    @Resource
    private UserMapper custUserMapper;

    @Resource
    private UserProfileMapper custUserProfileMapper;

    public PageInfo<CustUserRegisterListDTO> registerSearch(CustUserSearch search) {
        Page page = PageHelper.startPage(search.getPageNumber(), search.getPageSize());
        List<CustUserRegisterListDTO> list = custUserProfileMapper.registerSearch(search);
        return new PageInfo<>(list);
    }

    public List<CustUserRegisterExport> registerExport(CustUserSearch search) {
        List<CustUserRegisterExport> list = custUserProfileMapper.registerExport(search);
        return list;
    }

}
