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

    public void insertData(SaveTeleportLocation saveTeleportLocation){
        try (SqlSession session = factory.openSession()){
            SaveTeleportLocationMapper mapper = session.getMapper(SaveTeleportLocationMapper.class);
            mapper.insert(saveTeleportLocation);
            session.commit();
        }
    }

    public SaveTeleportLocation getById(int id){
        try (SqlSession session = factory.openSession()){
            SaveTeleportLocationMapper mapper = session.getMapper(SaveTeleportLocationMapper.class);
            return mapper.findById(id);
        }
    }

    public List<SaveTeleportLocation> getByGroupKey(String groupKey){
        try (SqlSession session = factory.openSession()){
            SaveTeleportLocationMapper mapper = session.getMapper(SaveTeleportLocationMapper.class);
            return mapper.findByGroupKey(groupKey);
        }
    }

    public Integer getPriorityById(int id){
        try (SqlSession session = factory.openSession()){
            SaveTeleportLocationMapper mapper = session.getMapper(SaveTeleportLocationMapper.class);
            return mapper.findPriorityById(id);
        }
    }

    public void update(SaveTeleportLocation saveTeleportLocation){
        try (SqlSession session = factory.openSession()){
            SaveTeleportLocationMapper mapper = session.getMapper(SaveTeleportLocationMapper.class);
            mapper.update(saveTeleportLocation);
            session.commit();
        }
    }

    public void updatePriorityById(int id, int priority){
        try (SqlSession session = factory.openSession()){
            SaveTeleportLocationMapper mapper = session.getMapper(SaveTeleportLocationMapper.class);
            mapper.updatePriorityById(id, priority);
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