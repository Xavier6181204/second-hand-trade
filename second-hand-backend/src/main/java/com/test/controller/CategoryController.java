package com.test.controller;

import com.test.dto.Result;
import com.test.entity.Category;
import com.test.entity.User;
import com.test.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/category")
@CrossOrigin(origins = {"http://localhost:8080", "http://127.0.0.1:8080"}, allowCredentials = "true")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public Result<List<Category>> list() {
        return Result.success(categoryService.findAll());
    }

    @PostMapping("/add")
    public Result<String> add(@RequestBody Category category, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            return Result.error(403, "需要管理员权限");
        }
        boolean success = categoryService.addCategory(category);
        if (!success) {
            return Result.error("添加失败");
        }
        return Result.success("添加成功");
    }

    @PostMapping("/update")
    public Result<String> update(@RequestBody Category category, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            return Result.error(403, "需要管理员权限");
        }
        boolean success = categoryService.updateCategory(category);
        if (!success) {
            return Result.error("修改失败");
        }
        return Result.success("修改成功");
    }

    @PostMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Integer id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            return Result.error(403, "需要管理员权限");
        }
        boolean success = categoryService.deleteCategory(id);
        if (!success) {
            return Result.error("删除失败");
        }
        return Result.success("删除成功");
    }
}
