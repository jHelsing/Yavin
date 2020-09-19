package se.jonteh.yavin;

import org.springframework.web.bind.annotation.*;
import se.jonteh.yavin.model.Snippet;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class SnippetController {

    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/snippets")
    public List<Snippet> listSnippets() throws Exception {
        throw new Exception();
    }

    @PutMapping("/snippets")
    public Snippet createNewSnippet() throws Exception {
        throw new Exception();
    }

    @PostMapping("/snippets")
    public Snippet updateSnippet(@RequestBody Snippet incoming) {
        return new Snippet(counter.getAndIncrement(), "hej");
    }

    @DeleteMapping("/snippets/")
    public void deleteSnippet() throws Exception {
        throw new Exception();
    }
}
