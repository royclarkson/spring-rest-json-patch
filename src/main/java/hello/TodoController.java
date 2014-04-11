/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hello;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.fge.jsonpatch.JsonPatchException;

/**
 * @author Roy Clarkson
 * @author Craig Walls
 * @author Greg L. Turnquist
 */
@RestController
@RequestMapping("/todos")
public class TodoController {

	private TodoRepository repository;
	
	@Autowired
	public TodoController(TodoRepository repository) {
		this.repository = repository;
	}

	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Iterable<Todo>> list() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept-Patch", "application/json-patch+json");
		return new ResponseEntity<Iterable<Todo>>(repository.findAll(), headers, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Todo create(@RequestBody Todo todo) {
		return repository.save(todo);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Transactional
	public void update(@RequestBody Todo updatedTodo, @PathVariable("id") long id) throws IOException, JsonPatchException {
		if (id != updatedTodo.getId()) {
			repository.delete(id);
		}
		repository.save(updatedTodo);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") long id) {
		repository.delete(id);
	}

}
