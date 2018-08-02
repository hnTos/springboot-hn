package com.utopia.springboothn.controller;


import com.mongodb.MongoException;
import com.utopia.springboothn.common.BaseJson;
import com.utopia.springboothn.common.BaseJsonData;
import com.utopia.springboothn.model.First;
import com.utopia.springboothn.service.IFirstService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/mongodb")
public class MongoController {
    @Autowired
    IFirstService firstService;

    @RequestMapping(value = "/first")
    public BaseJson save(First first){
        BaseJson json = new BaseJson();
        try {
            firstService.saveFirst(first);
        } catch (MongoException e){
            json.setErrorMessage(e.getMessage());
        }
        return json;
    }
    @RequestMapping(value = "/list")
    public BaseJsonData list(First first){
        BaseJsonData json = new BaseJsonData();
        try {
           List<First> results = firstService.listAll();
           json.setData(results);
        } catch (MongoException e){
            json.setErrorMessage(e.getMessage());
        }
        return json;
    }
}
