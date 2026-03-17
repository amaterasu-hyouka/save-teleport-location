package jp.amaterasu_hyouka.saveteleportlocation.repository;

import jp.amaterasu_hyouka.saveteleportlocation.model.SaveTeleportLocation;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SaveTeleportLocationMapper {

    @Insert("""
        INSERT INTO save_teleport_locations (name, player_uuid, player_name, category_id, material_name, world, x, y, z, yaw, pitch, player_priority, category_priority)
        VALUES (#{name}, #{playerUuid}, #{playerName}, #{categoryId}, #{materialName}, #{world}, #{x}, #{y}, #{z}, #{yaw}, #{pitch},
        COALESCE((SELECT MAX(player_priority) + 1 FROM save_teleport_locations WHERE player_uuid = #{playerUuid}), 1),
        COALESCE((SELECT MAX(category_priority) + 1 FROM save_teleport_locations WHERE category_id = #{categoryId}), 1))
    """)
    void insert(SaveTeleportLocation data);

    @Select("""
        SELECT id, name, player_uuid, player_name, category_id, material_name, world, x, y, z, yaw, pitch, player_priority, category_priority
        FROM save_teleport_locations
        WHERE player_uuid = #{playerUuid} AND category_id IS NULL
        ORDER BY player_priority
        LIMIT #{limit} OFFSET #{offset}
    """)
    List<SaveTeleportLocation> findByPlayerUuidInRange(@Param("playerUuid") String playerUuid, @Param("offset") int offset, @Param("limit") int limit);

    @Select("""
        SELECT id, name, player_uuid, player_name, category_id, material_name, world, x, y, z, yaw, pitch, player_priority, category_priority
        FROM save_teleport_locations
        WHERE category_id = #{categoryId}
        ORDER BY category_priority
        LIMIT #{limit} OFFSET #{offset}
    """)
    List<SaveTeleportLocation> findByCategoryIdInRange(@Param("categoryId") Integer categoryId, @Param("offset") int offset, @Param("limit") int limit);

    @Select("SELECT player_priority FROM save_teleport_locations WHERE id = #{id}")
    Integer findPlayerPriorityById(@Param("id") int id);

    @Select("SELECT category_priority FROM save_teleport_locations WHERE id = #{id}")
    Integer findCategoryPriorityById(@Param("id") int id);

    @Update("UPDATE save_teleport_locations SET name = #{name}, material_name = #{materialName}, player_priority = #{player_priority}, category_priority = #{category_priority} WHERE id = #{id}")
    void update(SaveTeleportLocation data);

    @Update("UPDATE save_teleport_locations SET name = #{name} WHERE id = #{id}")
    void updateNameById(@Param("id") int id, @Param("name") String name);

    @Update("UPDATE save_teleport_locations SET material_name = #{materialName} WHERE id = #{id}")
    void updateMaterialNameById(@Param("id") int id, @Param("materialName") String materialName);

    @Update("UPDATE save_teleport_locations SET player_priority = #{player_priority} WHERE id = #{id}")
    void updatePlayerPriorityById(@Param("id") int id, @Param("player_priority") int priority);

    @Update("UPDATE save_teleport_locations SET category_priority = #{category_priority} WHERE id = #{id}")
    void updateCategoryPriorityById(@Param("id") int id, @Param("category_priority") int priority);

    @Delete("DELETE FROM save_teleport_locations WHERE id = #{id}")
    void deleteById(@Param("id") int id);

}