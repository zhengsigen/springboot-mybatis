package cn.zhengsigen.mybatis.demo.controller.user;

import cn.zhengsigen.mybatis.demo.mapper.UserMapper;
import cn.zhengsigen.mybatis.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/dzy/user")
public class UserController {
    @Autowired
    UserMapper userMapper;

    @GetMapping
    public List<User> getUserList() {
        return userMapper.getUserList();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Integer id) {
        return userMapper.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public String delUserById(@PathVariable("id") Integer id) {
        userMapper.delUserById(id);
        return "删除成功";
    }

    @DeleteMapping()
    public String delMultipleUser(@RequestParam("ids") List<Integer> ids) {
        userMapper.delMultipleUser(ids);
        return "删除多个成功";
    }

    @PostMapping
    public String addUser(@RequestBody User user) {
        userMapper.addUser(user);
        return "新增成功";
    }

    @PostMapping("/multiple/add")
    public String addMultipleUser(@RequestBody List<User> users) {
        userMapper.addMultipleUser(users);
        return "新增多个用户成功";
    }

    @PutMapping
    public String updateUser(@RequestBody User user) {
        userMapper.updateUser(user);
        return "修改完成";
    }

}
