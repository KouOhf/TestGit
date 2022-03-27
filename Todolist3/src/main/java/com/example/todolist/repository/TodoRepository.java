package com.example.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.todolist.entity.Todo;

@Repository
//JpaRepository<Todo, Integer> ・・・ 第一引数＝entityのクラス/第二引数＝「主キー(@ID)のクラス」
//これにより、CRUDが一通り使える
public interface TodoRepository extends JpaRepository<Todo, Integer>{

}
