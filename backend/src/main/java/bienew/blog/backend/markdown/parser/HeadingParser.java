package bienew.blog.backend.markdown.parser;


import bienew.blog.backend.markdown.ast.Heading;

public class HeadingParser implements AbstractBlockParser {

    private Heading heading;


    @Override
    public boolean canParse(String line) {
        if (line == null || line.isEmpty()) {
            return false;
        }
        if (!line.startsWith("#")) {
            return false;
        }

        int count = getLevel(line);

        return count > 0 && count <= 6;
    }

    @Override
    public ParseResult parse(String[] lines, int startLineIndex) {

        String line = lines[startLineIndex];
        int level = getLevel(line);
        String text = line.substring(level).trim();

        heading = new Heading(level, text);
        return new ParseResult(heading, startLineIndex + 1);
    }

    private int getLevel(String line) {
        int count = 0;
        for (char c : line.toCharArray()) {
            if (c == '#') {
                count++;
            } else {
                break;
            }
        }
        return count;
    }
}
