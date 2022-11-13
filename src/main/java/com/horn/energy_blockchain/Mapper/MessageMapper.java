package com.horn.energy_blockchain.Mapper;

import com.horn.energy_blockchain.Entity.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MessageMapper {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO message VALUES (#{id},#{content},#{size},#{color},#{bold},#{italic}," +
            " #{position},#{ip},#{name},#{time},#{available},#{room})")
    void saveMessage(Message message);

    @Select("SELECT * FROM message")
    List<Message> findAll();

    @Select("SELECT * from message where room=#{room}")
    List<Message> findByRoom(String room);

    @Select("delete from message where room=#{room}")
    void removeByRoom(String room);
}
