package cn.zhengsigen.mybatis.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
@Data
@AllArgsConstructor
public class Admin {
    private Integer adminId;
    private String username;
    private String password;
    private Date createTime;
    private Date updateTime;
}
