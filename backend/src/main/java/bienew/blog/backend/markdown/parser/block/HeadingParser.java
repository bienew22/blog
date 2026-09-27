package bienew.blog.backend.markdown.parser.block;


import bienew.blog.backend.markdown.InlineParser;
import bienew.blog.backend.markdown.ast.block.Heading;
import bienew.blog.backend.markdown.parser.AbstractBlockParser;
import bienew.blog.backend.markdown.parser.ParseResult;

public class HeadingParser extends AbstractBlockParser {

    private Heading heading;


    @Override
    protected boolean canParse(String line) {
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
    protected ParseResult parseNode(String[] lines, int startLineIndex) {

        String line = lines[startLineIndex];
        int level = getLevel(line);

        String text = line.substring(level).trim();

        heading = new Heading(level);

        InlineParser.getInstance().parse(text.substring(level))
                .forEach(block -> heading.addChild(block));

        return new ParseResult(heading, startLineIndex + 1, true);
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
