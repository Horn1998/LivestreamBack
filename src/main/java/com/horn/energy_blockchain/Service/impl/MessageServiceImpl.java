package com.horn.energy_blockchain.Service.impl;

import com.horn.energy_blockchain.Entity.Message;
import com.horn.energy_blockchain.Mapper.MessageMapper;
import com.horn.energy_blockchain.Service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author DELL
 * @date 2020/5/20 21:22
 */
@Service
@Transactional
public class MessageServiceImpl implements IMessageService {

    @Autowired
    MessageMapper messageMapper;

    @Override
    public void saveMessage(Message message) {
        messageMapper.saveMessage(message);
    }

    @Override
    public List<Message> findAll() {
        return messageMapper.findAll();
    }

    @Override
    public List<Message> findByRoom(String room) {
        List<Message> results = messageMapper.findByRoom(room);
        if(results.size() < 5 ) return  results;
        return results.subList(results.size()-5, results.size());
    }

    @Override
    public void removeByRoom(String room) {
       messageMapper.removeByRoom(room);
    }
}
