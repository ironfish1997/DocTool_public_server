package top.liuliyong.publicservice.repository.repositoryImpl;

import top.liuliyong.publicservice.common.model.Patient;
import top.liuliyong.publicservice.common.model.TreatmentRow;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;
import top.liuliyong.publicservice.repository.util.JedisUtil;
import top.liuliyong.publicservice.repository.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * @Author liyong.liu
 * @Date 2019-04-22
 **/
@Repository
public class SpecialPatientRepository {
    private MongoOperations mongoTemplate;
    private static final String UNREVIEW_PATIENTS = "unreview_patients";
    private static final String REVIEW_RECORD = "review_record";
    private static final String PATIENTS = "patients";

    public SpecialPatientRepository(MongoOperations mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * 从mongo查找指定地区内的所有特殊病症病人
     *
     * @param area area of the target patients
     */
    public List<Patient> findSpecialPatients(String area) {
        return mongoTemplate.find(query(where("area").is(area).and("special_disease").exists(true)), Patient.class, PATIENTS);
    }

    /**
     * 查找所有特殊病症病人
     */
    public List<Patient> findAllSpecialPatients() {
        return mongoTemplate.find(query(where("special_disease").exists(true)), Patient.class, PATIENTS);
    }

    /**
     * 把特殊病患放进redis里
     */
    public Patient putSpecialPatientToRedis(Patient patient) {
        long result;
        long oriLength = JedisUtil.llen(UNREVIEW_PATIENTS);
        if (oriLength == 0) {
            long lastDayOfMonth = TimeUtil.getLastDayOfCurrentMonth();
            result = JedisUtil.lpush(UNREVIEW_PATIENTS, patient, lastDayOfMonth);
        } else {
            result = JedisUtil.lpush(UNREVIEW_PATIENTS, patient, 0);
        }
        if (result != oriLength + 1) {
            return null;
        } else {
            return patient;
        }
    }

    /**
     * 批量把特殊病患放进redis里面
     */
    public List<Patient> putSpecialPatientToRedis(Patient... patients) {
        List<Patient> result = new ArrayList<>();
        for (Patient patient : patients) {
            result.add(putSpecialPatientToRedis(patient));
        }
        return result;
    }

    /**
     * 删除需复查的病患
     */
    public Patient deleteSpecialPatientFromRedis(Patient patient) {
        //1.从redis里面拿到当前还没有复查的病患
        List<Object> unreviewPatients = JedisUtil.lgetAll(UNREVIEW_PATIENTS);
        //2.从病患中找到该病患并删除
        for (Object unreviewPatientObj : unreviewPatients) {
            Patient unreviewPatient = (Patient) unreviewPatientObj;
            if (unreviewPatient.equals(patient)) {
                long result = JedisUtil.lrem(UNREVIEW_PATIENTS, unreviewPatient);
                if (result > 0) {
                    return patient;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * 批量删除需复查的病患
     */
    public List<Patient> DeleteSpecialPatientFromRedis(Patient... patients) {
        List<Patient> result = new ArrayList<>();
        for (Patient patient : patients) {
            result.add(deleteSpecialPatientFromRedis(patient));
        }
        return result;
    }

    /**
     * 从redis读取当前所有未复查的特殊病患
     */
    public List<Patient> getAllUnreviewPatientsFromRedis() {
        List<Object> resultObjArr = JedisUtil.lgetAll(UNREVIEW_PATIENTS);
        List<Patient> result = new ArrayList<>();
        for (Object resultObj : resultObjArr) {
            if (resultObj instanceof Patient) {
                result.add((Patient) resultObj);
            }
        }
        return result;
    }


    /**
     * 清空需复查病人列表
     */
    public long clearUnreviewPatientsRecordFromRedis() {
        return JedisUtil.delSerialized(UNREVIEW_PATIENTS);
    }

    /**
     * 查找所有特殊病症就诊记录
     */
    public List<TreatmentRow> findAllSpecialTreatRow() {
        return mongoTemplate.findAll(TreatmentRow.class, REVIEW_RECORD);
    }

    /**
     * 通过patient_id_number查找特殊病症复查记录
     */
    public List<TreatmentRow> findSpecialTreatRowByPatientIdNumber(String idNumber) {
        return mongoTemplate.find(query(where("patient_id_number").is(idNumber)), TreatmentRow.class, REVIEW_RECORD);
    }

    /**
     * 通过就诊记录id删除特殊病症复查记录
     */
    public List<TreatmentRow> deleteSpecialTreatRow(String... ids) {
        return mongoTemplate.findAllAndRemove(query(where("id").in((Object[]) ids)), TreatmentRow.class, REVIEW_RECORD);
    }

    /**
     * 通过就诊记录id删除特殊病症复查记录
     */
    public TreatmentRow deleteSpecialTreatRow(String id) {
        return mongoTemplate.findAndRemove(query(where("id").is(new ObjectId(id))), TreatmentRow.class, REVIEW_RECORD);
    }

    /**
     * 保存特殊病症就诊记录
     */
    public TreatmentRow saveSpecialTreatRow(TreatmentRow treatmentRow) {
        return mongoTemplate.save(treatmentRow, REVIEW_RECORD);
    }

}
