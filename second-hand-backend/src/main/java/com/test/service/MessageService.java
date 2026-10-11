package com.test.service;

import com.test.entity.Message;
import java.util.List;

public interface MessageService {
    boolean sendMessage(Message message);
    Message findById(Integer id);
    List<Message> findByToUserId(Integer toUserId);
    List<Message> findByFromUserId(Integer fromUserId);
    List<Message> findByProductId(Integer productId);
    boolean replyMessage(Integer id, String reply);
}
