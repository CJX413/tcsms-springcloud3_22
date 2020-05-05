package com.tcsms.business.Service.ReceiveServiceImp;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tcsms.business.Dao.BuildingRegistryDao;
import com.tcsms.business.Entity.BuildingRegistry;
import com.tcsms.business.Exception.CustomizeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuildingRegistryServiceImp {
    @Autowired
    private BuildingRegistryDao buildingRegistryDao;

    public BuildingRegistryDao getDao() {
        return buildingRegistryDao;
    }

    public void save(BuildingRegistry buildingRegistry) throws Exception {
        if (buildingRegistryDao.existsById(buildingRegistry.getBuildingId())) {
            throw new CustomizeException("该建筑ID已存在！");
        }
        buildingRegistryDao.save(buildingRegistry);
    }

    public void deleteById(String buildingId) {
        buildingRegistryDao.deleteById(buildingId);
    }

    public JsonArray formAllBuildingToJsonArray() {
        JsonArray jsonArray = new JsonArray();
        List<BuildingRegistry> list = buildingRegistryDao.findAll();
        for (BuildingRegistry building : list) {
            jsonArray.add(toJsonObject(building));
        }
        return jsonArray;
    }

    private JsonObject toJsonObject(BuildingRegistry buildingRegistry) {
        JsonObject jsonObject = new JsonObject();
        JsonObject pointOne = new JsonObject();
        JsonObject pointTwo = new JsonObject();
        JsonObject pointThree = new JsonObject();
        JsonObject pointFour = new JsonObject();
        pointOne.addProperty("lat", buildingRegistry.getPointOneLat());
        pointOne.addProperty("lng", buildingRegistry.getPointOneLng());

        pointTwo.addProperty("lat", buildingRegistry.getPointTwoLat());
        pointTwo.addProperty("lng", buildingRegistry.getPointTwoLng());

        pointThree.addProperty("lat", buildingRegistry.getPointThreeLat());
        pointThree.addProperty("lng", buildingRegistry.getPointThreeLng());

        pointFour.addProperty("lat", buildingRegistry.getPointFourLat());
        pointFour.addProperty("lng", buildingRegistry.getPointFourLng());
        jsonObject.add("pointOne", pointOne);
        jsonObject.add("pointTwo", pointTwo);
        jsonObject.add("pointThree", pointThree);
        jsonObject.add("pointFour", pointFour);
        jsonObject.addProperty("height", buildingRegistry.getHeight());
        jsonObject.addProperty("buildingId", buildingRegistry.getBuildingId());
        return jsonObject;
    }

}
