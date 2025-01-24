package learnbyteaching.todos.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import learnbyteaching.todos.repositories.dao.TodoRepository;
import learnbyteaching.todos.repositories.vo.TodoItem;
// CORS 설정 : 원칙적으로는 허용할 도메인을 정확하게 명시해야 한다. 
@CrossOrigin(origins="*",			// 모든 호스트로부터의 오리진 허가 
	methods= {RequestMethod.GET, RequestMethod.POST,
			RequestMethod.PUT, RequestMethod.DELETE }
		)
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
	
	
	// keyword 파라미터 전달 받아서 title을 대상으로 검색 -> 목록 가져오기
	@GetMapping("/search")
	public ResponseEntity<List<TodoItem>> searchTodos(@RequestParam("keyword") String keyword) {
		List<TodoItem> foundTodos = todoRepository.findAll().stream().filter(todo -> todo.getTitle()
																						.toLowerCase()
																						.contains(keyword.toLowerCase()))
				.collect(Collectors.toList());
		return ResponseEntity.ok(foundTodos);
	}
	
	
	// 새로운 TodoItem 항목 생성
	@PostMapping
	public ResponseEntity<TodoItem> createTodo(
			@RequestBody TodoItem todotem
			) {
		TodoItem savedTodo = todoRepository.save(todotem);
//		return ResponseEntity.created("Location:/api/todos/" + savedTodo.getId());
		
//		return ResponseEntity.status(HttpStatus.CREATED)
//				.header("Location", "/api/todos/" + savedTodo.getId())
//				.body(savedTodo);
		//	TODO: 나중에 수정
		
		URI location = URI.create("/api/todos/" + savedTodo.getId());
		return ResponseEntity.created(location).body(savedTodo);
		
	}
	
	
	// 기존 TodoItem 수정
	@PutMapping("/{id}")			// /api/todos/{id}
	public ResponseEntity<TodoItem> updateTodo(@PathVariable Long id, @RequestBody TodoItem updatedTodo) {
		return todoRepository.findById(id)
			.map(todo -> {
				todo.setTitle(updatedTodo.getTitle());
				todo.setCompleted(updatedTodo.isCompleted());
				TodoItem savedTodo = todoRepository.save(todo);
				return ResponseEntity.ok(savedTodo);
		}).orElseGet(()-> ResponseEntity.notFound().build());
	}
	
	// 기존 TodoItem 항목 삭제
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
		Optional<TodoItem> existingTodo = todoRepository.findById(id);
		
		if (!existingTodo.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		todoRepository.deleteById(id);
		return ResponseEntity.ok().<Void>build();
	}
	

}


// 우리가 원하는 결과 > 투두아이템의 list를 원해!   -> public List<TodoItem>
