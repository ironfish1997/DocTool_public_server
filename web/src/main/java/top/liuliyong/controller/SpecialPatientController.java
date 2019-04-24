package top.liuliyong.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import model.Patient;
import model.TreatmentRow;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.liuliyong.response.PublicResponse;
import top.liuliyong.service.SpecialPatientService;

import java.util.List;

/**
 * @Author liyong.liu
 * @Date 2019-04-22
 **/
@RestController
@Api(value = "SpecialPatient", description = "特殊病患服务")
@RequestMapping(path = "/specialPatient")
@Validated
public class SpecialPatientController {
    private final SpecialPatientService specialPatientService;

    public SpecialPatientController(SpecialPatientService specialPatientService) {
        this.specialPatientService = specialPatientService;
    }

    @GetMapping("/getSpecialPatients")
    @ApiOperation(value = "查找辖区内所有特殊病症病人")
    @ApiImplicitParams({@ApiImplicitParam(name = "area", value = "辖区", required = true, dataType = "String"),})
    public PublicResponse getSpecialPatients(@RequestParam("area") String area) {
        List<Patient> result = specialPatientService.getSpecialPatients(area);
        return new PublicResponse(0, "ok", result);
    }

    @PostMapping("/checkedSpecialPatient")
    @ApiOperation(value = "标记患者本月已复查")
    @ApiImplicitParams({@ApiImplicitParam(name = "patient_id", value = "患者id", required = true, dataType = "String"),})
    public PublicResponse checkedSpecialPatient(@RequestParam("patient_id") String patient_id) {
        Patient result = specialPatientService.checkedSpecialPatient(patient_id);
        return new PublicResponse(0, "ok", result);
    }

    @PostMapping("/saveReviewRecord")
    @ApiOperation(value = "保存复查记录")
    @ApiImplicitParams({@ApiImplicitParam(name = "treatment", value = "就诊记录实体", required = true, dataType = "TreatmentRow"),})
    public PublicResponse saveReviewRecord(@RequestBody TreatmentRow treatment) {
        TreatmentRow result = specialPatientService.saveReviewRecord(treatment);
        return new PublicResponse(0, "ok", result);
    }

    @DeleteMapping("/deleteReviewRecord")
    @ApiOperation(value = "删除复查记录")
    @ApiImplicitParams({@ApiImplicitParam(name = "treatment_id", value = "就诊记录id", required = true, dataType = "String"),})
    public PublicResponse deleteReviewRecord(@RequestParam("treatment_id") String treatment_id) {
        TreatmentRow result = specialPatientService.deleteReviewRecord(treatment_id);
        return new PublicResponse(0, "ok", result);
    }
}