package cn.zhengsigen.mybatis.demo.mapper;

import cn.zhengsigen.mybatis.demo.model.Admin;
import cn.zhengsigen.mybatis.demo.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    List<User> getUserList();
    User getUserById(Integer id);
    void delUserById(Integer id);
    void delMultipleUser(List<Integer>ids);
    void addUser(User user);
    void addMultipleUser(List<User> users);
    void updateUser(User user);
 }
