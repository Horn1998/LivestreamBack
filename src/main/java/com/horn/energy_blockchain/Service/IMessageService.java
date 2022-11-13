package com.horn.energy_blockchain.Service;


import com.horn.energy_blockchain.Entity.Message;

import java.util.List;

public interface IMessageService {

    void saveMessage(Message message);

    List<Message> findAll();

    List<Message> findByRoom(String room);

    void removeByRoom(String room);

}
