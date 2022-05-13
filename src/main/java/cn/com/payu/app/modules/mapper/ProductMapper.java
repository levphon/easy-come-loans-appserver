package cn.com.payu.app.modules.mapper;

import cn.com.payu.app.modules.entity.Product;
import cn.com.payu.app.modules.model.MngProductDTO;
import cn.com.payu.app.modules.model.export.MngProductExport;
import cn.com.payu.app.modules.model.params.ProductSearch;
import com.glsx.plat.mybatis.mapper.CommonBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper extends CommonBaseMapper<Product> {

    Product selectById(@Param("id") Long id);

    int countByName(@Param("name") String name);

    Product selectByName(@Param("name") String name);

    List<Product> selectUsable(ProductSearch search);

    List<MngProductDTO> search(ProductSearch search);

    List<MngProductExport> export(ProductSearch search);

    int updateEnableStatusById(@Param("id") Long id, @Param("enableStatus") Integer enableStatus);

    int increaseUsedCntById(@Param("id") Long id);

}