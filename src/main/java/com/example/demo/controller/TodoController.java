package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TodoDTO;
import com.example.demo.model.TodoEntity;
import com.example.demo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<?> createTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO todoDTO){
        try{
            //String temporaryUserId = "temporary - user"; //temporaryUserId 임시 아이디

            //dto -> entity
            TodoEntity todoEntity = TodoDTO.toEntity(todoDTO);
            todoEntity.setId(null);

            todoEntity.setUserId(userId); //매개변수 userId로 변경

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


    @GetMapping
    public ResponseEntity<?> retrieveTodoList(@AuthenticationPrincipal String userId){
        //String temporaryUserId = "temporary - user"; //temporaryUserId 임시 아이디

        List<TodoEntity> todoEntities = todoService.retrieve(userId); //id로 검색하여 리스트 가져오기
        List<TodoDTO> todoDTOS = todoEntities.stream().map(TodoDTO::new).collect(Collectors.toList()); //entity를 dto로
        ResponseDTO<TodoDTO> responseDTO = ResponseDTO.<TodoDTO>builder().data(todoDTOS).build(); //dto를 responseDTO로
        return  ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping
    public ResponseEntity<?> updateTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO todoDTO){
        //String temporaryUserId = "temporary - user"; //temporaryUserId 임시 아이디

        TodoEntity todoEntity = TodoDTO.toEntity(todoDTO);

        //id를 임시 아이디로 초기화 한다 (?) -> 나중에 수정. 현재는 임시 아이디 가져와야 하므로 setUserId 작성
        todoEntity.setUserId(userId);
        //리스트 업데이트
        List<TodoEntity> todoEntities = todoService.update(todoEntity);
        //entity -> dto
        List<TodoDTO> todoDTOS = todoEntities.stream().map(TodoDTO::new).collect(Collectors.toList());
        //dto -> responseDTO
        ResponseDTO<TodoDTO> responseDTO = ResponseDTO.<TodoDTO>builder().data(todoDTOS).build();
        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO todoDTO){
        try{
            //String temporaryUserId = "temporary - user"; //temporaryUserId 임시 아이디

            TodoEntity todoEntity = TodoDTO.toEntity(todoDTO);
            todoEntity.setUserId(userId);

            List<TodoEntity> todoEntities = todoService.delete(todoEntity);

            List<TodoDTO>todoDTOS = todoEntities.stream().map(TodoDTO::new).collect(Collectors.toList());

            ResponseDTO<TodoDTO> responseDTO = ResponseDTO.<TodoDTO>builder().data(todoDTOS).build();
            return ResponseEntity.ok().body(responseDTO);
        }catch (Exception e){
            String error = e.getMessage();
            ResponseDTO<TodoDTO> responseDTO = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
