package com.utopia.springboothn.service.impl;

import com.utopia.springboothn.model.First;
import com.utopia.springboothn.service.IFirstService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
@Component("firstService")
public class FirstServiceImpl implements IFirstService{
    @Autowired
    MongoOperations operations;
    @Override
    public void saveFirst(First first) {
        operations.save(first);
    }

    @Override
    public First findById(String id) {
        return operations.findById(new Query(Criteria.where("id").is(id)),First.class);
    }

    @Override
    public List<First> listAll() {
        return operations.findAll(First.class);
    }
}
