package cn.zhengsigen.mybatis.demo.controller.admin;

import cn.zhengsigen.mybatis.demo.mapper.AdminMapper;
import cn.zhengsigen.mybatis.demo.model.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/dzy/admin")
public class AdminController {
    @Autowired
    AdminMapper adminMapper;

    @PostMapping("/login")
    public Admin adminLogin(@RequestBody Admin admin){
        return adminMapper.adminLogin(admin.getUsername(),admin.getPassword());
    }
}
