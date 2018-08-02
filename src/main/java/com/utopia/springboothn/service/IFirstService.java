package com.utopia.springboothn.service;

import com.utopia.springboothn.model.First;

import java.util.List;

/**
 * @author hn
 * @date 2018/08/02
 */
public interface IFirstService {

    public void saveFirst(First first);

    public First findById(String id);

    public List<First> listAll();


}
