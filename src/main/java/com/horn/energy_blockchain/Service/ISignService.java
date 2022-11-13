package com.horn.energy_blockchain.Service;/*
 *@Author: horn
 *@DATE: 2022/11/12 0012 20:31
 *@Description
 *@Version 1.0
 */

public interface ISignService  {
    boolean checkSign(String room, String name, String date);

    void signForAll(String room, String name, String date);

    boolean checkRoom(String room);

    void stopSign(String room);

}
