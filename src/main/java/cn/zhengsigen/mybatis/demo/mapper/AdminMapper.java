package cn.zhengsigen.mybatis.demo.mapper;

import cn.zhengsigen.mybatis.demo.model.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminMapper {
    Admin adminLogin(@Param("username")String username, @Param("password")String password);

}
