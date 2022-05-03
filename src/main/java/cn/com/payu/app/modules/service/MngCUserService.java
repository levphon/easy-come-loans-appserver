package cn.com.payu.app.modules.service;

import cn.com.payu.app.modules.mapper.UserMapper;
import cn.com.payu.app.modules.mapper.UserProfileMapper;
import cn.com.payu.app.modules.model.MngCUserDTO;
import cn.com.payu.app.modules.model.export.MngCUserExport;
import cn.com.payu.app.modules.model.params.CustUserSearch;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MngCUserService {

    @Resource
    private UserMapper custUserMapper;

    @Resource
    private UserProfileMapper custUserProfileMapper;

    public PageInfo<MngCUserDTO> search(CustUserSearch search) {
        Page page = PageHelper.startPage(search.getPageNumber(), search.getPageSize());
        List<MngCUserDTO> list = custUserProfileMapper.search(search);
        return new PageInfo<>(list);
    }

    public List<MngCUserExport> export(CustUserSearch search) {
        List<MngCUserExport> list = custUserProfileMapper.export(search);
        return list;
    }

}
