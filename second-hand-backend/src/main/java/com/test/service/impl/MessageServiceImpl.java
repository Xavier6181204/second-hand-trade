package com.test.service.impl;

import com.test.entity.Message;
import com.test.mapper.MessageMapper;
import com.test.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Override
    @Transactional
    public boolean sendMessage(Message message) {
        message.setStatus(0);
        return messageMapper.insert(message) > 0;
    }

    @Override
    public Message findById(Integer id) {
        return messageMapper.findById(id);
    }

    @Override
    public List<Message> findByToUserId(Integer toUserId) {
        return messageMapper.findByToUserId(toUserId);
    }

    @Override
    public List<Message> findByFromUserId(Integer fromUserId) {
        return messageMapper.findByFromUserId(fromUserId);
    }

    @Override
    public List<Message> findByProductId(Integer productId) {
        return messageMapper.findByProductId(productId);
    }

    @Override
    @Transactional
    public boolean replyMessage(Integer id, String reply) {
        return messageMapper.updateReply(id, reply, 1) > 0;
    }
}
