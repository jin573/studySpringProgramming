package com.example.demo.service;

import com.example.demo.model.TodoEntity;
import com.example.demo.persistence.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public List<TodoEntity> create(final TodoEntity todoEntity){
        //validations
        validate(todoEntity);

        repository.save(todoEntity);

        log.info("Entity Id: {} is saved", todoEntity.getId());

        return repository.findByUserId(todoEntity.getUserId());
    }

    private void validate(TodoEntity todoEntity){
        if (todoEntity == null){
            log.warn("Entity cannot be null");
            throw new RuntimeException();
        }

        if (todoEntity.getUserId() == null){
            log.warn("Unknown user.");
            throw new RuntimeException("Unknown user.");
        }
    }

    public List<TodoEntity> retrieve(final String userId){
        return repository.findByUserId(userId);
    }

    public List<TodoEntity> update(final TodoEntity todoEntity){
        //저장할 entity가 유효한지 구현
        validate(todoEntity);

        //해당하는 entity를 가져온다
        final Optional<TodoEntity> optionalTodoEntity = repository.findById(todoEntity.getId());

        optionalTodoEntity.ifPresent(todo ->{
            //entity를 수정
            todo.setTitle(todoEntity.getTitle());
            todo.setDone(todoEntity.isDone());
            //저장
            repository.save(todo);
        });

        //유저의 모든 todo list를 리턴
        return  retrieve(todoEntity.getUserId());
    }
}
