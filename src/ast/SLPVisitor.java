package ast;

import ast.nodes.*;
import ast.types.*;
import parser.SimpLanPlusBaseVisitor;
import parser.SimpLanPlusParser;

import java.util.ArrayList;

public class SLPVisitor extends SimpLanPlusBaseVisitor<Node> {
    @Override
    public Node visitSimpleProg(SimpLanPlusParser.SimpleProgContext ctx) {
        return new ProgSimpleNode(this.visit(ctx.exp()));
    }

    @Override
    public Node visitComplexProg(SimpLanPlusParser.ComplexProgContext ctx) {
        ArrayList<Node> declarationList = new ArrayList<>();
        ArrayList<Node> statementList = new ArrayList<>();

        for (SimpLanPlusParser.DecContext dc : ctx.dec())
            declarationList.add(this.visit(dc));

        for (SimpLanPlusParser.StmContext sc : ctx.stm())
            statementList.add(this.visit(sc));

        if (ctx.exp() != null)
            return new ProgComplexNode(declarationList, statementList, this.visit(ctx.exp()));
        else
            return new ProgComplexNode(declarationList, statementList, null);
    }

    @Override
    public Node visitVarDec(SimpLanPlusParser.VarDecContext ctx) {
        Type type = (Type) this.visit(ctx.type());

        return new VarDeclarationNode(ctx.ID().getText(), type);
    }

    @Override
    public Node visitFunDec(SimpLanPlusParser.FunDecContext ctx) {
        ArrayList<ParamNode> paramList = new ArrayList<>();

        for (SimpLanPlusParser.ParamContext pc : ctx.param())
            paramList.add((ParamNode) this.visit(pc));

        return new FunDecNode(ctx.ID().getText(), (Type) this.visit(ctx.type()), paramList,
                (BodyNode) this.visit(ctx.body()));
    }

    @Override
    public Node visitParam(SimpLanPlusParser.ParamContext ctx) {
        return new ParamNode(ctx.ID().getText(), (Type) this.visit(ctx.type()));
    }

    @Override
    public Node visitBody(SimpLanPlusParser.BodyContext ctx) {
        ArrayList<Node> declarationList = new ArrayList<>();
        ArrayList<Node> statementList = new ArrayList<>();

        for (SimpLanPlusParser.DecContext dc : ctx.dec())
            declarationList.add(this.visit(dc));

        for (SimpLanPlusParser.StmContext sc : ctx.stm())
            statementList.add(this.visit(sc));

        if (ctx.exp() != null)
            return new BodyNode(declarationList, statementList, this.visit(ctx.exp()));
        else
            return new BodyNode(declarationList, statementList, null);
    }

    @Override
    public Node visitType(SimpLanPlusParser.TypeContext ctx) {
        String type = ctx.getText();

        return switch (type) {
            case "int" -> new IntType();
            case "bool" -> new BoolType();
            default -> new VoidType();
        };
    }

    @Override
    public Node visitInitStm(SimpLanPlusParser.InitStmContext ctx) {
        return new VarAssNode(ctx.ID().getText(), this.visit(ctx.exp()));
    }

    @Override
    public Node visitFunStm(SimpLanPlusParser.FunStmContext ctx) {
        ArrayList<Node> argumentList = new ArrayList<>();

        for (SimpLanPlusParser.ExpContext ec : ctx.exp())
            argumentList.add(this.visit(ec));

        return new FunStmNode(ctx.ID().getText(), argumentList);
    }

    @Override
    public Node visitIfStm(SimpLanPlusParser.IfStmContext ctx) {
        return new IfStmNode(this.visit(ctx.exp()), (IfBodyStmNode) this.visit(ctx.then),
                (IfBodyStmNode) this.visit(ctx.else_));
    }

    @Override
    public Node visitVarExp(SimpLanPlusParser.VarExpContext ctx) {
        return new IdNode(ctx.ID().getText());
    }

    @Override
    public Node visitRealtionalExp(SimpLanPlusParser.RealtionalExpContext ctx) {
        if (ctx.gr != null)
            return new GreaterNode(this.visit(ctx.left), this.visit(ctx.right));
        else if (ctx.min != null)
            return new MinorNode(this.visit(ctx.left), this.visit(ctx.right));
        else if (ctx.greq != null)
            return new GreatEqualNode(this.visit(ctx.left), this.visit(ctx.right));
        else if (ctx.mineq != null)
            return new MinEqualNode(this.visit(ctx.left), this.visit(ctx.right));
        else
            return new ComparisonNode(this.visit(ctx.left), this.visit(ctx.right));
    }

    @Override
    public Node visitArithmeticExp(SimpLanPlusParser.ArithmeticExpContext ctx) {
        if (ctx.plus != null)
            return new PlusNode(this.visit(ctx.left), this.visit(ctx.right));
        else if (ctx.minus != null)
            return new MinusNode(this.visit(ctx.left), this.visit(ctx.right));
        else if (ctx.mul != null)
            return new MultiplicationNode(this.visit(ctx.left), this.visit(ctx.right));
        else
            return new DivisionNode(this.visit(ctx.left), this.visit(ctx.right));
    }

    @Override
    public Node visitIfExp(SimpLanPlusParser.IfExpContext ctx) {
        return new IfExpNode(this.visit(ctx.exp()), (IfBodyExpNode) this.visit(ctx.then),
                (IfBodyExpNode) this.visit(ctx.else_));
    }

    @Override
    public Node visitBoolExp(SimpLanPlusParser.BoolExpContext ctx) {
        return new BoolNode(Boolean.parseBoolean(ctx.getText()));
    }

    @Override
    public Node visitFunExp(SimpLanPlusParser.FunExpContext ctx) {
        ArrayList<Node> argumentList = new ArrayList<>();

        for (SimpLanPlusParser.ExpContext ec : ctx.exp())
            argumentList.add(this.visit(ec));

        return new FunCallNode(ctx.ID().getText(), argumentList);
    }

    @Override
    public Node visitLogicalExp(SimpLanPlusParser.LogicalExpContext ctx) {
        return ctx.and != null ? new AndNode(this.visit(ctx.left), this.visit(ctx.right)) :
                new OrNode(this.visit(ctx.left), this.visit(ctx.right));
    }

    @Override
    public Node visitNotExp(SimpLanPlusParser.NotExpContext ctx) {
        return new NotNode(this.visit(ctx.exp()));
    }

    @Override
    public Node visitBracketsExp(SimpLanPlusParser.BracketsExpContext ctx) {
        return this.visit(ctx.exp());
    }

    @Override
    public Node visitIntExp(SimpLanPlusParser.IntExpContext ctx) {
        return new IntNode(Integer.parseInt(ctx.INTEGER().getText()));
    }

    @Override
    public Node visitIfBodyE(SimpLanPlusParser.IfBodyEContext ctx) {
        ArrayList<Node> statementList = new ArrayList<>();

        for (SimpLanPlusParser.StmContext sc : ctx.stm())
            statementList.add(this.visit(sc));

        return new IfBodyExpNode(statementList, this.visit(ctx.exp()));
    }

    @Override
    public Node visitIfBodyS(SimpLanPlusParser.IfBodySContext ctx) {
        ArrayList<Node> statementList = new ArrayList<>();

        for (SimpLanPlusParser.StmContext sc : ctx.stm())
            statementList.add(this.visit(sc));

        return new IfBodyStmNode(statementList);
    }
}
