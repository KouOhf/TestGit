package com.example.todolist.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.todolist.entity.Todo;


//JpaRepository<Todo, Integer> ・・・ 第一引数＝entityのクラス/第二引数＝「主キー(@ID)のクラス」
//これにより、CRUDが一通り使える
@Repository
public interface TodoRepository extends JpaRepository<Todo, Integer>{
	List<Todo> findByTitleLike(String title);
	List<Todo> findByImportance(Integer importance);
	List<Todo> findByUrgency(Integer urgency);
	List<Todo> findByDeadlineBetweenOrderByDeadlineAsc(Date from, Date to);
	List<Todo> findByDeadlineGreaterThanEqualOrderByDeadlineAsc(Date from);
	List<Todo> findByDeadlineLessThanEqualOrderByDeadlineAsc(Date to);
	List<Todo> findByDone(String done);
	/*
	 * 【命名規則に準じた抽象メソッドをリポジトリに宣言すると、SpringBootが自動実装する】
	 *findBy～Like = その 文字( 列)を～に含む
	 * findBy～Between～Asc　＝　（この場合）開始～終了の範囲内に期限が収まっている※Asc=昇順
	 *  findBy～GreaterThanEqualOrderBy～Asc＝～以上の。SQL⇒『SELECT * FROM todo WHERE importance = ーー
	 *  ーー＞引数 importanceの値 AND urgency = 引数 urgency の値』
	 * */
}
