package bienew.blog.backend.markdown;

import bienew.blog.backend.markdown.ast.ASTNode;
import org.junit.jupiter.api.Test;

class MarkDownParserTest {


    private final BlockParser blockParser = BlockParser.INSTANCE;

    @Test
    void block_parser_test() {
        ASTNode astNode = blockParser.parse("""
                [TEXT](https://www.naver.com)
                [_TEXT_](https://www.naver.com)
                [__TEXT__](https://www.naver.com)
                [**TEXT**](https://www.naver.com)
                """);

        System.out.println("=============after parse=============");
        while (astNode != null) {
            System.out.println(astNode);
            astNode = astNode.getNext();
        }
        System.out.println("=====================================");
    }

    @Test
    void fenced_code_block_parser_test() {
        ASTNode astNode = blockParser.parse("""
                ```java
                public class HelloWorld {
                    public static void main(String[] args) {
                        System.out.println("Hello, World!");
                    }
                }
                ```
                
                ```java
                ```java
                """);

        System.out.println("=============after parse=============");
        while (astNode != null) {
            System.out.println(astNode);
            astNode = astNode.getNext();
        }
        System.out.println("=====================================");
    }

    @Test
    void inline_code_block_parser_test() {
        ASTNode astNode = blockParser.parse("""
                `public class HelloWorld {`
                `public **static** void main(String[] args) {`
                `System.out.println("Hello, World!");`
                `}`
                **`}`**
                """);

        System.out.println("=============after parse=============");
        while (astNode != null) {
            System.out.println(astNode);
            astNode = astNode.getNext();
        }
        System.out.println("=====================================");
    }

    @Test
    void list_parser_test() {
        ASTNode astNode = blockParser.parse("""
                - Item 1
                \t1. Subitem 1.1
                \t2. Subitem 1.2
                \t\t1. Subsubitem 1.2.1 **bold**
                \t\t2. Subsubitem 1.2.2
                - Item 2
                \t- Subitem 2.1
                \t- Subitem 2.2
                - Item 3
                """);
        System.out.println("=============after parse=============");
        while (astNode != null) {
            System.out.println(astNode);
            astNode = astNode.getNext();
        }
        System.out.println("=====================================");
    }

    @Test
    void ast_to_html_test() {
        String result = MarkDownParser.toHtml("""
                # Markdown 파서 테스트
                
                Markdown 파서의 기본 문법과 중첩 구조를 확인하기 위한 테스트 문서입니다.
                
                ## 1. 텍스트 서식
                
                일반 텍스트, **굵은 글씨**, *기울임 글씨*, ~~취소선~~을 확인합니다.
                
                문장 안에서 **굵은 글씨 안에 *기울임*을 넣는 중첩 서식**도 테스트합니다.
                
                ## 2. 제목 단계
                
                ### 3단계 제목
                
                #### 4단계 제목
                
                ##### 5단계 제목
                
                ###### 6단계 제목
                
                ## 3. 목록
                
                ### 순서 없는 목록
                
                - 사과
                - 바나나
                  - 노란 바나나
                  - 초록 바나나
                    - 덜 익은 바나나
                - 포도
                
                ### 순서 있는 목록
                
                1. 첫 번째 단계
                2. 두 번째 단계
                   1. 하위 단계 A
                   2. 하위 단계 B
                3. 세 번째 단계
                
                ### 혼합 목록
                
                1. 첫 번째 단계
                2. 두 번째 단계
                    - 하위 단계 A
                    - 하위 단계 B
                3. 세 번째 단계
                
                ## 4. 인용문 (미구현)
                
                > 이것은 인용문입니다.
                >
                > 인용문 안에 여러 문단이 있을 수 있습니다.
                >
                > > 중첩된 인용문도 확인합니다.
                
                ## 5. 코드 블록
                
                인라인 코드는 `const value = 42;`처럼 표시합니다.
                
                ```java
                public class HelloMarkdown {
                    public static void main(String[] args) {
                        System.out.println("Hello, Markdown!");
                    }
                }
                ```
                
                언어 지정이 없는 코드 블록도 테스트합니다.
                
                ```
                plain text
                <not-an-html-tag>
                A & B
                ```
                
                ## 6. 링크와 이미지 (이미지 미구현)
                
                - 일반 링크: [OpenAI](https://openai.com)
                - URL이 포함된 링크: [Markdown 가이드](https://www.markdownguide.org/)
                
                ## 7. 수평선
                
                위 문장과 수평선 사이에 구분선이 있습니다.
                
                ---
                
                수평선 아래의 문장입니다.
                
                ## 8. 표 (미구현)
                
                | 이름 | 유형 | 설명 |
                | --- | --- | --- |
                | Text | 인라인 | 일반 텍스트 |
                | Paragraph | 블록 | 문단 |
                | Heading | 블록 | 제목 |
                | CodeBlock | 블록 | 코드 |
                
                ## 9. 이스케이프와 특수 문자
                
                특수 문자: & < > " '
                
                이스케이프된 기호: \\*별표\\*, \\_밑줄\\_, \\# 샵
                
                ## 10. 줄바꿈과 빈 줄
                
                첫 번째 문단입니다.
                
                두 번째 문단입니다. 같은 문단 내 일반 줄바꿈이 어떻게 처리되는지도 확인하세요.
                이 문장은 바로 앞 줄 다음에 있습니다.
                
                마지막 문단입니다.
                
                ## 11. 혼합 테스트
                
                > **주의:** 다음 코드는 예시입니다.
                >
                > ```js
                > const message = "Hello <world>";
                > console.log(message);
                > ```
                
                목록 안의 `인라인 코드`와 **굵은 텍스트**도 확인합니다.
                
                - 첫 항목
                - 두 번째 항목에는 [링크](https://example.com)가 있습니다.
                - 세 번째 항목
                
                ## 12. HTML 입력 처리 확인
                
                일반 텍스트로 입력된 HTML 유사 문자열:
                
                `<script>alert("xss")</script>`
                
                다음 문장은 HTML 이스케이프 처리 결과를 확인하기 위한 문자열입니다.
                
                `<b>이 문자열은 코드로 표시되어야 합니다.</b>`
                """);

        System.out.println("=============to html=============");
        System.out.println(result);
        System.out.println("=====================================");
    }
}