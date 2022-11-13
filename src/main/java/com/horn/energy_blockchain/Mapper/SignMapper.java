package com.horn.energy_blockchain.Mapper;

import com.horn.energy_blockchain.Entity.Sign;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

@Mapper
@Service
public interface SignMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Sign record);

    int insertSelective(Sign record);

    Sign selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Sign record);

    int updateByPrimaryKey(Sign record);

    @Select("SELECT * from sign where admin_code=#{adminCode} and name=#{name}")
    Sign getSign(String adminCode, String name);

    @Select("SELECT admin_code from sign where name=#{room}")
    String getAdminCode(String room);

    @Select("SELECT COUNT(*) from sign where name=#{room}")
    int checkRoom(String room);

    @Delete("DELETE FROM SIGN WHERE name=#{room}")
    void stopSign(String room);
}