package filter.ast;

import static org.junit.jupiter.api.Assertions.assertEquals;

import filter.ast.builder.AstBuilderPattern;
import filter.ast.builder.AstBuilderVisitor;
import filter.ast.builder.AstBuilders;
import filter.ast.nodes.CompOp;
import filter.ast.nodes.Expr;
import filter.ast.nodes.Value;
import filter.ast.printer.AstPrinter;
import org.junit.jupiter.api.Test;
import org.mockito.internal.debugging.LoggingListener;

public class AstTest {
    // TODO
    @Test
    public void vergleich() {
        var expr = new Expr.Comparison("artist", CompOp.EQ, new Value.Str("Beatles"));
        assertEquals("(artist == \"Beatles\")", AstPrinter.toString(expr));
    }

    @Test
    void AndOrNotInList() {
        var inList = new Expr.InList("genre", java.util.List.of(new Value.Str("rock"), new Value.Str("jazz")));
        var notExpr = new Expr.Not(new Expr.Comparison("artist", CompOp.NE, new Value.Str("Beatles")));
        var andExpr = new Expr.And(inList, notExpr);
        assertEquals("((genre in (\"rock\", \"jazz\")) and (not (artist != \"Beatles\")))", AstPrinter.toString(andExpr));
    }
}

