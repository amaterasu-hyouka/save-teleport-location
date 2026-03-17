package jp.amaterasu_hyouka.saveteleportlocation.repository;

import jp.amaterasu_hyouka.saveteleportlocation.db.mybatis.DatabaseRegistry;
import jp.amaterasu_hyouka.saveteleportlocation.model.SaveTeleportLocation;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class SaveTeleportLocationRepository {
    private static final SaveTeleportLocationRepository saveTeleportLocationRepository = new SaveTeleportLocationRepository();
    private SaveTeleportLocationRepository(){}
    public static SaveTeleportLocationRepository getInstance(){return saveTeleportLocationRepository;}

    private final SqlSessionFactory factory = DatabaseRegistry.SAVE_TELEPORT_LOCATION.getSqlSessionFactory();

    public void insert(SaveTeleportLocation saveTeleportLocation){
        try (SqlSession session = factory.openSession()){
            SaveTeleportLocationMapper mapper = session.getMapper(SaveTeleportLocationMapper.class);
            mapper.insert(saveTeleportLocation);
            session.commit();
        }
    }

    public List<SaveTeleportLocation> findByPlayerUuidInRange(String playerUuid, int offset, int limit){
        try (SqlSession session = factory.openSession()){
            SaveTeleportLocationMapper mapper = session.getMapper(SaveTeleportLocationMapper.class);
            return mapper.findByPlayerUuidInRange(playerUuid, offset, limit);
        }
    }
    public List<SaveTeleportLocation> getByCategoryIdInRange(Integer categoryId, int offset, int limit){
        try (SqlSession session = factory.openSession()){
            SaveTeleportLocationMapper mapper = session.getMapper(SaveTeleportLocationMapper.class);
            return mapper.findByCategoryIdInRange(categoryId, offset, limit);
        }
    }

    public Integer getPlayerPriorityById(int id){
        try (SqlSession session = factory.openSession()){
            SaveTeleportLocationMapper mapper = session.getMapper(SaveTeleportLocationMapper.class);
            return mapper.findPlayerPriorityById(id);
        }
    }
    public Integer getCategoryPriorityById(int id){
        try (SqlSession session = factory.openSession()){
            SaveTeleportLocationMapper mapper = session.getMapper(SaveTeleportLocationMapper.class);
            return mapper.findCategoryPriorityById(id);
        }
    }

    public void update(SaveTeleportLocation saveTeleportLocation){
        try (SqlSession session = factory.openSession()){
            SaveTeleportLocationMapper mapper = session.getMapper(SaveTeleportLocationMapper.class);
            mapper.update(saveTeleportLocation);
            session.commit();
        }
    }
    public void updateNameById(int id, String name){
        try (SqlSession session = factory.openSession()){
            SaveTeleportLocationMapper mapper = session.getMapper(SaveTeleportLocationMapper.class);
            mapper.updateNameById(id, name);
            session.commit();
        }
    }
    public void updateMaterialNameById(int id, String materialName){
        try (SqlSession session = factory.openSession()){
            SaveTeleportLocationMapper mapper = session.getMapper(SaveTeleportLocationMapper.class);
            mapper.updateMaterialNameById(id, materialName);
            session.commit();
        }
    }
    public void updatePlayerPriorityById(int id, int priority){
        try (SqlSession session = factory.openSession()){
            SaveTeleportLocationMapper mapper = session.getMapper(SaveTeleportLocationMapper.class);
            mapper.updatePlayerPriorityById(id, priority);
            session.commit();
        }
    }
    public void updateCategoryPriorityById(int id, int priority){
        try (SqlSession session = factory.openSession()){
            SaveTeleportLocationMapper mapper = session.getMapper(SaveTeleportLocationMapper.class);
            mapper.updateCategoryPriorityById(id, priority);
            session.commit();
        }
    }

    public void deleteById(int id){
        try (SqlSession session = factory.openSession()){
            SaveTeleportLocationMapper mapper = session.getMapper(SaveTeleportLocationMapper.class);
            mapper.deleteById(id);
            session.commit();
        }
    }
}