package top.liuliyong.service;

import enums.StatusEnum;
import exception.SpecialPatientOperationException;
import model.Patient;
import model.TreatmentRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.liuliyong.repository.SpecialPatientRepository;

import java.util.List;

/**
 * @Author liyong.liu
 * @Date 2019-04-22
 **/
@Service
public class SpecialPatientService {
    @Autowired
    private SpecialPatientRepository specialPatientRepository;

    public List<Patient> getSpecialPatients(String area) {
        if (area == null || area.trim().length() == 0) {
            throw new SpecialPatientOperationException(StatusEnum.LACK_OF_INFORMATION);
        }
        List<Patient> specialPatients = specialPatientRepository.findSpecialPatients(area);
        return specialPatients;
    }

    public Patient checkedSpecialPatient(String patient_id) {
        if (patient_id == null || patient_id.trim().length() == 0) {
            throw new SpecialPatientOperationException(StatusEnum.LACK_OF_INFORMATION);
        }
        //1.从redis里获取全部未复查的对象
        List<Patient> patientsInRedis = specialPatientRepository.getAllUnreviewPatientsFromRedis();
        Patient targetPatient = null;
        //2.循环找到patient_id对应的对象，从redis里删除
        for (Patient patient : patientsInRedis) {
            if (patient.getId_number().equals(patient_id)) {
                targetPatient = specialPatientRepository.deleteSpecialPatientFromRedis(patient);
            }
        }
        if (targetPatient == null) {
            throw new SpecialPatientOperationException(StatusEnum.NOT_FOUND);
        }
        //3.返回从redis删除的病人对象
        return targetPatient;
    }

    /**
     * 把当前数据库里的所有特殊病患放进redis里
     */
    public List<Patient> addSpecialPatientsToRedis(String area) {
        //拿到当前mongo里存储的所有特殊病患
        List<Patient> specialPatients = getSpecialPatients(area);
        return specialPatientRepository.putSpecialPatientToRedis((Patient[]) specialPatients.toArray());
    }

    /**
     * 保存复查记录进mongo
     */
    public TreatmentRow saveReviewRecord(TreatmentRow treatment) {
        if (treatment == null) {
            throw new SpecialPatientOperationException(StatusEnum.LACK_OF_INFORMATION);
        }
        return specialPatientRepository.saveSpecialTreatRow(treatment);
    }

    /**
     * 删除特殊病症复查记录
     *
     * @param treatment_id 复查记录的id
     */
    public TreatmentRow deleteReviewRecord(String treatment_id) {
        if (treatment_id == null || treatment_id.trim().length() == 0) {
            throw new SpecialPatientOperationException(StatusEnum.LACK_OF_INFORMATION);
        }
        return specialPatientRepository.deleteSpecialTreatRow(treatment_id);
    }
}
