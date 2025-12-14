// Generated from AggregatorWtal.g4 by ANTLR 4.13.2
package org.nbreval.weather_twin.gateway.infrastructure.util.wtal;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link AggregatorWtalParser}.
 */
public interface AggregatorWtalListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link AggregatorWtalParser#start}.
	 * @param ctx the parse tree
	 */
	void enterStart(AggregatorWtalParser.StartContext ctx);
	/**
	 * Exit a parse tree produced by {@link AggregatorWtalParser#start}.
	 * @param ctx the parse tree
	 */
	void exitStart(AggregatorWtalParser.StartContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AssignStatement}
	 * labeled alternative in {@link AggregatorWtalParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterAssignStatement(AggregatorWtalParser.AssignStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AssignStatement}
	 * labeled alternative in {@link AggregatorWtalParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitAssignStatement(AggregatorWtalParser.AssignStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code EvalStatement}
	 * labeled alternative in {@link AggregatorWtalParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterEvalStatement(AggregatorWtalParser.EvalStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code EvalStatement}
	 * labeled alternative in {@link AggregatorWtalParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitEvalStatement(AggregatorWtalParser.EvalStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FilterFunction}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterFilterFunction(AggregatorWtalParser.FilterFunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FilterFunction}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitFilterFunction(AggregatorWtalParser.FilterFunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Mod}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMod(AggregatorWtalParser.ModContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Mod}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMod(AggregatorWtalParser.ModContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MulDiv}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMulDiv(AggregatorWtalParser.MulDivContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MulDiv}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMulDiv(AggregatorWtalParser.MulDivContext ctx);
	/**
	 * Enter a parse tree produced by the {@code In}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterIn(AggregatorWtalParser.InContext ctx);
	/**
	 * Exit a parse tree produced by the {@code In}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitIn(AggregatorWtalParser.InContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UnaryMinus}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterUnaryMinus(AggregatorWtalParser.UnaryMinusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UnaryMinus}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitUnaryMinus(AggregatorWtalParser.UnaryMinusContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Atomic}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAtomic(AggregatorWtalParser.AtomicContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Atomic}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAtomic(AggregatorWtalParser.AtomicContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MapFunction}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMapFunction(AggregatorWtalParser.MapFunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MapFunction}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMapFunction(AggregatorWtalParser.MapFunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IsNullFunction}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterIsNullFunction(AggregatorWtalParser.IsNullFunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IsNullFunction}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitIsNullFunction(AggregatorWtalParser.IsNullFunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PlusMinus}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterPlusMinus(AggregatorWtalParser.PlusMinusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PlusMinus}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitPlusMinus(AggregatorWtalParser.PlusMinusContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Parenthesis}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterParenthesis(AggregatorWtalParser.ParenthesisContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Parenthesis}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitParenthesis(AggregatorWtalParser.ParenthesisContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SqrtFunction}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterSqrtFunction(AggregatorWtalParser.SqrtFunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SqrtFunction}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitSqrtFunction(AggregatorWtalParser.SqrtFunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Accesor}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAccesor(AggregatorWtalParser.AccesorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Accesor}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAccesor(AggregatorWtalParser.AccesorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Comparators}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterComparators(AggregatorWtalParser.ComparatorsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Comparators}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitComparators(AggregatorWtalParser.ComparatorsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TernaryFunction}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterTernaryFunction(AggregatorWtalParser.TernaryFunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TernaryFunction}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitTernaryFunction(AggregatorWtalParser.TernaryFunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PowFunction}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterPowFunction(AggregatorWtalParser.PowFunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PowFunction}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitPowFunction(AggregatorWtalParser.PowFunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AndOr}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAndOr(AggregatorWtalParser.AndOrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AndOr}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAndOr(AggregatorWtalParser.AndOrContext ctx);
	/**
	 * Enter a parse tree produced by {@link AggregatorWtalParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterAtom(AggregatorWtalParser.AtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link AggregatorWtalParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitAtom(AggregatorWtalParser.AtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link AggregatorWtalParser#lambda_expression}.
	 * @param ctx the parse tree
	 */
	void enterLambda_expression(AggregatorWtalParser.Lambda_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link AggregatorWtalParser#lambda_expression}.
	 * @param ctx the parse tree
	 */
	void exitLambda_expression(AggregatorWtalParser.Lambda_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link AggregatorWtalParser#json_object}.
	 * @param ctx the parse tree
	 */
	void enterJson_object(AggregatorWtalParser.Json_objectContext ctx);
	/**
	 * Exit a parse tree produced by {@link AggregatorWtalParser#json_object}.
	 * @param ctx the parse tree
	 */
	void exitJson_object(AggregatorWtalParser.Json_objectContext ctx);
	/**
	 * Enter a parse tree produced by {@link AggregatorWtalParser#json_pair}.
	 * @param ctx the parse tree
	 */
	void enterJson_pair(AggregatorWtalParser.Json_pairContext ctx);
	/**
	 * Exit a parse tree produced by {@link AggregatorWtalParser#json_pair}.
	 * @param ctx the parse tree
	 */
	void exitJson_pair(AggregatorWtalParser.Json_pairContext ctx);
	/**
	 * Enter a parse tree produced by {@link AggregatorWtalParser#json_array}.
	 * @param ctx the parse tree
	 */
	void enterJson_array(AggregatorWtalParser.Json_arrayContext ctx);
	/**
	 * Exit a parse tree produced by {@link AggregatorWtalParser#json_array}.
	 * @param ctx the parse tree
	 */
	void exitJson_array(AggregatorWtalParser.Json_arrayContext ctx);
	/**
	 * Enter a parse tree produced by {@link AggregatorWtalParser#json_value}.
	 * @param ctx the parse tree
	 */
	void enterJson_value(AggregatorWtalParser.Json_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link AggregatorWtalParser#json_value}.
	 * @param ctx the parse tree
	 */
	void exitJson_value(AggregatorWtalParser.Json_valueContext ctx);
}