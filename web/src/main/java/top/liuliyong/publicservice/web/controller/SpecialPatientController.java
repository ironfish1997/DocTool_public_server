package top.liuliyong.publicservice.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.liuliyong.publicservice.common.model.Patient;
import top.liuliyong.publicservice.common.model.TreatmentRow;
import top.liuliyong.publicservice.service.SpecialPatientService;
import top.liuliyong.publicservice.web.response.PublicResponse;

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
    @ApiImplicitParams({@ApiImplicitParam(name = "patient_id_number", value = "患者身份证号", required = true, dataType = "String"),})
    public PublicResponse checkedSpecialPatient(@RequestParam("patient_id_number") String patient_id_number) {
        Patient result = specialPatientService.checkedSpecialPatient(patient_id_number);
        return new PublicResponse(0, "ok", result);
    }

    @PostMapping("/saveReviewRecord")
    @ApiOperation(value = "保存复查记录")
    @ApiImplicitParams({@ApiImplicitParam(name = "TreatmentRow", value = "就诊记录实体", required = true, dataType = "TreatmentRow"),})
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

    @GetMapping("/getUnreviewSpecialPatients")
    @ApiOperation(value = "获取还未复查的患者清单")
    @ApiImplicitParams({@ApiImplicitParam(name = "area", value = "地区", required = true, dataType = "String"),})
    public PublicResponse getUnreviewSpecialPatients(@RequestParam("area") String area) {
        List<Patient> result = specialPatientService.getUnreviewSpPatients(area);
        return new PublicResponse(0, "ok", result);
    }

    @PostMapping("/addUnreviewSpecialPatient")
    @ApiOperation(value = "新增一个需要复查的病人到本月的需复查清单")
    @ApiImplicitParams({@ApiImplicitParam(name = "patient", value = "病人信息", required = true, dataType = "Patient")})
    public PublicResponse addUnreviewSpecialPatient(@RequestBody Patient patient) {
        Patient result = specialPatientService.addSpecialPatientsToRedis(patient);
        return new PublicResponse(0, "ok", result);
    }

    @GetMapping("/getReviewRecord")
    @ApiOperation(value = "获取患者的所有复查记录")
    @ApiImplicitParams({@ApiImplicitParam(name = "id_number", value = "病人身份证号", required = true, dataType = "string")})
    public PublicResponse getReviewRecord(@RequestParam("id_number") String idNumber) {
        List<TreatmentRow> result = specialPatientService.getReviewRecord(idNumber);
        return new PublicResponse(0, "ok", result);
    }
}
