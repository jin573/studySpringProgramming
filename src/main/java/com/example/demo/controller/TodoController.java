package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TodoDTO;
import com.example.demo.model.TodoEntity;
import com.example.demo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("todo") //localhost:8080/todo
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping("/test")
    public ResponseEntity<?> testTodo(){
        String str = todoService.testService();
        List<String> list = new ArrayList<>();
        list.add(str);

        ResponseDTO<String> responseEntity = ResponseDTO.<String>builder().data(list).build();
        return ResponseEntity.ok().body(responseEntity);
    }

    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO todoDTO){
        try{
            String temporaryUserId = "temporary - user"; //temporaryUserId 임시 아이디

            //dto -> entity
            TodoEntity todoEntity = TodoDTO.toEntity(todoDTO);
            todoEntity.setId(null);

            todoEntity.setUserId(temporaryUserId);

            //todoEntity 생성
            List<TodoEntity> todoEntities = todoService.create(todoEntity);

            //리턴된 todoEntities 를 todoDTOS 로 변환
            List<TodoDTO> todoDTOS = todoEntities.stream().map(TodoDTO::new).collect(Collectors.toList());

            //변환된 todoDTOS 를 이용해 ResponseDTO 초기화
            ResponseDTO<TodoDTO> responseDTO = ResponseDTO.<TodoDTO>builder().data(todoDTOS).build();

            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e){
            //예외 처리
            String error = e.getMessage();
            ResponseDTO<TodoDTO> responseDTO = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
