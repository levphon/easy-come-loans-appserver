package cn.com.payu.app.modules.utils;

import cn.com.payu.app.modules.model.AppUser;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

/**
 * @author payu
 */
@NoArgsConstructor
public class AppContextHolder {

    private static final ThreadLocal<AppUser> USER_THREAD_LOCAL = new ThreadLocal<>();

    // 获取当前用户
    public static AppUser getUser() {
        AppUser user = USER_THREAD_LOCAL.get();
        Assert.notNull(user, "获取当前用户信息失败");
        return user;
    }

    // 设置当前用户
    public static void setUser(AppUser user) {
        USER_THREAD_LOCAL.set(user);
    }

    // 移除当前用户
    public static void removeUser() {
        USER_THREAD_LOCAL.remove();
    }

    public static Long getUserId() {
        AppUser user = getUser();
        return user.getId();
    }

    public static String getUnionId() {
        AppUser user = getUser();
        return user.getUnionId();
    }

    public static String getAccount() {
        AppUser user = getUser();
        return user.getAccount();
    }

    public static String getChannel() {
        AppUser user = getUser();
        return user.getChannel();
    }

}
