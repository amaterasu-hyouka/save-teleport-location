package jp.amaterasu_hyouka.saveteleportlocation.repository;

import jp.amaterasu_hyouka.saveteleportlocation.model.LocationCategory;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LocationCategoryMapper {

    @Insert("""
        INSERT INTO location_categories (name, material_name, priority)
        VALUES (#{name}, #{materialName},COALESCE((SELECT MAX(priority) + 1 FROM location_categories), 1))
    """)
    void insert(LocationCategory locationCategory);

    @Select("SELECT id, name, material_name, priority FROM location_categories WHERE id = #{id}")
    LocationCategory findById(@Param("id") int id);

    @Select("SELECT id, name, material_name, priority FROM location_categories ORDER BY priority LIMIT #{limit} OFFSET #{offset}")
    List<LocationCategory> findAllInRange(@Param("offset") int offset, @Param("limit") int limit);

    @Select("SELECT priority FROM location_categories WHERE id = #{id}")
    Integer findPriorityById(@Param("id") int id);

    @Update("UPDATE location_categories SET name = #{name}, material_name = #{materialName}, priority = #{priority} WHERE id = #{id}")
    void update(LocationCategory locationCategory);

    @Update("UPDATE location_categories SET name = #{name} WHERE id = #{id}")
    void updateNameById(@Param("id") int id, @Param("name") String name);

    @Update("UPDATE location_categories SET material_name = #{materialName} WHERE id = #{id}")
    void updateMaterialNameById(@Param("id") int id, @Param("materialName") String materialName);

    @Update("UPDATE location_categories SET priority = #{priority} WHERE id = #{id}")
    void updatePriorityById(@Param("id") int id, @Param("priority") int priority);

    @Delete("DELETE FROM location_categories WHERE id = #{id}")
    void deleteById(@Param("id") int id);

}