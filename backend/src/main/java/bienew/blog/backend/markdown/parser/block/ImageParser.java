package bienew.blog.backend.markdown.parser.block;

import bienew.blog.backend.markdown.ast.BlockNode;
import bienew.blog.backend.markdown.ast.block.Image;
import bienew.blog.backend.markdown.parser.ParseResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageParser extends AbstractBlockParser {
    @Override
    protected boolean canParse(String line) {
        if  (line == null || line.isEmpty()) {
            return false;
        }

        return line.startsWith("![") && line.contains("](") && line.endsWith(")");
    }

    @Override
    protected ParseResult<? extends BlockNode> parseNode(String[] lines, int startLineIndex) {

        Pattern pattern = Pattern.compile("!\\[(.*?)]\\((.*?)\\)");
        Matcher matcher = pattern.matcher(lines[startLineIndex]);

        if (matcher.find()) {
            String altText = matcher.group(1);
            String url = matcher.group(2);

            if (url.isBlank() || !url.startsWith("/api/v1/images/")) {
                return ParseResult.blockNotParsed();
            }

            return new ParseResult<>(
                    new Image(altText, url)
                    , startLineIndex + 1, true);
        }

        return ParseResult.blockNotParsed();
    }
}
