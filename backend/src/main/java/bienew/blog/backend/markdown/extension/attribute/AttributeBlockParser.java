package bienew.blog.backend.markdown.extension.attribute;

import org.commonmark.node.Block;
import org.commonmark.parser.block.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AttributeBlockParser extends AbstractBlockParser {

    private static final Pattern ATTRIBUTE_PATTERN =
            Pattern.compile("^\\{\\s*((?:\\.[a-zA-Z0-9_-]+\\s*)+)}$");


    private final AttributeBlock block  = new AttributeBlock();

    // factory method를 사용하여 객체를 생성하도록 변경
    private AttributeBlockParser(String line) {
        parseAttributes(line);
    }

    @Override
    public Block getBlock() {
        return block;
    }

    @Override
    public BlockContinue tryContinue(ParserState parserState) {
        // 한줄로 작성될 예정이므로 다음 줄로 이어지지 않도록 종료를 반환합니다.
        return BlockContinue.finished();
    }

    private void parseAttributes(String line) {
        Matcher matcher = ATTRIBUTE_PATTERN.matcher(line);

        if (!matcher.matches()) {
            return;
        }

        String attributeString = matcher.group(1);
        // { .class1 .class2 } 형태의 문자열에서 클래스 이름을 추출하여 AttributeBlock에 추가합니다.
        for (String token : attributeString.split("\\s+")) {
            if (token.startsWith(".")) {
                String className = token.substring(1);
                block.addClass(className);
            }
        }
    }

    public static class Factory extends AbstractBlockParserFactory {
        @Override
        public BlockStart tryStart(ParserState state, MatchedBlockParser matchedBlockParser) {
            String line = state.getLine()
                    .getContent()
                    .toString()
                    .trim();

            // 해당 줄이 AttributeBlock의 패턴과 일치하지 않으면
            // BlockStart.none()을 반환하여 파서를 시작하지 않습니다.
            if (!ATTRIBUTE_PATTERN.matcher(line).matches()) {
                return BlockStart.none();
            }

            BlockParser parser = new AttributeBlockParser(line);

            return BlockStart.of(parser)
                    .atIndex(state.getNextNonSpaceIndex());
        }
    }
}
