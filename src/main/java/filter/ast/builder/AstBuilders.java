package filter.ast.builder;

import filter.FilterLexer;
import filter.FilterParser;
import filter.ast.nodes.Expr;
import java.util.function.Function;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class AstBuilders {

  public static Expr fromQuery(String query, Function<FilterParser.QueryContext, Expr> translator) {
    return simplify(translator.apply(parse(query)));
  }

  public static Expr simplify(Expr e) {

          return switch (e) {

              case Expr.Not(var inner) -> {
                  Expr s = simplify(inner);

                  if (s instanceof Expr.Not(var inner2)) {
                      yield simplify(inner2);
                  }

                  yield new Expr.Not(s);
              }

              case Expr.And(var l, var r) ->
                  new Expr.And(simplify(l), simplify(r));

              case Expr.Or(var l, var r) ->
                  new Expr.Or(simplify(l), simplify(r));

              case Expr.Comparison c -> c;

              case Expr.InList i -> i;
          };

  }

  public static FilterParser.QueryContext parse(String query) {
    var cs = CharStreams.fromString(query);
    var lexer = new FilterLexer(cs);
    var tokens = new CommonTokenStream(lexer);
    var parser = new FilterParser(tokens);

    var ctx = parser.query();
    if (parser.getNumberOfSyntaxErrors() > 0)
      throw new IllegalStateException("Syntax errors in query: " + query);

    return ctx;
  }
}
