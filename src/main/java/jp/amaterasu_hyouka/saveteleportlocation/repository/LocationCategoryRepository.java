package jp.amaterasu_hyouka.saveteleportlocation.repository;

import jp.amaterasu_hyouka.saveteleportlocation.db.mybatis.DatabaseRegistry;
import jp.amaterasu_hyouka.saveteleportlocation.model.LocationCategory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class LocationCategoryRepository {
    private static final LocationCategoryRepository locationCategoryRepository = new LocationCategoryRepository();
    private LocationCategoryRepository(){}
    public static LocationCategoryRepository getInstance(){return locationCategoryRepository;}

    private final SqlSessionFactory factory = DatabaseRegistry.SAVE_TELEPORT_LOCATION.getSqlSessionFactory();

    public void insert(LocationCategory locationCategory){
        try (SqlSession session = factory.openSession()){
            LocationCategoryMapper mapper = session.getMapper(LocationCategoryMapper.class);
            mapper.insert(locationCategory);
            session.commit();
        }
    }

    public List<LocationCategory> getAll(){
        try (SqlSession session = factory.openSession()){
            LocationCategoryMapper mapper = session.getMapper(LocationCategoryMapper.class);
            return mapper.findAll();
        }
    }

    public LocationCategory getById(int id){
        try (SqlSession session = factory.openSession()){
            LocationCategoryMapper mapper = session.getMapper(LocationCategoryMapper.class);
            return mapper.findById(id);
        }
    }

    public Integer getPriorityById(int id){
        try (SqlSession session = factory.openSession()){
            LocationCategoryMapper mapper = session.getMapper(LocationCategoryMapper.class);
            return mapper.findPriorityById(id);
        }
    }

    public void update(LocationCategory locationCategory){
        try (SqlSession session = factory.openSession()){
            LocationCategoryMapper mapper = session.getMapper(LocationCategoryMapper.class);
            mapper.update(locationCategory);
            session.commit();
        }
    }

    public void updatePriorityById(int id, int priority){
        try (SqlSession session = factory.openSession()){
            LocationCategoryMapper mapper = session.getMapper(LocationCategoryMapper.class);
            mapper.updatePriorityById(id, priority);
            session.commit();
        }
    }

    public void deleteDataById(int id){
        try (SqlSession session = factory.openSession()){
            LocationCategoryMapper mapper = session.getMapper(LocationCategoryMapper.class);
            mapper.deleteById(id);
            session.commit();
        }
    }

}
