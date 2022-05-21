package cn.com.payu.app.modules.utils;

import cn.com.payu.app.modules.entity.Department;
import cn.com.payu.app.modules.entity.Role;
import cn.com.payu.app.modules.entity.Tenant;
import cn.com.payu.app.modules.model.SysUser;
import com.google.common.collect.Sets;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author payu
 */
@NoArgsConstructor
public class MngContextHolder {

    public static final Long ADMIN_ID = 1L;
    public static final Long ADMIN_ROLE_ID = 1L;

    private static final ThreadLocal<SysUser> USER_THREAD_LOCAL = new ThreadLocal<>();

    // 获取当前用户
    public static SysUser getUser() {
        SysUser user = USER_THREAD_LOCAL.get();
        Assert.notNull(user, "获取当前用户信息失败");
        return user;
    }

    // 设置当前用户
    public static void setUser(SysUser user) {
        USER_THREAD_LOCAL.set(user);
    }

    // 移除当前用户
    public static void removeUser() {
        USER_THREAD_LOCAL.remove();
    }

    public static boolean isSuperAdmin() {
        return getUser().getIsAdmin();
    }

    public static Long getUserId() {
        SysUser user = getUser();
        return user.getUserId();
    }

    public static Collection<Long> getRoleIds() {
        return getUser().getRoles().stream().map(Role::getId).collect(Collectors.toSet());
    }

    public static Role getRole() {
        Role role = getUser().getRoles().get(0);
        Assert.notNull(role, "获取当前用户角色信息失败");
        return role;
    }

    public static Long getRoleId() {
        return getRole().getId();
    }

    public static Integer getRolePermissionType() {
        return getRole().getRolePermissionType();
    }

    public static String getAccount() {
        SysUser user = getUser();
        return user.getAccount();
    }


    public static Tenant getTenant() {
        SysUser user = getUser();
        Tenant tenant = user.getTenant();
        Assert.notNull(tenant, "获取当前用户租户信息失败");
        return tenant;
    }

    public static Long getTenantId() {
        return getTenant().getId();
    }

    public static Department getDepartment() {
        SysUser user = getUser();
        Department dept = user.getDepartment();
        Assert.notNull(dept, "获取当前用户部门信息失败");
        return dept;
    }

    public static Long getDepartmentId() {
        return getDepartment().getId();
    }

    public static Set<Long> getVisibleCreatorIds() {
        return Sets.newHashSet();
    }

}
