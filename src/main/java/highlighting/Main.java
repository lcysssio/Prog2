package highlighting;

import highlighting.antlr.*;
import highlighting.core.SyntaxHighlighter;
import highlighting.presets.Texts;
import highlighting.regex.*;
import highlighting.ui.EditorUI;
import java.util.Scanner;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class Main {

  public static void main(String... args) {

    Scanner scanner = new Scanner(System.in);
    System.out.println("Wähle die Einrückungsstufe 2, 4, 8 ");
    int indentWidth = scanner.nextInt();

    // Demonstration: run the PrettyPrinter on several MiniJava examples
    String[] examples = new String[] {
        // simple class with a field and a method
        "package demo;\nclass Simple {\n  int field;\n  public int getField() {\n    return field;\n  }\n}",
        // method with if/else and while (use identifiers only, no numeric literals)
        "package demo;\nclass Control {\n  public int count() {\n    int i;\n    int j;\n    while (i) {\n      if (i) i = j;\n      else j = i;\n    }\n    return i;\n  }\n}",
        // nested blocks (assignments between identifiers)
        "package demo;\nclass Nest {\n  public int calc() {\n    int a;\n    {\n      int b;\n      {\n        int c;\n        c = a;\n      }\n    }\n    return a;\n  }\n}"
    };

    for (int ex = 0; ex < examples.length; ex++) {
      String sample = examples[ex];
      System.out.println("\n=== Example " + (ex+1) + " (original) ===\n" + sample + "\n");
      try {
        var chars = CharStreams.fromString(sample);
        var lexer = new MiniJavaLexer(chars);
        var tokens = new CommonTokenStream(lexer);
        var parser = new MiniJavaParser(tokens);

        var tree = parser.compilationUnit();
        PrettyPrinterVisitor visitor = new PrettyPrinterVisitor(indentWidth);
        visitor.visit(tree);
        System.out.println("--- Formatted (indent=" + indentWidth + ") ---\n" + visitor.result());
      } catch (Exception e) {
        System.out.println("Parsing/printing failed: " + e.getMessage());
      }
    }

    // Phase I: RegexHighlighter
    SyntaxHighlighter regex = new RegexHighlighter();

    // Phase II: ScanningHighlighter
    SyntaxHighlighter scanning = new ScanningHighlighter();

    // Phase III: AntlrTokenCollector (tokenbasiert)
    SyntaxHighlighter antlrToken = new AntlrTokenCollector();

    // and go ...
    EditorUI.show(Texts.START_TEXT, regex);
    EditorUI.show(Texts.START_TEXT, scanning);
    // EditorUI.show(Texts.START_TEXT, antlrToken);
  }
}
