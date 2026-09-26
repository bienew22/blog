package bienew.blog.backend.markdown.extension;

import bienew.blog.backend.markdown.extension.attribute.AttributeBlockParser;
import bienew.blog.backend.markdown.extension.attribute.AttributePostProcessor;
import bienew.blog.backend.markdown.extension.attribute.AttributeProvider;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class AttributeExtension implements Parser.ParserExtension, HtmlRenderer.HtmlRendererExtension {

    private static final AttributeExtension INSTANCE = new AttributeExtension();

    private AttributeExtension() {}

    public static AttributeExtension create() {
        return INSTANCE;
    }

    @Override
    public void extend(Parser.Builder parserBuilder) {
        parserBuilder.customBlockParserFactory(new AttributeBlockParser.Factory());

        parserBuilder.postProcessor(new AttributePostProcessor());
    }

    @Override
    public void extend(HtmlRenderer.Builder rendererBuilder) {
        rendererBuilder.attributeProviderFactory(context -> new AttributeProvider());
    }
}
