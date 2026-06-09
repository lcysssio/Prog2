import static org.junit.jupiter.api.Assertions.*;

import highlighting.core.HighlightRegion;
import highlighting.presets.MiniJavaTokens;
import highlighting.regex.Token;
import java.util.List;
import org.junit.jupiter.api.Test;

public class Tests {

  private List<Token> tokens() {
    return MiniJavaTokens.defaultTokens();
  }

  private Token findTokenByRegex(String regex) {
    for (Token t : tokens()) {
      if (t.pattern().pattern().equals(regex)) return t;
    }
    System.out.println("Kein exaktes Pattern gefunden für: " + regex);
    System.out.println("Verfügbare Patterns:");
    for (Token t : tokens()) {
      System.err.println(" - " + t.pattern().pattern());
    }
    throw new AssertionError("Token nicht gefunden: " + regex);
  }

  // Tests sind doof
  @Test
  void keyword_treffer_am_anfang_mitte_ende() {
    Token keyword =
        findTokenByRegex("\\b(package|import|class|public|private|final|return|null|new)\\b");

    assertFalse(keyword.test("class A {}").isEmpty());
    assertFalse(keyword.test("A class B").isEmpty());
    assertFalse(keyword.test("A B return").isEmpty());
  }

  @Test
  void keyword_mehrere_treffer_und_kein_treffer() {
    Token keyword =
        findTokenByRegex("\\b(package|import|class|public|private|final|return|null|new)\\b");

    List<HighlightRegion> multiple = keyword.test("public class A { return null; }");
    assertTrue(multiple.size() >= 4);

    List<HighlightRegion> none = keyword.test("className returnValue publicX");
    assertTrue(none.isEmpty());
  }

  @Test
  void kommentar_enthaelt_keyword_aehnlichen_text() {
    Token lineComment = findTokenByRegex("//[^\\n]*");
    String text = "// if return class new";

    List<HighlightRegion> matches = lineComment.test(text);
    assertEquals(1, matches.size());
    assertEquals(0, matches.get(0).start());
    assertEquals(text.length(), matches.get(0).end());
  }

  @Test
  void annotation_am_zeilenanfang_und_mit_leerzeichen() {
    Token annotation = findTokenByRegex("@Override\\b");

    assertEquals(1, annotation.test("@Override").size());
    assertEquals(1, annotation.test("   @Override").size());
    assertTrue(annotation.test("@Overrides").isEmpty());
  }

  @Test
  void string_mit_comment_markern_im_inhalt() {
    Token stringToken = findTokenByRegex("\"([^\"\\\\]|\\\\.)*\"");

    assertEquals(1, stringToken.test("\"http://example\"").size());
    assertEquals(1, stringToken.test("\"not a /* comment */\"").size());
  }

  @Test
  void block_und_javadoc_mehrere_und_kein_treffer() {
    Token block = findTokenByRegex("/\\*[\\s\\S]*?\\*/");
    Token javadoc = findTokenByRegex("/\\*\\*[\\s\\S]*?\\*/");

    assertEquals(2, block.test("/* a */ x /* b */").size());
    assertEquals(1, javadoc.test("/** doc */").size());
    assertTrue(block.test("kein kommentar").isEmpty());
  }
}
