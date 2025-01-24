package learnbyteaching.todos.controllers;

import java.util.List;
import java.util.Optional;

import org.apache.catalina.startup.ClassLoaderFactory.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import learnbyteaching.todos.repositories.dao.TodoRepository;
import learnbyteaching.todos.repositories.vo.TodoItem;

@RequestMapping("/api/todos")
@RestController		// 요청 처리를 위한 컨트롤러와, 뭐 어쩌고를 함께 구현하는 ( @Controller + @ResponesBody )
public class TodoApiController {
	// Repositiry 연결
	@Autowired
	private TodoRepository todoRepository;
	
	// 객체를 단순히 리턴만 하면 status 200만 전송
	// 그렇기 때문에
	// 보다 세밀한 응답 정보를 함께 보내고자 한다면 ResponesEntity 를 사용 
	@GetMapping
//	public List<TodoItem> getAllTodos() {
	public ResponseEntity<List<TodoItem>> getAllTodos(){
		List<TodoItem> todos = todoRepository.findAll();
//		return todos;
		return ResponseEntity.ok(todos);
		// status 200에 body에 todos 실어보냄
		
		
	}
	
	// id로 ToDO 항목 조회 -> /api/todos/{id}
	@GetMapping("/{id}")
	public ResponseEntity<TodoItem> getTodoById(@PathVariable("id") Long id) {
		Optional<TodoItem> todo = todoRepository.findById(id);
		return todo.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
		// 이렇게 별로 안쓰는데 실제 쓰이는 코드기도하니까, 보여주려고 이렇게 썼다구 합니다
	}
	
	
	

}


// 우리가 원하는 결과 > 투두아이템의 list를 원해!   -> public List<TodoItem>
