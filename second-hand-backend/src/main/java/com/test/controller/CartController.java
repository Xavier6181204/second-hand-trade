package com.test.controller;

import com.test.dto.Result;
import com.test.entity.Cart;
import com.test.entity.User;
import com.test.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = {"http://localhost:8080", "http://127.0.0.1:8080"}, allowCredentials = "true")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public Result<String> add(@RequestParam Integer productId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error(401, "请先登录");
        }

        boolean success = cartService.addToCart(user.getId(), productId);
        if (!success) {
            return Result.error("添加失败，商品可能已下架或已在购物车中");
        }
        return Result.success("添加成功");
    }

    @PostMapping("/remove")
    public Result<String> remove(@RequestParam Integer id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error(401, "请先登录");
        }
        boolean success = cartService.removeFromCart(id);
        if (!success) {
            return Result.error("删除失败");
        }
        return Result.success("删除成功");
    }

    @PostMapping("/removeByProduct")
    public Result<String> removeByProduct(@RequestParam Integer productId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error(401, "请先登录");
        }
        boolean success = cartService.removeFromCartByUserAndProduct(user.getId(), productId);
        if (!success) {
            return Result.error("删除失败");
        }
        return Result.success("删除成功");
    }

    @GetMapping("/my")
    public Result<List<Cart>> my(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error(401, "请先登录");
        }
        return Result.success(cartService.findByUserId(user.getId()));
    }

    @GetMapping("/count")
    public Result<Integer> count(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.success(0);
        }
        return Result.success(cartService.countByUserId(user.getId()));
    }
}
