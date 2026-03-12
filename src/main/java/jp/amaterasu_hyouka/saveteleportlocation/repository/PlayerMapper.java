package jp.amaterasu_hyouka.saveteleportlocation.repository;

import jp.amaterasu_hyouka.saveteleportlocation.model.LoginPlayer;
import org.apache.ibatis.annotations.*;

@Mapper
public interface PlayerMapper {

    @Insert("INSERT OR REPLACE INTO players (uuid, name) VALUES (#{uuid}, #{name})")
    void insertOrReplace(LoginPlayer loginPlayer);

    @Select("SELECT name FROM players WHERE uuid = #{uuid}")
    String findNameByUuid(@Param("uuid") String uuid);

}