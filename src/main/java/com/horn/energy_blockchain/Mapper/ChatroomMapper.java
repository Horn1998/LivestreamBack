package com.horn.energy_blockchain.Mapper;

import com.horn.energy_blockchain.Entity.Chatroom;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ChatroomMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Chatroom record);

    int insertSelective(Chatroom record);

    Chatroom selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Chatroom record);

    int updateByPrimaryKey(Chatroom record);

    @Select("SELECT * FROM chatroom")
    List<Chatroom> findAll();

    @Select("select COUNT(*) from chatroom where room=#{room}")
    int selectByRoom(String room);

    @Select("select COUNT(*) from chatroom where room=#{room} and name=#{name}")
    int selectByRoomAndName(String room, String name);

    @Select("delete from chatroom where room=#{room} and name=#{name}")
    void removeByRoomAndName(String room, String name);

}