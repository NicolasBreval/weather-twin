// Generated from AggregatorWtal.g4 by ANTLR 4.13.2
package org.nbreval.weather_twin.gateway.infrastructure.util.wtal;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link AggregatorWtalParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface AggregatorWtalVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link AggregatorWtalParser#start}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStart(AggregatorWtalParser.StartContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AssignStatement}
	 * labeled alternative in {@link AggregatorWtalParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignStatement(AggregatorWtalParser.AssignStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code EvalStatement}
	 * labeled alternative in {@link AggregatorWtalParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEvalStatement(AggregatorWtalParser.EvalStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FilterFunction}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilterFunction(AggregatorWtalParser.FilterFunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Mod}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMod(AggregatorWtalParser.ModContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MulDiv}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulDiv(AggregatorWtalParser.MulDivContext ctx);
	/**
	 * Visit a parse tree produced by the {@code In}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIn(AggregatorWtalParser.InContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnaryMinus}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryMinus(AggregatorWtalParser.UnaryMinusContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Atomic}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtomic(AggregatorWtalParser.AtomicContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MapFunction}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapFunction(AggregatorWtalParser.MapFunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IsNullFunction}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIsNullFunction(AggregatorWtalParser.IsNullFunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PlusMinus}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlusMinus(AggregatorWtalParser.PlusMinusContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Parenthesis}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenthesis(AggregatorWtalParser.ParenthesisContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SqrtFunction}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSqrtFunction(AggregatorWtalParser.SqrtFunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Accesor}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAccesor(AggregatorWtalParser.AccesorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Comparators}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparators(AggregatorWtalParser.ComparatorsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TernaryFunction}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTernaryFunction(AggregatorWtalParser.TernaryFunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PowFunction}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPowFunction(AggregatorWtalParser.PowFunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AndOr}
	 * labeled alternative in {@link AggregatorWtalParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndOr(AggregatorWtalParser.AndOrContext ctx);
	/**
	 * Visit a parse tree produced by {@link AggregatorWtalParser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtom(AggregatorWtalParser.AtomContext ctx);
	/**
	 * Visit a parse tree produced by {@link AggregatorWtalParser#lambda_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLambda_expression(AggregatorWtalParser.Lambda_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link AggregatorWtalParser#json_object}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_object(AggregatorWtalParser.Json_objectContext ctx);
	/**
	 * Visit a parse tree produced by {@link AggregatorWtalParser#json_pair}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_pair(AggregatorWtalParser.Json_pairContext ctx);
	/**
	 * Visit a parse tree produced by {@link AggregatorWtalParser#json_array}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_array(AggregatorWtalParser.Json_arrayContext ctx);
	/**
	 * Visit a parse tree produced by {@link AggregatorWtalParser#json_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson_value(AggregatorWtalParser.Json_valueContext ctx);
}