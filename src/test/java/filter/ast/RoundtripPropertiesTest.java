package filter.ast;

import static org.junit.jupiter.api.Assertions.assertEquals;

import filter.FilterParser;
import filter.ast.builder.AstBuilderPattern;
import filter.ast.builder.AstBuilderVisitor;
import filter.ast.builder.AstBuilders;
import filter.ast.nodes.Expr;
import filter.ast.printer.AstPrinter;
import net.jqwik.api.*;

public class RoundtripPropertiesTest {

    @Property
    boolean roundtrip(@ForAll("simpleQueries") String query) {

        var ctx = AstBuilders.parse(query);

        Expr ast1 = new AstBuilderVisitor().translate(ctx);

        String printed = AstPrinter.toString(ast1);

        Expr ast2 = new AstBuilderVisitor().translate(AstBuilders.parse(printed));

        return ast1.equals(ast2);
    }

    @Property
    boolean printerStable(@ForAll("simpleQueries") String query) {

        var ctx = AstBuilders.parse(query);

        Expr ast = new AstBuilderVisitor().translate(ctx);

        String p1 = AstPrinter.toString(ast);
        String p2 = AstPrinter.toString(
            new AstBuilderVisitor().translate(AstBuilders.parse(p1))
        );

        return p1.equals(p2);
    }

    @Property
    boolean simplifyIdempotent(@ForAll("simpleQueries") String query) {

        var ctx = AstBuilders.parse(query);

        Expr ast = new AstBuilderVisitor().translate(ctx);

        Expr s1 = AstBuilders.simplify(ast);
        Expr s2 = AstBuilders.simplify(s1);

        return s1.equals(s2);
    }


    @Provide
  Arbitrary<String> fields() {
    return Arbitraries.of("title", "artist", "genre", "year");
  }

  @Provide
  Arbitrary<String> stringLiterals() {
    return Arbitraries.strings()
        .withChars("abcxyz")
        .ofMinLength(1)
        .ofMaxLength(5)
        .map(s -> "\"" + s + "\"");
  }

  @Provide
  Arbitrary<String> numberLiterals() {
    return Arbitraries.integers().between(1900, 2025).map(Object::toString);
  }

  @Provide
  Arbitrary<String> comparisons() {
    Arbitrary<String> ops = Arbitraries.of("==", "!=", "<", "<=", ">", ">=");

    Arbitrary<String> stringComp =
        Combinators.combine(fields(), ops, stringLiterals())
            .as((f, op, lit) -> f + " " + op + " " + lit);

    Arbitrary<String> numberComp =
        Combinators.combine(Arbitraries.of("year"), ops, numberLiterals())
            .as((f, op, lit) -> f + " " + op + " " + lit);

    return Arbitraries.oneOf(stringComp, numberComp);
  }

  @Provide
  Arbitrary<String> simpleQueries() {
    return comparisons()
        .list()
        .ofMinSize(1)
        .ofMaxSize(3)
        .map(
            list -> {
              if (list.size() == 1) return list.getFirst();
              StringBuilder sb = new StringBuilder();
              for (int i = 0; i < list.size(); i++) {
                if (i > 0) {
                  String conn = Arbitraries.of(" and ", " or ").sample();
                  sb.append(conn);
                }
                sb.append(list.get(i));
              }
              return sb.toString();
            });
  }
}
