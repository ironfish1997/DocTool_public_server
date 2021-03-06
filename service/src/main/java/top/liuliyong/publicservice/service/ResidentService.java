package top.liuliyong.publicservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.liuliyong.publicservice.common.enums.StatusEnum;
import top.liuliyong.publicservice.common.exception.ResidentOperationException;
import top.liuliyong.publicservice.common.model.Resident;
import top.liuliyong.publicservice.repository.repositoryImpl.ResidentRepository;

import java.util.List;

/**
 * @Author liyong.liu
 * @Date 2019-04-22
 **/
@Service
public class ResidentService {

    @Autowired
    private ResidentRepository residentRepository;

    public List<Resident> findAllInArea(String areaStr) {
        if (areaStr == null && areaStr.trim().length() == 0) {
            throw new ResidentOperationException(StatusEnum.LACK_OF_INFORMATION);
        }
        List<Resident> result = residentRepository.findByArea(areaStr);
        return result;
    }
}
