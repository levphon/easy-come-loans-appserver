package cn.com.payu.app.modules.controller;

import cn.com.payu.app.modules.model.ProductDTO;
import cn.com.payu.app.modules.service.ProductService;
import com.glsx.plat.core.web.R;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/product")
@Api(value = "产品模块", tags = {"产品模块"})
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping(value = "/list")
    public R list(String cityCode) {
        List<ProductDTO> items = productService.list(cityCode);
        return R.ok().data(items);
    }

    @GetMapping(value = "/click")
    public R click(@RequestParam("productId") Long productId) {
        String url = productService.click(productId);
        return R.ok().data(url);
    }

}

