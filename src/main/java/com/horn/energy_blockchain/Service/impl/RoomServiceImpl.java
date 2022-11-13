package com.horn.energy_blockchain.Service.impl;/*
 *@Author: horn
 *@DATE: 2022/11/3 0003 21:35
 *@Description
 *@Version 1.0
 */

import com.horn.energy_blockchain.Entity.Chatroom;
import com.horn.energy_blockchain.Mapper.ChatroomMapper;
import com.horn.energy_blockchain.Service.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@Transactional
public class RoomServiceImpl implements IRoomService {
    @Autowired
    ChatroomMapper chatroomMapper;
    @Override
    public void saveRoom(String room, String userName) {
        try {

            Chatroom chatroom = new Chatroom();
            chatroom.setName(userName);
            chatroom.setRoom(room);
            chatroomMapper.insert(chatroom);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Chatroom> getRooms() {
        try {
            return chatroomMapper.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String judgeAndInsert(String room, String userName) {
        try {
            Random rand = new Random(25);
            int now = rand.nextInt(1000);
            room = String.valueOf(now);
            while(chatroomMapper.selectByRoom(room) > 0){
                now = rand.nextInt(1000);
                room = String.valueOf(now);
            }
            Chatroom chatroom = new Chatroom();
            chatroom.setName(userName);
            chatroom.setRoom(room);
            chatroomMapper.insert(chatroom);
            return room;
        } catch (Exception e) {
            e.printStackTrace();
            return room;
        }
    }

    @Override
    public boolean removeRoom(String room, String name) {
        if(chatroomMapper.selectByRoomAndName(room, name) == 1){
            chatroomMapper.removeByRoomAndName(room, name);
            return true;
        };
        return false;
    }

    @Override
    public int countByNameAndRoom(String room, String name) {
        return chatroomMapper.selectByRoomAndName(room, name);
    }

}
