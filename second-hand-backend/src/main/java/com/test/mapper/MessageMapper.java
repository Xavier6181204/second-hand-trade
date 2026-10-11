package com.test.mapper;

import com.test.entity.Message;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface MessageMapper {
    int insert(Message message);
    Message findById(Integer id);
    List<Message> findByToUserId(Integer toUserId);
    List<Message> findByFromUserId(Integer fromUserId);
    List<Message> findByProductId(Integer productId);
    int updateReply(@Param("id") Integer id, @Param("reply") String reply, @Param("status") Integer status);
}
