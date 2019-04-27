package top.liuliyong.service;

import enums.StatusEnum;
import exception.ResidentOperationException;
import model.Resident;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.liuliyong.repository.ResidentRepository;

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
