package highlighting.antlr;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

/// MiniJava Pretty Printer (minimal, stateful)
///
/// Requirements:
/// - Reproduce the whole program (comments and whitespaces are gone).
/// - Ignore whitespace from the input; instead, generate:
///     - indentation for class bodies and blocks,
///     - exactly one line per statement (lines ending in ';').
///
/// Simplification:
/// Everything that is not indentation or line breaks is printed as raw tokens (with a very simple
/// space heuristic). Expression and signature formatting is therefore not "nice", which is
/// acceptable for this exercise.
public final class PrettyPrinterVisitor extends MiniJavaBaseVisitor<Void> {

  private final StringBuilder out = new StringBuilder();
  private final int indentWidth;
  private int currentIndent = 0;
  private boolean atLineStart = true;

  // For simple spacing between tokens:
  private Token lastToken = null;

  public PrettyPrinterVisitor(int indentWidth) {
    this.indentWidth = Math.max(0, indentWidth);
  }

  public String result() {
    return out.toString();
  }

  // ----------------------------------------------------
  // Structural methods – these enforce indentation and "one statement per line"
  //
  // TODO: implement the four structural visitXyz-methods below: visitCompilationUnit,
  // visitClassBody, visitBlock, and visitStatement
  // ----------------------------------------------------

  @Override
  public Void visitCompilationUnit(MiniJavaParser.CompilationUnitContext ctx) {
    // package
    if (ctx.packageDecl() != null) {
      visit(ctx.packageDecl());
      nl();
      nl();
    }

    // imports
    if (ctx.importDecl() != null && !ctx.importDecl().isEmpty()) {
      for (var imp : ctx.importDecl()) {
        visit(imp);
        nl();
      }
      nl();
    }

    // type
    var types = ctx.typeDecl();
    for (int i = 0; i < types.size(); i++) {
      visit(types.get(i));
      if (i + 1 < types.size()) nl();
    }

    return null;
  }

  @Override
  public Void visitClassBody(MiniJavaParser.ClassBodyContext ctx) {
    // Erzeuge sichtbare Struktur: öffnende Klammer, eingerückte Mitglieder, schließende Klammer.
    // (Wenn die umliegende Regel bereits die '{'/' }' schreibt, kannst du das hier anpassen.)
    writeln("{");
    currentIndent++;

    for (var decl : ctx.classBodyDeclaration()) {
      visit(decl);
      if (!atLineStart) nl();
    }

    currentIndent--;
    writeln("}");
    return null;
  }

  @Override
  public Void visitBlock(MiniJavaParser.BlockContext ctx) {
    // Blockstruktur: { newline, indented children, then } on its own line
    writeln("{");
    currentIndent++;

    for (var bs : ctx.blockStatement()) {
      visit(bs);
      if (!atLineStart) nl();
    }

    currentIndent--;
    writeln("}");
    return null;
  }

  @Override
  public Void visitStatement(MiniJavaParser.StatementContext ctx) {
    // Wenn Statement selbst ein Block ist, übertrage an visit(block)
    if (ctx.block() != null) {
      visit(ctx.block());
      return null;
    }

    // Andernfalls: besuche die Kinder
    visitChildren(ctx);

    if (!atLineStart) nl();
    return null;
  }

  // ---------------- helper methods ----------------

  private void indent(int indentWidth) {
    if (atLineStart) {
      int spaces = Math.max(0, indentWidth * currentIndent);
      out.append(" ".repeat(spaces));
      atLineStart = false;
    }
  }

  private void write(String s, int indentWidth) {
    if (s == null || s.isEmpty()) return;
    indent(indentWidth);
    out.append(s);
  }

  private void nl() {
    out.append('\n');
    atLineStart = true;
    lastToken = null; // Reset spacing context at the beginning of a line
  }

  private void writeln(String s) {
    write(s, indentWidth);
    nl();
  }

  // --------------- token output + basic spacing ---------------

  @Override
  public Void visitTerminal(TerminalNode node) {
    Token t = node.getSymbol();
    String text = t.getText();

    if (lastToken != null) {
      int prevType = lastToken.getType();
      int curType = t.getType();

      // Simple heuristic: insert a space between "word-like" tokens
      if (needsSpaceBetween(prevType, curType)) write(" ", indentWidth);
    }

    write(text, indentWidth);
    lastToken = t;
    return null;
  }

  private boolean needsSpaceBetween(int prevType, int curType) {
    return isWordLike(prevType) && isWordLike(curType);
  }

  private boolean isWordLike(int type) {
    return type == MiniJavaLexer.IDENTIFIER
        || type == MiniJavaLexer.STRING_LITERAL
        || type == MiniJavaLexer.CHAR_LITERAL
        || type == MiniJavaLexer.NULL
        || type == MiniJavaLexer.PACKAGE
        || type == MiniJavaLexer.IMPORT
        || type == MiniJavaLexer.CLASS
        || type == MiniJavaLexer.PUBLIC
        || type == MiniJavaLexer.PRIVATE
        || type == MiniJavaLexer.FINAL
        || type == MiniJavaLexer.RETURN
        || type == MiniJavaLexer.NEW
        || type == MiniJavaLexer.IF
        || type == MiniJavaLexer.ELSE
        || type == MiniJavaLexer.WHILE
        || type == MiniJavaLexer.EXTENDS
        || type == MiniJavaLexer.IMPLEMENTS;
  }
}
