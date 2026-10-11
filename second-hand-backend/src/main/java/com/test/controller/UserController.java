package com.test.controller;

import com.test.dto.Result;
import com.test.entity.User;
import com.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = {"http://localhost:8080", "http://127.0.0.1:8080"}, allowCredentials = "true")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<User> login(@RequestParam String username,
                              @RequestParam String password,
                              HttpSession session) {
        User user = userService.login(username, password);
        if (user == null) {
            return Result.error("用户名或密码错误");
        }
        session.setAttribute("user", user);
        return Result.success(user);
    }

    @PostMapping("/register")
    public Result<String> register(@RequestBody User user) {
        boolean success = userService.register(user);
        if (!success) {
            return Result.error("用户名已存在");
        }
        return Result.success("注册成功");
    }

    @GetMapping("/info")
    public Result<User> getInfo(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error(401, "未登录");
        }
        return Result.success(userService.findById(user.getId()));
    }

    @PostMapping("/update")
    public Result<String> update(@RequestBody User user, HttpSession session) {
        User loginUser = (User) session.getAttribute("user");
        if (loginUser == null) {
            return Result.error(401, "未登录");
        }
        user.setId(loginUser.getId());
        boolean success = userService.updateUser(user);
        if (!success) {
            return Result.error("修改失败");
        }
        session.setAttribute("user", userService.findById(loginUser.getId()));
        return Result.success("修改成功");
    }

    @PostMapping("/logout")
    public Result<String> logout(HttpSession session) {
        session.removeAttribute("user");
        return Result.success("退出成功");
    }
}
