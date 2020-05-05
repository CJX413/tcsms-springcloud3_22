package com.tcsms.business.Controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tcsms.business.Entity.BuildingRegistry;
import com.tcsms.business.JSON.ResultJSON;
import com.tcsms.business.Service.ReceiveServiceImp.BuildingRegistryServiceImp;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class BuildingController {
    @Autowired
    private BuildingRegistryServiceImp buildingRegistryServiceImp;

    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    @RequestMapping(value = "/addBuilding", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String addBuilding(@RequestBody String json) {
        try {
            BuildingRegistry buildingRegistry = new Gson().fromJson(json, BuildingRegistry.class);
            buildingRegistryServiceImp.save(buildingRegistry);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJSON(200, false, e.getMessage(), null).toString();
        }
        return new ResultJSON(200, true, "添加障碍建筑成功！", null).toString();
    }

    @RequestMapping(value = "/allBuilding", method = RequestMethod.POST)
    public String getAllBuilding() {
        JsonArray data;
        try {
            data = buildingRegistryServiceImp.formAllBuildingToJsonArray();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJSON(200, false, e.getMessage(), null).getMassege();
        }
        return new ResultJSON(200, true, "获取所有障碍建筑信息成功！", data).toString();
    }

    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    @RequestMapping(value = "/deleteBuilding", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String deleteBuilding(@RequestBody String json) {
        try {
            String buildingId = new Gson().fromJson(json, JsonObject.class).get("buildingId").getAsString();
            buildingRegistryServiceImp.deleteById(buildingId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJSON(200, false, e.getMessage(), null).toString();
        }
        return new ResultJSON(200, true, "删除障碍建筑成功！", null).toString();
    }

}
