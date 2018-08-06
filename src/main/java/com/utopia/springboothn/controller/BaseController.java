package com.utopia.springboothn.controller;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseController {

    @Resource
    HttpServletRequest request;

    @Resource
    HttpServletResponse response;

}
