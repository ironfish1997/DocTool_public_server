package top.liuliyong.publicservice.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.liuliyong.publicservice.common.model.Resident;
import top.liuliyong.publicservice.service.ResidentService;
import top.liuliyong.publicservice.web.response.PublicResponse;

import java.util.List;

/**
 * @Author liyong.liu
 * @Date 2019-04-21
 **/
@RestController
@RequestMapping(path = "/resident")
@Validated
@Api(value = "Resident", description = "辖区居民服务")
public class ResidentController {
    private static final Logger logger = LoggerFactory.getLogger(ResidentController.class);
    @Autowired
    private ResidentService residentService;


    @GetMapping("/findAllResidents")
    @ApiOperation(value = "查找辖区内所有居民信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "area", value = "辖区", required = true, dataType = "String"),})
    public PublicResponse findAllResidents(@RequestParam(name = "area") String area) {
        List<Resident> queryResult = residentService.findAllInArea(area);
        return new PublicResponse(0, "ok", queryResult);
    }

    @PostMapping("/sendNotification")
    @ApiOperation(value = "向所有辖区内居民发送公共卫生服务通知")
    @ApiImplicitParams({@ApiImplicitParam(name = "area", value = "辖区", required = true, dataType = "String"), @ApiImplicitParam(name = "msg", value = "信息内容", required = true, dataType = "String")})
    public PublicResponse sendNotification(@RequestParam(name = "msg") String msg, @RequestParam(name = "area") String area) {
        return new PublicResponse(0, "test", null);
    }
}
