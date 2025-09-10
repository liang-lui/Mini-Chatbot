package my.edu.utar.minichatbot;

import java.util.List;

public class GeminiResponse {
    private List<Candidate> candidates;

    public String getText() {
        if (candidates != null && !candidates.isEmpty()) {
            Candidate candidate = candidates.get(0);
            if (candidate.content != null && candidate.content.parts != null &&
                    !candidate.content.parts.isEmpty()) {
                return candidate.content.parts.get(0).text;
            }
        }
        return "Sorry, I couldn't process that request.";
    }

    public static class Candidate {
        public Content content;
    }

    public static class Content {
        public List<Part> parts;
    }

    public static class Part {
        public String text;
    }
}
