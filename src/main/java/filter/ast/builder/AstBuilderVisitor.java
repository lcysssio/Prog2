package filter.ast.builder;

import filter.FilterBaseVisitor;
import filter.FilterParser;
import filter.ast.nodes.CompOp;
import filter.ast.nodes.Expr;
import filter.ast.nodes.Value;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class AstBuilderVisitor extends FilterBaseVisitor<Void> {

  private final Deque<Expr> exprStack = new ArrayDeque<>();
  private final Deque<Value> valueStack = new ArrayDeque<>();
  private final Deque<List<Value>> valuesStack = new ArrayDeque<>();

  // Public entry point
  public Expr translate(FilterParser.QueryContext ctx) {
    exprStack.clear();
    valueStack.clear();
    valuesStack.clear();

    visit(ctx);
    if (exprStack.size() != 1) {
      throw new IllegalStateException("Expected exactly one expression on stack, got " + exprStack.size());
    }
    return exprStack.pop();
  }

  // query  : expr EOF
  @Override
  public Void visitQuery(FilterParser.QueryContext ctx) {
    visit(ctx.expr());
    return null;
  }

  // expr: orExpr
  @Override
  public Void visitExpr(FilterParser.ExprContext ctx) {
    visit(ctx.orExpr());
    return null;
  }

  // orExpr : andExpr (OR andExpr)*
  @Override
  public Void visitOrExpr(FilterParser.OrExprContext ctx) {
    visit(ctx.andExpr(0));
    Expr acc = exprStack.pop();

    for (int i = 1; i < ctx.andExpr().size(); i++) {
      visit(ctx.andExpr(i));
      Expr right = exprStack.pop();
      acc = new Expr.Or(acc, right);
    }

    exprStack.push(acc);
    return null;
  }

  // andExpr: notExpr (AND notExpr)*
  @Override
  public Void visitAndExpr(FilterParser.AndExprContext ctx) {
    visit(ctx.notExpr(0));
    Expr acc = exprStack.pop();

    for (int i = 1; i < ctx.notExpr().size(); i++) {
      visit(ctx.notExpr(i));
      Expr right = exprStack.pop();
      acc = new Expr.And(acc, right);
    }

    exprStack.push(acc);
    return null;
  }

  // notExpr: NOT notExpr | primary
  @Override
  public Void visitNotExpr(FilterParser.NotExprContext ctx) {
    if (ctx.NOT() != null) {
      visit(ctx.notExpr());
      Expr inner = exprStack.pop();
      exprStack.push(new Expr.Not(inner));
    } else {
      visit(ctx.primary());
    }
    return null;
  }

  // primary: comparison | '(' expr ')'
  @Override
  public Void visitPrimary(FilterParser.PrimaryContext ctx) {
    if (ctx.comparison() != null) {
      visit(ctx.comparison());
    } else {
      visit(ctx.expr());
    }
    return null;
  }

  // comparison
  //   : IDENTIFIER op=COMPOP value=literal
  //   | IDENTIFIER IN '(' literalList ')'
  @Override
  public Void visitComparison(FilterParser.ComparisonContext ctx) {
    String field = ctx.IDENTIFIER().getText();

    if (ctx.IN() != null) {
      visit(ctx.literalList());
      List<Value> values = valuesStack.pop();
      exprStack.push(new Expr.InList(field, values));
    } else {
      visit(ctx.value);
      Value value = valueStack.pop();
      CompOp op = CompOp.fromSymbol(ctx.op.getText());
      exprStack.push(new Expr.Comparison(field, op, value));
    }
    return null;
  }

  // literalList: literal (',' literal)*
  @Override
  public Void visitLiteralList(FilterParser.LiteralListContext ctx) {
    List<Value> values = new ArrayList<>();
    for (FilterParser.LiteralContext literalCtx : ctx.literal()) {
      visit(literalCtx);
      values.add(valueStack.pop());
    }
    valuesStack.push(values);
    return null;
  }

  // literal: STRING | NUMBER
  @Override
  public Void visitLiteral(FilterParser.LiteralContext ctx) {
    if (ctx.STRING() != null) {
      valueStack.push(new Value.Str(unquoteAndUnescape(ctx.STRING().getText())));
    } else {
      valueStack.push(new Value.Num(Integer.parseInt(ctx.NUMBER().getText())));
    }
    return null;
  }

  private String unquoteAndUnescape(String token) {
    if (token.length() < 2) return token;

    String inner = token.substring(1, token.length() - 1);
    StringBuilder sb = new StringBuilder(inner.length());

    boolean escaped = false;
    for (int i = 0; i < inner.length(); i++) {
      char c = inner.charAt(i);
      if (escaped) {
        sb.append(
            switch (c) {
              case 'n' -> '\n';
              case 'r' -> '\r';
              case 't' -> '\t';
              default -> c;
            });
        escaped = false;
      } else if (c == '\\') {
        escaped = true;
      } else {
        sb.append(c);
      }
    }

    if (escaped) {
      sb.append('\\');
    }
    return sb.toString();
  }
}
