package bienew.blog.backend.markdown.utils;

import bienew.blog.backend.markdown.ast.ASTNode;
import bienew.blog.backend.markdown.ast.block.Image;

import java.util.ArrayList;
import java.util.List;

public class MarkdownImageExtractor {

    public static List<String> extractImageUrls(ASTNode node) {
        List<String> imageUrls = new ArrayList<>();
        traverse(node, imageUrls);

        return imageUrls;
    }

    private static void traverse(ASTNode node, List<String> imageUrls) {

        if (node instanceof Image image) {
            imageUrls.add(image.getUrl());
        }

        for (ASTNode child : node.getChildren()) {
            traverse(child, imageUrls);
        }
    }
}
