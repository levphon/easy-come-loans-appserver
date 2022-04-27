package cn.com.payu.app.config;

import cn.com.payu.app.common.constant.Constants;
import cn.com.payu.app.modules.model.SysUser;
import cn.com.payu.app.modules.service.MngUserService;
import cn.com.payu.app.modules.utils.MngContextHolder;
import com.alibaba.fastjson.JSON;
import com.glsx.plat.common.utils.ObjectUtils;
import com.glsx.plat.common.utils.StringUtils;
import com.glsx.plat.core.web.R;
import com.glsx.plat.exception.SystemMessage;
import com.glsx.plat.jwt.base.ComJwtUser;
import com.glsx.plat.jwt.util.JwtUtils;
import com.glsx.plat.web.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

/**
 * 授权验证拦截器
 *
 * @author payu
 */
@Slf4j
@Component
public class MngRequestAuthorizeInterceptor implements HandlerInterceptor {

    @Value("${spring.application.name}")
    private String application;

    @Resource
    private JwtUtils<ComJwtUser> jwtUtils;

    @Resource
    private MngUserService mngUserService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        MngContextHolder.setUser(null);
        MngContextHolder.removeUser();

        String contextPath = request.getContextPath();
        String uri = request.getRequestURI();
        if (!uri.contains(contextPath + "/mng/")) {
            return true;
        }

        String ip = IpUtils.getIpAddr(request);

        //##判断是否登录
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        // 1.判断请求是否携带token
        if (StringUtils.isBlank(token)) {
            // 不存在token数据
            log.warn("【访问拦截】来自{}的请求[{}] Header中未包含授权信息.[请求头{}]", ip, uri, HttpHeaders.AUTHORIZATION);
            needLogin(response);
            return false;
        }

        Map<String, Object> userMap = jwtUtils.parseClaim(token);
        ComJwtUser jwtUser = (ComJwtUser) ObjectUtils.mapToObject(userMap, ComJwtUser.class);

        boolean accessClientFlag = checkAccessClient(jwtUser);

//        if (!accessClientFlag) {
//            log.warn("【访问拦截】来自{}的请求[{}] Token来源{}验证不通过.", ip, uri, jwtUser.getApplication());
//            return false;
//        }

        SysUser user = mngUserService.getSysUserByUserId(Long.valueOf(jwtUser.getUserId()));
        if (user != null) {
            MngContextHolder.setUser(user);
            return true;
        }

        //需要登录
        needLogin(response);
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        MngContextHolder.setUser(null);
        MngContextHolder.removeUser();
    }

    /**
     * 检查token来源
     *
     * @param jwtUser
     * @return
     */
    private boolean checkAccessClient(ComJwtUser jwtUser) {
        if (jwtUser == null) {
            return false;
        }
        return Constants.MNG_SERVER_NAME.equals(jwtUser.getApplication());
    }

    private void needLogin(HttpServletResponse response) throws Exception {
        response.setContentType("application/json; charset=utf-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.write(JSON.toJSONString(R.error(SystemMessage.NOT_LOGIN.getCode(), SystemMessage.NOT_LOGIN.getMsg())));
            writer.flush();
        }
    }

    private void blacklist(HttpServletResponse response) throws Exception {
        response.setContentType("application/json; charset=utf-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.write(JSON.toJSONString(R.error(605, "您的账号被限制登录，请联系管理员！")));
            writer.flush();
        }
    }

}
