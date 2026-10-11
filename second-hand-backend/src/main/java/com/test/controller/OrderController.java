package com.test.controller;

import com.test.dto.Result;
import com.test.entity.Order;
import com.test.entity.User;
import com.test.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/order")
@CrossOrigin(origins = {"http://localhost:8080", "http://127.0.0.1:8080"}, allowCredentials = "true")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public Result<Order> create(@RequestParam Integer productId,
                                @RequestParam(required = false) String remark,
                                HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error(401, "请先登录");
        }

        try {
            Order order = orderService.createOrder(productId, user.getId(), remark);
            return Result.success(order);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/my-buy")
    public Result<List<Order>> myBuy(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error(401, "未登录");
        }
        return Result.success(orderService.findByBuyerId(user.getId()));
    }

    @GetMapping("/my-sell")
    public Result<List<Order>> mySell(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error(401, "未登录");
        }
        return Result.success(orderService.findBySellerId(user.getId()));
    }

    @PostMapping("/status")
    public Result<String> updateStatus(@RequestParam Integer id,
                                       @RequestParam Integer status,
                                       HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error(401, "未登录");
        }

        Order order = orderService.findById(id);
        if (order == null) {
            return Result.error("订单不存在");
        }

        // status=2: 卖家发货（待发货 → 待收货）
        if (status == 2) {
            if (!order.getSellerId().equals(user.getId())) {
                return Result.error("只有卖家可以发货");
            }
            if (order.getStatus() != 1) {
                return Result.error("订单状态错误，当前状态无法发货");
            }
        }
        // status=3: 买家确认收货（待收货 → 已完成）
        else if (status == 3) {
            if (!order.getBuyerId().equals(user.getId())) {
                return Result.error("只有买家可以确认收货");
            }
            if (order.getStatus() != 2) {
                return Result.error("订单状态错误，当前状态无法确认收货");
            }
        } else {
            return Result.error("不支持的状态变更");
        }

        boolean success = orderService.updateStatus(id, status);
        if (!success) {
            return Result.error("操作失败");
        }
        return Result.success("操作成功");
    }
}
