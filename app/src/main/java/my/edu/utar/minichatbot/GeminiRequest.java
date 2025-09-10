package my.edu.utar.minichatbot;

import java.util.ArrayList;
import java.util.List;

public class GeminiRequest {
    private List<Content> contents;

    public GeminiRequest(String text) {
        this.contents = new ArrayList<>();
        Content content = new Content();
        content.parts = new ArrayList<>();
        content.parts.add(new Part(text));
        this.contents.add(content);
    }

    public static class Content {
        public List<Part> parts;
    }

    public static class Part {
        public String text;

        public Part(String text) {
            this.text = text;
        }
    }
}