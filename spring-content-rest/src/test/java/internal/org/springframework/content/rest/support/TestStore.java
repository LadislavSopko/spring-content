package internal.org.springframework.content.rest.support;

import org.springframework.content.commons.repository.Store;
import org.springframework.web.bind.annotation.CrossOrigin;

import internal.org.springframework.content.rest.StoreRestResource;

@StoreRestResource(path="teststore")
@CrossOrigin(origins={"http://www.someurl.com"})
public interface TestStore extends Store<String> {
}


