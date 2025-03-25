package com.example.demo.service;

import com.example.demo.model.TodoEntity;
import com.example.demo.persistence.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TodoService {

    @Autowired
    private TodoRepository repository;

    public String testService(){
        //TodoEntity 생성
        TodoEntity todoEntity = TodoEntity.builder().title("My first todo item").build();
        //TodoEntity 저장
        repository.save(todoEntity);
        //TodoEntity 검색
        TodoEntity savedEntity = repository.findById(todoEntity.getId()).get();
        return savedEntity.getTitle();
    }
}
