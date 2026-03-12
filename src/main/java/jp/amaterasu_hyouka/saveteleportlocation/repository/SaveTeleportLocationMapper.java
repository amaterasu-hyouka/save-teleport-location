package jp.amaterasu_hyouka.saveteleportlocation.repository;

import jp.amaterasu_hyouka.saveteleportlocation.model.SaveTeleportLocation;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SaveTeleportLocationMapper {

    @Insert("""
    INSERT INTO save_teleport_locations (name, group_key, material_name ,world ,x ,y , z, yaw, pitch, priority)
    VALUES (#{name}, #{groupKey}, #{materialName}, #{world}, #{x}, #{y}, #{z}, #{yaw}, #{pitch},
        COALESCE((SELECT MAX(priority) + 1 FROM save_teleport_locations), 1)
    )
    """)
    void insert(SaveTeleportLocation data);

    @Select("""
        SELECT id, name, group_key, material_name, world, x, y, z, yaw, pitch, priority
        FROM save_teleport_locations
        WHERE group_key = #{groupKey}
        ORDER BY priority
    """)
    List<SaveTeleportLocation> findByGroupKey(@Param("groupKey") String groupKey);

    @Select("SELECT id, name, group_key, material_name, world, x, y, z, yaw, pitch, priority FROM save_teleport_locations WHERE id = #{id}")
    SaveTeleportLocation findById(@Param("id") int id);

    @Select("SELECT priority FROM save_teleport_locations WHERE id = #{id}")
    Integer findPriorityById(@Param("id") int id);

    @Update("UPDATE save_teleport_locations SET name = #{name}, material_name = #{materialName}, meta = #{meta}, priority = #{priority} WHERE id = #{id}")
    void update(SaveTeleportLocation data);

    @Update("UPDATE save_teleport_locations SET priority = #{priority} WHERE id = #{id}")
    void updatePriorityById(@Param("id") int id, @Param("priority") int priority);

    @Delete("DELETE FROM save_teleport_locations WHERE id = #{id}")
    void deleteById(@Param("id") int id);

}