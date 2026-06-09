package highlighting.antlr;

import highlighting.core.HighlightRegion;
import highlighting.core.SyntaxHighlighter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import highlighting.presets.MiniJavaColours;
import org.antlr.v4.runtime.*;

// TODO Phase III — AntlrTokenCollector (token-based syntax highlighting).

// This highlighter uses the ANTLR-generated MiniJavaLexer to turn the input text into a token
// stream. {@code collectMatches(String)} is the only method you need to implement: extract tokens
// of interest and map them to {@code HighlightRegions} using the colours from {@code
// MiniJavaColours}. Sorting, filtering of invalid regions, and conflict handling are performed by
// the base class {@code SyntaxHighlighter} via the template method {@code computeRegions(...)}.
public class AntlrTokenCollector extends SyntaxHighlighter {

    // TODO (Phase III — implement this method): Use the token stream produced by the ANTLR-generated
    // {@code MiniJavaLexer} to collect highlight regions.
    //
    // Requirements / hints:
    // - Iterate over the lexer tokens (typically via {@code CommonTokenStream}); ignore the EOF
    // token.
    // - For each token type that should be coloured (e.g., keywords, string/char literals, comments),
    // create a {@code HighlightRegion} with the corresponding colour from {@code MiniJavaColours}.
    // - Use {@code Token#getStartIndex()} and {@code Token#getStopIndex()} (inclusive) to compute
    // {@code [start, end)} ranges: {@code start = startIndex, end = stopIndex + 1}.
    // - Do not sort, merge, or resolve overlaps here; return all candidates as you find them.
    // Normalisation and conflict resolution are handled later by the template method.
    // - Annotation highlighting: colour '@' and the immediately following IDENTIFIER token (if
    // present).
    @Override
    public List<HighlightRegion> collectMatches(String text) {
        var input = CharStreams.fromString(text);
        var lexer = new MiniJavaLexer(input);
        var tokens = new CommonTokenStream(lexer);

        tokens.fill(); // fill stream (fetch all tokens from lexer)

        var region = new ArrayList<HighlightRegion>();
        var Tokenliste = tokens.getTokens();
        for (int i = 0; i < Tokenliste.size(); i++) {
            var a = Tokenliste.get(i);
            int typ = a.getType();

            java.awt.Color Colour = null;
            //DAmit ich hier die Colors habe muss switch benutzt werden
            switch (typ) {
                case MiniJavaLexer.PACKAGE:
                case MiniJavaLexer.IMPORT:
                case MiniJavaLexer.CLASS:
                case MiniJavaLexer.PUBLIC:
                case MiniJavaLexer.PRIVATE:
                case MiniJavaLexer.FINAL:
                case MiniJavaLexer.RETURN:
                case MiniJavaLexer.NULL:
                case MiniJavaLexer.NEW:
                case MiniJavaLexer.IF:
                case MiniJavaLexer.ELSE:
                case MiniJavaLexer.WHILE:
                case MiniJavaLexer.EXTENDS:
                case MiniJavaLexer.IMPLEMENTS:
                    Colour = MiniJavaColours.KEYWORD_COLOUR;
                    break;
                case MiniJavaLexer.STRING_LITERAL:
                    Colour = MiniJavaColours.STRING_LITERAL_COLOUR;
                    break;
                case MiniJavaLexer.CHAR_LITERAL:
                    Colour = MiniJavaColours.CHAR_LITERAL_COLOUR;
                    break;
                case MiniJavaLexer.LINE_COMMENT:
                case MiniJavaLexer.BLOCK_COMMENT:
                    Colour = MiniJavaColours.BLOCK_COMMENT_COLOUR;
                    break;
                case MiniJavaLexer.JAVADOC_COMMENT:
                    Colour = MiniJavaColours.JAVADOC_COMMENT_COLOUR;
                    break;
                //Wieso konnte es nicht Annotation sein?
                case MiniJavaLexer.AT:
                    Colour = MiniJavaColours.ANNOTATION_COLOUR;
                    break;
                default:
                    Colour = null;
            }
            if (Colour != null) {
                int start = a.getStartIndex();
                int end = a.getStopIndex() +1;
                region.add(new HighlightRegion(start, end, Colour));
            }


        }
        return region;

    }

    public void Baum(String text) {
        var input = CharStreams.fromString(text);
        var lexer = new MiniJavaLexer(input);
        var tokens = new CommonTokenStream(lexer);

        var parser = new MiniJavaParser(tokens);
        var tree = parser.compilationUnit(); // Wurzelknoten des Baums (Startregel der Grammatik)

        IO.println(tree.toStringTree(parser));
    }


}
