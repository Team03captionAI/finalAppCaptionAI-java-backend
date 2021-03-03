package com.fsnteam.fsnweb.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fsnteam.fsnweb.bean.UserRole;
import com.fsnteam.fsnweb.dao.UserMapper;
import com.fsnteam.fsnweb.entity.Users;
import com.fsnteam.fsnweb.handler.BussinessException;
import com.fsnteam.fsnweb.service.UsersService;
import com.fsnteam.fsnweb.util.Result;
import com.fsnteam.fsnweb.util.ReturnCode;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author StupidBear
 * @since 2020-11-23
 */
@RestController
@RequestMapping("/users")
@CrossOrigin
public class UsersController {

    @Autowired
    UsersService usersService;

    @Autowired
    UserMapper userMapper;

    /**
     * 分页查询
     *
     * @return
     */
    @PostMapping(value = "/getAllUsers", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "查询全部用户", notes = "")
    public Result getAllUsers(@RequestBody Map params) {
        return usersService.getAllUsers(params);
    }

    @GetMapping("getUserById/{id}")
    @ApiOperation(value = "查询单个用户信息", notes = "通过id查询对应用户信息")
    public Result getUserById(@PathVariable("id") Long id) {
        Users user = usersService.getById(id);
        if (user != null) {
            return Result.success().data("user", user);
        } else {
            throw new BussinessException(ReturnCode.USER_NOT_FOUND.getCode(), ReturnCode.USER_NOT_FOUND.getMessage());
        }
    }

    @PostMapping("insertUser")
    @ApiOperation(value = "插入一条新队员信息")
    public Result insertUser(@RequestBody Map params) {
        //从前端参数中取得user
        JSONObject jsonobject = JSONObject.parseObject(JSON.toJSONString(params.get("user")));
        Users user = JSON.toJavaObject(jsonobject,Users.class);
        usersService.save(user);
        UserRole userRole=new UserRole();
        userRole.setUserID(user.getId());
        userRole.setRole("ROLE_MEMBER");
        userMapper.insertRole(userRole);
        return Result.success().tip("添加新队员信息完成");
    }

    @PostMapping("updateUserById")
    @ApiOperation(value = "根据用户ID修改信息")
    public Result updateUserById(@RequestBody Map params){
        JSONObject jsonobject = JSONObject.parseObject(JSON.toJSONString(params.get("user")));
        Users user = JSON.toJavaObject(jsonobject,Users.class);
        usersService.updateById(user);
        return Result.success().tip("修改信息完成");
    }
}

