package com.example.demo.persistence;

import com.example.demo.model.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, String>{
    //save, findById, delete 는 자동으로 구현된다

    //새로운 메소드 추가할 수 있음. findByUserId 등
    //@Query(value = "select * from Todo t where t.userid = ?1")
    List<TodoEntity> findByUserId(String userId);
    List<TodoEntity> findByUserIdAndTitle(String userId, String title);
}
