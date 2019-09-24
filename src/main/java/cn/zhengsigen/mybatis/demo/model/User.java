package cn.zhengsigen.mybatis.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class User{
    private Integer userId;
    private String username;
    private String cover;
    private Double balance;
    private Integer addrId;
    private Date createTime;
    private Date updateTime;
}