package com.example.todolist.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.todolist.dao.TodoDaoImpl;
import com.example.todolist.entity.Todo;
import com.example.todolist.form.TodoData;
import com.example.todolist.form.TodoQuery;
import com.example.todolist.repository.TodoRepository;
import com.example.todolist.service.TodoService;

import lombok.RequiredArgsConstructor;

@Controller
//@AllArgsConstructor
@RequiredArgsConstructor
public class TodoListController {
	private final TodoRepository todoRepository;
	private final TodoService todoService;
	private final HttpSession session;
	@PersistenceContext
	private EntityManager entityManager;
	TodoDaoImpl todoDaoImpl;
	
	//インスタンスが作成された後で実際の処理が行われる前に呼び出されるのが「init」メソッド
	@PostConstruct
	public void init() {
		todoDaoImpl = new TodoDaoImpl(entityManager);
	}	
	
	//ToDo一覧表示
	@GetMapping("/todo")
	public ModelAndView showTodoList(ModelAndView mv) {
		mv.setViewName("todoList");
		List<Todo> todoList = todoRepository.findAll();
		mv.addObject("todoList",todoList);
		mv.addObject("todoQuery", new TodoQuery());
		return mv;
	}
	
	//Todo入力フォーム表示
	//todoList.htmlで「新規追加」がクリックされたとき
	@GetMapping("/todo/create")
	public ModelAndView createTodo(ModelAndView mv) {
		mv.setViewName("todoForm");
		mv.addObject("todoData", new TodoData());
		session.setAttribute("mode", "create");
		return mv;
	}
	
	//登録ボタンがクリックされたとき
	@PostMapping("/todo/create")
	public String createTodo(@ModelAttribute @Validated TodoData todoData, BindingResult result, Model model) {
		//エラーチェック
		boolean isValid = todoService.isValid(todoData, result);
		if(!result.hasErrors() && isValid) {
			//エラーなし
			Todo todo = todoData.toEntity();
			todoRepository.saveAndFlush(todo);
			return "redirect:/todo";
		} else {
			//エラーあり
			//mv.setViewName("todoForm");
			return "todoForm";
		}
	}
	
	//Todo一覧へ戻る
	//【処理３】Todo入力画面で「キャンセル登録」ボタンがクリックされたとき
	@PostMapping("/todo/cancel")
	public String canccel() {
		return "redirect:/todo";
	}
	
	@GetMapping("/todo/{id}")
	public ModelAndView todoById(@PathVariable(name="id") int id, ModelAndView mv) {
		mv.setViewName("todoForm");
		Todo todo = todoRepository.findById(id).get();
		mv.addObject("todoData", todo);
		session.setAttribute("mode", "update");
		return mv;
	}
	
	//「更新」ボタンが押されたときの処理
	@PostMapping("/todo/update")
	public String updateTodo(@ModelAttribute @Validated TodoData todoData, BindingResult result, Model model) {
		//エラーチェック
		boolean isValid = todoService.isValid(todoData, result);
		if(!result.hasErrors() && isValid) {
			//エラーなし
			Todo todo = todoData.toEntity();
			todoRepository.saveAndFlush(todo);
			return "redirect:/todo";
		} else {
			//エラーあり
			return "todoForm";
		}
	}
	
	//「削除」ボタンが押された時の処理
	@PostMapping("todo/delete")
	public String deleteTodo(@ModelAttribute TodoData todoData) {
		todoRepository.deleteById(todoData.getId());
		return "redirect:/todo";
	}
	
	//「検索」ボタンが押された時の処理
	@PostMapping("/todo/query")
	public ModelAndView queryTodo(@ModelAttribute TodoQuery todoQuery, BindingResult result, ModelAndView mv) {
		mv.setViewName("todoList");
		List<Todo> todoList = null;
		if(todoService.isValid(todoQuery, result)){
			//エラーがなければ検索
			//todoList = todoService.doQuery(todoQuery);
			//↑list4時の記述　↓ここでJPQLによる検索に書き換え
			//todoList = todoDaoImpl.findByJPQL(todoQuery);
			todoList = todoDaoImpl.findByCriteria(todoQuery);
		}
		//mv.addObject("todoQuery", todoQuery)
		mv.addObject("todoList", todoList);
		
		return mv;
	}
	
	

}