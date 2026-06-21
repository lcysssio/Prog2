package filter.ast;

import filter.ast.builder.AstBuilderPattern;
import filter.ast.builder.AstBuilderVisitor;
import filter.ast.builder.AstBuilders;
import filter.ast.nodes.CompOp;
import filter.ast.nodes.Expr;
import filter.ast.nodes.Value;
import filter.ast.printer.AstPrinter;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;
import org.lambda.query.Query;

public class ApprovalTest {

    @Test
    void artistEqualsBeatles() {
        Expr expr = new Expr.Comparison(
            "artist",
            filter.ast.nodes.CompOp.EQ,
            new filter.ast.nodes.Value.Str("Beatles"));
        System.out.println(AstPrinter.toString(expr));
        Approvals.verify(
            AstPrinter.toString(expr)
        );
    }
    @Test
    void approvalTestAnd() {
        Expr expr =
            new Expr.And(
                new Expr.Comparison(
                    "artist",
                    CompOp.EQ,
                    new Value.Str("Beatles")),
                new Expr.Comparison(
                    "year",
                    CompOp.EQ,
                    new Value.Num(1968)));

        Approvals.verify(
            AstPrinter.toString(expr)
        );
    }
    @Test
    void approvalTestOr() {

        Expr expr =
            new Expr.Or(
                new Expr.Comparison(
                    "artist",
                    CompOp.EQ,
                    new Value.Str("Beatles")),
                new Expr.Comparison(
                    "artist",
                    CompOp.EQ,
                    new Value.Str("Queen")));
        Approvals.verify(
            AstPrinter.toString(expr)
        );
    }

}
