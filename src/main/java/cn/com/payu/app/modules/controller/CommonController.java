package cn.com.payu.app.modules.controller;

import cn.com.payu.app.common.exception.AppServerException;
import cn.com.payu.app.modules.service.SmsService;
import com.glsx.plat.common.annotation.NoLogin;
import com.glsx.plat.common.utils.SnowFlake;
import com.glsx.plat.common.utils.StringUtils;
import com.glsx.plat.context.properties.UploadProperties;
import com.glsx.plat.core.web.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.io.File;
import java.io.IOException;

@Slf4j
@RestController
@Api(value = "通用模块(短信、文件上传等)", tags = {"通用模块"})
public class CommonController {

    @Autowired
    private SmsService smsService;

    @Autowired
    private UploadProperties uploadProperties;

    @NoLogin
    @ApiOperation(value = "发送短信验证码接口")
    @GetMapping(value = "/common/sendCode")
    public R sendCode(@RequestParam("mobile") @Validated @NotBlank(message = "手机号码不能为空") String mobile, Integer fromApp) {
        if (fromApp == null) {
            fromApp = 1;
        }
        String code = smsService.sendCode(mobile, fromApp);
        return R.ok().data(code);
    }

    @ApiOperation(value = "上传文件接口")
    @PostMapping(value = "/common/file/upload", headers = "content-type=multipart/form-data")
    public R fileUpload(@RequestPart("file") MultipartFile file) throws IOException {
        String result = uploadFile(file);
        return R.ok().data(result);
    }

    @ApiOperation(value = "后台管理系统上传文件接口")
    @PostMapping(value = "/mng/file/upload", headers = "content-type=multipart/form-data")
    public R fileUpload2(@RequestPart("file") MultipartFile file) throws IOException {
        String result = uploadFile(file);
        return R.ok().data(result);
    }

    public String uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new AppServerException("上传文件不能为空");
        }

        String result;

        //获取文件存放路径
        String path = uploadProperties.getBasePath();

        File targetDir = new File(path);
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }

        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.indexOf("."));
        String newFilename = SnowFlake.nextId() + suffix;

        String tempPath = targetDir + File.separator + newFilename;

        // 转存文件
        File desc = new File(tempPath);
        FileUtils.copyInputStreamToFile(file.getInputStream(), desc);

        path = tempPath;
        if (StringUtils.isNotEmpty(path)) {
            result = uploadProperties.getFilepath() + newFilename;
        } else {
            result = "上传失败";
        }
        return result;
    }

}
