package com.utopia.springboothn.controller;

import com.fasterxml.jackson.databind.node.BaseJsonNode;
import com.utopia.springboothn.common.BaseJson;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController extends BaseController{

    @RequestMapping(value = "/login")
    public void login(){
        BaseJson baseJson = new BaseJson();

    }

}
