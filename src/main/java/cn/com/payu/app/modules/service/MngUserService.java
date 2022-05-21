package cn.com.payu.app.modules.service;

import cn.com.payu.app.common.exception.AppServerException;
import cn.com.payu.app.modules.entity.*;
import cn.com.payu.app.modules.mapper.*;
import cn.com.payu.app.modules.model.MngUserDTO;
import cn.com.payu.app.modules.model.SysUser;
import cn.com.payu.app.modules.model.params.MngUserBO;
import cn.com.payu.app.modules.model.params.MngUserSearch;
import cn.com.payu.app.modules.utils.MngContextHolder;
import cn.hutool.core.lang.UUID;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.glsx.plat.common.model.DropOptions;
import com.glsx.plat.common.utils.ObjectUtils;
import com.glsx.plat.common.utils.StringUtils;
import com.glsx.plat.exception.SystemMessage;
import com.glsx.plat.jwt.base.ComJwtUser;
import com.glsx.plat.jwt.util.JwtUtils;
import com.glsx.plat.web.utils.SessionUtils;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MngUserService {

    @Resource
    private MngUserMapper mngUserMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private TenantMapper tenantMapper;

    @Resource
    private DepartmentMapper departmentMapper;

    @Resource
    private UserRoleRelationMapper userRoleRelationMapper;

    @Resource
    private HashedCredentialsMatcher hcm;

    @Resource
    private JwtUtils<ComJwtUser> jwtUtils;

    public MngUser findByAccount(String account) {
        return mngUserMapper.selectByAccount(account);
    }

    public PageInfo<MngUserDTO> search(MngUserSearch search) {

        Page page = PageHelper.startPage(search.getPageNumber(), search.getPageSize());
        List<MngUser> userList = mngUserMapper.selectList(search);

        List<MngUserDTO> userDTOList = userListAssembled(userList);

        PageInfo<MngUserDTO> pageInfo = new PageInfo<>(userDTOList);
        pageInfo.setPages(page.getPages());//总页数
        pageInfo.setTotal(page.getTotal());//总条数
        return pageInfo;
    }

    public List<MngUserDTO> userListAssembled(List<MngUser> userList) {
        List<MngUserDTO> userDTOList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(userList)) {
            return userDTOList;
        }

        List<Long> departmentIdList = userList.stream().map(MngUser::getDepartmentId).collect(Collectors.toList());

        List<Department> departmentList = departmentMapper.selectByIds(departmentIdList);

        Map<Long, Department> departmentMap = departmentList.stream().collect(Collectors.toMap(Department::getId, d -> d));

        for (MngUser user : userList) {
            MngUserDTO userDTO = new MngUserDTO();
            BeanUtils.copyProperties(user, userDTO);
            Department department = departmentMap.get(user.getDepartmentId());
            userDTO.setDepartmentName(Optional.ofNullable(department).map(d -> department.getDepartmentName()).orElse(""));
            userDTOList.add(userDTO);
        }
        return userDTOList;
    }

    public MngUserDTO userInfo(Long userId) {
        MngUserDTO userDTO = null;

        MngUser user = mngUserMapper.selectById(userId);
        if (user != null) {
            userDTO = new MngUserDTO();
            BeanUtils.copyProperties(user, userDTO);

            List<UserRoleRelation> userRoleRelations = userRoleRelationMapper.selectUserRoleRelationList(new UserRoleRelation().setUserId(userId));
            if (CollectionUtils.isNotEmpty(userRoleRelations)) {
                Long roleId = userRoleRelations.get(0).getRoleId();
                userDTO.setRoleId(roleId);

                Role role = roleMapper.selectById(roleId);
                userDTO.setRoleName(role != null ? role.getRoleName() : "");
            }
        }
        return userDTO;
    }

    /**
     * 生成带用户信息的token
     *
     * @param user
     * @return
     */
    public String createToken(MngUser user) {
        String uuid = UUID.randomUUID().toString(); //JWT 随机ID,做为验证的key
        String jwtId = jwtUtils.getApplication() + ":" + uuid + "_" + jwtUtils.JWT_SESSION_PREFIX + user.getId();
        ComJwtUser jwtUser = new ComJwtUser();
        jwtUser.setApplication(jwtUtils.getApplication());
        jwtUser.setJwtId(jwtId);
        jwtUser.setUserId(String.valueOf(user.getId()));
        jwtUser.setAccount(user.getAccount());

        Map<String, Object> userMap = (Map<String, Object>) ObjectUtils.objectToMap(jwtUser);

        return jwtUtils.createToken(userMap);
    }

    /**
     * 生成带用户信息的token
     *
     * @param user
     * @param application 管理系统后台服务和App后台服务合在一起时，可以指定不同值用以区分，验证token来源合法性
     * @return
     */
    public String createToken(MngUser user, String application) {
        String uuid = UUID.randomUUID().toString(); //JWT 随机ID,做为验证的key
        String jwtId = application + ":" + uuid + "_" + jwtUtils.JWT_SESSION_PREFIX + user.getId();
        ComJwtUser jwtUser = new ComJwtUser();
        jwtUser.setApplication(application);
        jwtUser.setJwtId(jwtId);
        jwtUser.setUserId(String.valueOf(user.getId()));
        jwtUser.setAccount(user.getAccount());

        Map<String, Object> userMap = (Map<String, Object>) ObjectUtils.objectToMap(jwtUser);

        return jwtUtils.createToken(userMap);
    }

    /**
     * 从redis删除token缓存
     */
    public void removeToken() {
        String token = SessionUtils.request().getHeader(HttpHeaders.AUTHORIZATION);
        jwtUtils.destroyToken(token);
    }

    public SysUser getSysUserByUserId(Long userId) {
        MngUser user = mngUserMapper.selectById(userId);
        if (user != null) {
            SysUser sysUser = new SysUser();
            sysUser.setUserId(user.getId());
            sysUser.setAccount(user.getAccount());
            sysUser.setUsername(user.getUsername());
            sysUser.setPhoneNumber(user.getPhoneNumber());
            sysUser.setIsAdmin(user.getIsAdmin() == 1);
            sysUser.setDataType(user.getDataType());
            sysUser.setDepartmentId(user.getDepartmentId());

            Set<Long> roleIds = userRoleRelationMapper.selectRoleIdsByUserId(userId);
            List<Role> roleList = roleMapper.selectByIds(roleIds);
            sysUser.setRoles(roleList);

            Tenant tenant = tenantMapper.selectById(user.getTenantId());
            sysUser.setTenant(tenant);

            Department department = departmentMapper.selectById(sysUser.getDepartmentId());
            sysUser.setDepartment(department);
            return sysUser;
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public void addUser(MngUserBO userBO) {

        //检查用户关键信息
        checkUser(userBO);

        //校验角色
        checkUserRole(userBO);

        MngUser user = new MngUser(true);
        BeanUtils.copyProperties(userBO, user);
        user.setIsAdmin(0);

        //生成加密密码
        generateAndSetPassword(user, userBO.getPassword());
        mngUserMapper.insertUseGeneratedKeys(user);

        Long userId = user.getId();
        userRoleRelationMapper.insert(new UserRoleRelation(true).setUserId(userId).setRoleId(userBO.getRoleId()));
    }

    @Transactional(rollbackFor = Exception.class)
    public void editUser(MngUserBO userBO) {

        //检查用户关键信息
        checkUser(userBO);

        //校验角色
        checkUserRole(userBO);

        MngUser user = mngUserMapper.selectById(userBO.getId());

        String oldPassword = user.getPassword();

        BeanUtils.copyProperties(userBO, user);

        //如果填了密码，修改密码
        boolean changePwd = StringUtils.isNotBlank(userBO.getPassword());
        if (changePwd) {
            generateAndSetPassword(user, userBO.getPassword());
        } else {
            user.setPassword(oldPassword);
        }
        mngUserMapper.updateByPrimaryKey(user);

        //更新角色关系
        List<UserRoleRelation> relationList = userRoleRelationMapper.selectUserRoleRelationList(new UserRoleRelation().setUserId(user.getId()));
        if (CollectionUtils.isNotEmpty(relationList)) {
            UserRoleRelation relation = relationList.get(0);
            if (!relation.getRoleId().equals(userBO.getRoleId())) {
                relation.setRoleId(userBO.getRoleId());
                relation.setUpdatedBy(MngContextHolder.getUserId());
                relation.setUpdatedDate(new Date());
                userRoleRelationMapper.updateByPrimaryKeySelective(relation);
            }
        }
    }

    /**
     * 检查用户关键信息
     *
     * @param userBO
     */
    private void checkUser(MngUserBO userBO) {
        if (userBO.getId() == null) {
            int cnt = mngUserMapper.selectCntByAccount(userBO.getAccount());
            if (cnt > 0) {
                throw new AppServerException(SystemMessage.FAILURE.getCode(), "相同账号已存在");
            }
        } else {
            MngUser dbUser = mngUserMapper.selectByAccount(userBO.getAccount());
            if (dbUser != null && !dbUser.getId().equals(userBO.getId())) {
                throw new AppServerException(SystemMessage.FAILURE.getCode(), "相同账号已存在");
            }
        }
    }

    /**
     * 检查用户角色权限
     *
     * @param userBO
     */
    private void checkUserRole(MngUserBO userBO) {
        Role role = roleMapper.selectById(userBO.getRoleId());
        if (role == null) {
            throw new AppServerException(SystemMessage.FAILURE.getCode(), "角色已停用或删除");
        }
    }

    /**
     * 验证密码
     *
     * @param user
     * @param inputPassword
     * @return
     */
    public boolean verifyPassword(MngUser user, String inputPassword) {
        String dbPassword = user.getPassword();
        String salt = user.getSalt();
        SimpleHash hash = new SimpleHash(hcm.getHashAlgorithmName(), inputPassword, salt, hcm.getHashIterations());
        return dbPassword.equals(hash.toString());
    }

    /**
     * 生成密码
     *
     * @param user
     */
    private void generateAndSetPassword(MngUser user, String newPassword) {
        String salt = StringUtils.generateRandomCode(false, 4);
        SimpleHash hash = new SimpleHash(hcm.getHashAlgorithmName(), newPassword, salt, hcm.getHashIterations());
        user.setSalt(salt);
        user.setPassword(hash.toString());
    }

    @Transactional(rollbackFor = Exception.class)
    public int logicDeleteById(Long id) {
        if (MngContextHolder.ADMIN_ID.equals(id)) {
            throw new AppServerException(SystemMessage.FAILURE.getCode(), "管理员账号不能被删除");
        }
        return mngUserMapper.logicDeleteById(id);
    }

    public List<DropOptions> options(String username) {
        if (MngContextHolder.isSuperAdmin()) {
            return mngUserMapper.selectOptions(username);
        } else {
            Set<Long> userIdSet = MngContextHolder.getVisibleCreatorIds();
            List<DropOptions> permList = Lists.newArrayList();
            List<DropOptions> list = mngUserMapper.selectOptions(username);
            list.forEach(option -> {
                if (userIdSet.contains(option.getId())) {
                    permList.add(option);
                }
            });
            return permList;
        }
    }

}
