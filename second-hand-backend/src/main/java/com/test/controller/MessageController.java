package com.test.controller;

import com.test.dto.Result;
import com.test.entity.Message;
import com.test.entity.User;
import com.test.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/message")
@CrossOrigin(origins = {"http://localhost:8080", "http://127.0.0.1:8080"}, allowCredentials = "true")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public Result<String> send(@RequestParam Integer productId,
                               @RequestParam Integer toUserId,
                               @RequestParam String content,
                               HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error(401, "请先登录");
        }
        if (user.getId().equals(toUserId)) {
            return Result.error("不能给自己留言");
        }

        Message message = new Message();
        message.setProductId(productId);
        message.setFromUserId(user.getId());
        message.setToUserId(toUserId);
        message.setContent(content);

        boolean success = messageService.sendMessage(message);
        if (!success) {
            return Result.error("发送失败");
        }
        return Result.success("发送成功");
    }

    @GetMapping("/received")
    public Result<List<Message>> received(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error(401, "请先登录");
        }
        return Result.success(messageService.findByToUserId(user.getId()));
    }

    @GetMapping("/sent")
    public Result<List<Message>> sent(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error(401, "请先登录");
        }
        return Result.success(messageService.findByFromUserId(user.getId()));
    }

    @GetMapping("/product/{productId}")
    public Result<List<Message>> byProduct(@PathVariable Integer productId) {
        return Result.success(messageService.findByProductId(productId));
    }

    @PostMapping("/reply")
    public Result<String> reply(@RequestParam Integer id,
                                @RequestParam String reply,
                                HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error(401, "请先登录");
        }

        Message message = messageService.findById(id);
        if (message == null) {
            return Result.error("留言不存在");
        }
        if (!message.getToUserId().equals(user.getId())) {
            return Result.error("无权回复此留言");
        }

        boolean success = messageService.replyMessage(id, reply);
        if (!success) {
            return Result.error("回复失败");
        }
        return Result.success("回复成功");
    }
}
