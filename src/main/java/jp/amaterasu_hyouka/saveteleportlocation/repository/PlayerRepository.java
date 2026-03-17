package jp.amaterasu_hyouka.saveteleportlocation.repository;

import jp.amaterasu_hyouka.saveteleportlocation.db.mybatis.DatabaseRegistry;
import jp.amaterasu_hyouka.saveteleportlocation.model.LoginPlayer;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class PlayerRepository {
    private static final PlayerRepository playerRepository = new PlayerRepository();
    private PlayerRepository(){}
    public static PlayerRepository getInstance(){return playerRepository;}

    private final SqlSessionFactory factory = DatabaseRegistry.SAVE_TELEPORT_LOCATION.getSqlSessionFactory();

    public void insertOrReplaceData(LoginPlayer loginPlayer){
        try (SqlSession session = factory.openSession()){
            PlayerMapper mapper = session.getMapper(PlayerMapper.class);
            mapper.insertOrReplace(loginPlayer);
            session.commit();
        }
    }

    public String getNameByUuid(String uuid){
        try (SqlSession session = factory.openSession()){
            PlayerMapper mapper = session.getMapper(PlayerMapper.class);
            return mapper.findNameByUuid(uuid);
        }
    }

    public List<LoginPlayer> getAll(){
        try (SqlSession session = factory.openSession()){
            PlayerMapper mapper = session.getMapper(PlayerMapper.class);
            return mapper.findAll();
        }
    }

}
