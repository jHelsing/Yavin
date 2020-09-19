package se.jonteh.yavin.model;

public class Snippet {

    private final long id;
    private final String content;

    public Snippet(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public long getId() {
        return id;
    }
}
