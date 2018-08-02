package com.utopia.springboothn.controller;

import com.utopia.springboothn.common.BaseJson;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hn
 * @date 2018/08/02
 */
@RestController
public class TestController extends BaseController{

    @RequestMapping(value = "/test")
    public BaseJson test(){
        return  new BaseJson();
    }



}
