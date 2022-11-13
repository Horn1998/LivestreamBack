package com.horn.energy_blockchain.Service;/*
 *@Author: horn
 *@DATE: 2022/11/3 0003 21:32
 *@Description
 *@Version 1.0
 */

import com.horn.energy_blockchain.Entity.Chatroom;

import java.util.List;

public interface IRoomService {
    void saveRoom(String room, String userName);

    List<Chatroom> getRooms();

    //返回最终房间名，判断并且新建房间
    String judgeAndInsert(String name, String userName);

    //删除指定房间
    boolean removeRoom(String room, String name);

    int countByNameAndRoom(String room, String name);
}

