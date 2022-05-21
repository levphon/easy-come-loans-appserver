package cn.com.payu.app.modules.converter;

import cn.com.payu.app.modules.entity.Department;
import cn.com.payu.app.modules.model.view.DepartmentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DepartmentConverter {

    DepartmentConverter INSTANCE = Mappers.getMapper(DepartmentConverter.class);

    DepartmentDTO do2dto(Department department);

}
