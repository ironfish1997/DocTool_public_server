package top.liuliyong.publicservice.repositoryImpl;

import top.liuliyong.publicservice.common.model.Resident;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * @Author liyong.liu
 * @Date 2019-04-22
 **/
@Repository
public class ResidentRepository {
    private final MongoOperations mongoTemplate;

    public ResidentRepository(MongoOperations mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * 查找地区内所有居民信息
     */
    public List<Resident> findByArea(String areaStr) {
        return mongoTemplate.find(query(where("area").is(areaStr)), Resident.class, "patients");
    }
}
