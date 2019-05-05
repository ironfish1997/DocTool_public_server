package top.liuliyong.publicservice.service;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import top.liuliyong.publicservice.common.enums.MQQueueEnum;
import top.liuliyong.publicservice.common.model.MQMessageObject;
import top.liuliyong.publicservice.common.model.Patient;
import top.liuliyong.publicservice.repository.repositoryImpl.SpecialPatientRepository;

import java.util.Date;
import java.util.List;

/**
 * 特殊病症模块定时任务
 *
 * @Author liyong.liu
 * @Date 2019-04-24
 **/
@Service
public class ScheduleTaskService {
    private final SpecialPatientRepository specialPatientRepository;
    private static final Logger logger = LoggerFactory.getLogger(ScheduleTaskService.class);
    private final AmqpTemplate amqpTemplate;

    public ScheduleTaskService(SpecialPatientRepository specialPatientRepository, AmqpTemplate amqpTemplate) {
        this.specialPatientRepository = specialPatientRepository;
        this.amqpTemplate = amqpTemplate;
    }

    /**
     * 将mongo内所有特殊病症病人推送到redis里，3月，6月，9月，12月1日执行
     */
    @Scheduled(cron = "0 0 0 1 3,6,9,12 ?")
//    @Scheduled(cron = "10 * * * * ?")
    public void putUnreviewPatientsToRedis() {
        try {
            logger.info(">>>>>>推送特殊病症病人到redis任务启动");
            List<Patient> allSpPatients = specialPatientRepository.findAllSpecialPatients();
            if (allSpPatients == null || allSpPatients.size() == 0) {
                return;
            }
            //1.清空redis里所有未就诊病人
            specialPatientRepository.clearUnreviewPatientsRecordFromRedis();
            //2.把mongo里查到的所有特殊病症病人加入redis缓存
            List<Patient> result = specialPatientRepository.putSpecialPatientToRedis(allSpPatients.toArray(new Patient[0]));
            if (result == null || result.size() != allSpPatients.size()) {
                logger.error(">>>>>>特殊病症病人推送redis失败!");
                return;
            }
            logger.info(">>>>>>特殊病症病人推送redis成功");
        } catch (Exception e) {
            logger.error(">>>>>>特殊病症病人推送redis失败:", e);
        }
    }

    /**
     * 3月，6月，9月，12月20号如果还有未复查的高血压，糖尿病患者，则短信通知医生。
     * 本来应该是用短信通知，但是短信需要营业执照，故先用消息来代替
     */
//    @Scheduled(cron = "1 * * * * ?")
    @Scheduled(cron = "* * * 20 3,6,9,12 ?")
    public void sendUnreviewSpecialPatientsListNotificationToFront() {
        try {
            List<Patient> currentUnreviewPetients = specialPatientRepository.getAllUnreviewPatientsFromRedis();
            if (currentUnreviewPetients.size() == 0) {
                return;
            }
            for (Patient patient : currentUnreviewPetients) {
                //消息队列中消息的id为area，因为是根据area区分发送的对象的
                MQMessageObject msg = new MQMessageObject(new Date().getTime(), "systemPermanentNotification", patient.getArea(), "病人" + patient.getName() + "本月还未复查");
                amqpTemplate.convertAndSend(MQQueueEnum.TEMPMESSAGEQUEUE.getName(), JSON.toJSONString(msg));
            }
        } catch (Exception e) {
            logger.error("特殊病症通知失败:", e);
        }
    }

}
