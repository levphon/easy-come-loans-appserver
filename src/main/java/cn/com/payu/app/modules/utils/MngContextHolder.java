package cn.com.payu.app.modules.utils;

import cn.com.payu.app.modules.model.SysUser;
import com.google.common.collect.Sets;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.util.Collection;

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

    public static String getAccount() {
        SysUser user = getUser();
        return user.getAccount();
    }

    public static Collection<Long> getRoleIds() {
        return getUser().getRoleIds();
    }

    public static Collection<Long> getDeptIds() {
        return Sets.newHashSet(getUser().getDepartmentId());
    }

}
