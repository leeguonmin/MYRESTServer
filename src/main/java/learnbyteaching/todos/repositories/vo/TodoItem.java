package learnbyteaching.todos.repositories.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// 롬복으로 수정해보자 


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class TodoItem {
	private Long id;
	private String title;
	private boolean completed;

	
}
