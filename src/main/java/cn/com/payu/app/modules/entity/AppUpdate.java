package cn.com.payu.app.modules.entity;

import com.glsx.plat.mybatis.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "t_app_update")
public class AppUpdate extends BaseEntity {

    /**
     * 客户端读取到的应用名称
     */
    private String name;

    /**
     * 客户端读取到的版本号信息
     */
    private String version;

    /**
     * 是否有更新
     */
    private Boolean update;

    /**
     * wgt 包的下载地址，用于 wgt 方式更新
     */
    @Column(name = "wgt_url")
    private String wgtUrl;

    /**
     * apk/ipa 包的下载地址或 AppStore 地址，用于整包升级的方式
     */
    @Column(name = "pkg_url")
    private String pkgUrl;

    /**
     * 描述
     */
    private String remark;

}