// Generated from AggregatorWtal.g4 by ANTLR 4.13.2
package org.nbreval.weather_twin.gateway.infrastructure.util.wtal;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class AggregatorWtalParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		PLUS=1, MINUS=2, MULT=3, DIV=4, AND=5, OR=6, ASSIGN=7, ARROW=8, GREATER_THAN=9, 
		LESS_THAN=10, EQUALS=11, DISTINCT=12, GREATER_OR_EQUALS=13, LESS_OR_EQUALS=14, 
		MOD=15, IN=16, SEMI=17, POW=18, SQRT=19, MAP=20, FILTER=21, ISNULL=22, 
		TERNARY=23, NUMBER=24, POINT=25, COMMA=26, LBRACKET=27, RBRACKET=28, LBRACE=29, 
		RBRACE=30, COLON=31, LPAREN=32, RPAREN=33, SPACE=34, STRING=35, BOOLEAN=36, 
		NULL=37, IDENTIFIER=38;
	public static final int
		RULE_start = 0, RULE_statement = 1, RULE_expression = 2, RULE_atom = 3, 
		RULE_lambda_expression = 4, RULE_json_object = 5, RULE_json_pair = 6, 
		RULE_json_array = 7, RULE_json_value = 8;
	private static String[] makeRuleNames() {
		return new String[] {
			"start", "statement", "expression", "atom", "lambda_expression", "json_object", 
			"json_pair", "json_array", "json_value"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'+'", "'-'", "'*'", "'/'", "'&'", "'|'", "'='", "'->'", "'>'", 
			"'<'", "'=='", "'!='", "'>='", "'<='", "'%'", "'IN'", "';'", "'POW'", 
			"'SQRT'", "'MAP'", "'FILTER'", "'ISNULL'", "'TERNARY'", null, "'.'", 
			"','", "'['", "']'", "'{'", "'}'", "':'", "'('", "')'", null, null, null, 
			"'null'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "PLUS", "MINUS", "MULT", "DIV", "AND", "OR", "ASSIGN", "ARROW", 
			"GREATER_THAN", "LESS_THAN", "EQUALS", "DISTINCT", "GREATER_OR_EQUALS", 
			"LESS_OR_EQUALS", "MOD", "IN", "SEMI", "POW", "SQRT", "MAP", "FILTER", 
			"ISNULL", "TERNARY", "NUMBER", "POINT", "COMMA", "LBRACKET", "RBRACKET", 
			"LBRACE", "RBRACE", "COLON", "LPAREN", "RPAREN", "SPACE", "STRING", "BOOLEAN", 
			"NULL", "IDENTIFIER"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "AggregatorWtal.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public AggregatorWtalParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StartContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(AggregatorWtalParser.EOF, 0); }
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public StartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_start; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).enterStart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).exitStart(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AggregatorWtalVisitor ) return ((AggregatorWtalVisitor<? extends T>)visitor).visitStart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StartContext start() throws RecognitionException {
		StartContext _localctx = new StartContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_start);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(19); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(18);
				statement();
				}
				}
				setState(21); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 520395423748L) != 0) );
			setState(23);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementContext extends ParserRuleContext {
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
	 
		public StatementContext() { }
		public void copyFrom(StatementContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AssignStatementContext extends StatementContext {
		public TerminalNode IDENTIFIER() { return getToken(AggregatorWtalParser.IDENTIFIER, 0); }
		public TerminalNode ASSIGN() { return getToken(AggregatorWtalParser.ASSIGN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(AggregatorWtalParser.SEMI, 0); }
		public AssignStatementContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).enterAssignStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).exitAssignStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AggregatorWtalVisitor ) return ((AggregatorWtalVisitor<? extends T>)visitor).visitAssignStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class EvalStatementContext extends StatementContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(AggregatorWtalParser.SEMI, 0); }
		public EvalStatementContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).enterEvalStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).exitEvalStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AggregatorWtalVisitor ) return ((AggregatorWtalVisitor<? extends T>)visitor).visitEvalStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_statement);
		try {
			setState(33);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				_localctx = new AssignStatementContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(25);
				match(IDENTIFIER);
				setState(26);
				match(ASSIGN);
				setState(27);
				expression(0);
				setState(28);
				match(SEMI);
				}
				break;
			case 2:
				_localctx = new EvalStatementContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(30);
				expression(0);
				setState(31);
				match(SEMI);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionContext extends ParserRuleContext {
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	 
		public ExpressionContext() { }
		public void copyFrom(ExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FilterFunctionContext extends ExpressionContext {
		public TerminalNode FILTER() { return getToken(AggregatorWtalParser.FILTER, 0); }
		public TerminalNode LPAREN() { return getToken(AggregatorWtalParser.LPAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(AggregatorWtalParser.COMMA, 0); }
		public Lambda_expressionContext lambda_expression() {
			return getRuleContext(Lambda_expressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(AggregatorWtalParser.RPAREN, 0); }
		public FilterFunctionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).enterFilterFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).exitFilterFunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AggregatorWtalVisitor ) return ((AggregatorWtalVisitor<? extends T>)visitor).visitFilterFunction(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ModContext extends ExpressionContext {
		public Token op;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode MOD() { return getToken(AggregatorWtalParser.MOD, 0); }
		public ModContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).enterMod(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).exitMod(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AggregatorWtalVisitor ) return ((AggregatorWtalVisitor<? extends T>)visitor).visitMod(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class MulDivContext extends ExpressionContext {
		public Token op;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode MULT() { return getToken(AggregatorWtalParser.MULT, 0); }
		public TerminalNode DIV() { return getToken(AggregatorWtalParser.DIV, 0); }
		public MulDivContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).enterMulDiv(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).exitMulDiv(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AggregatorWtalVisitor ) return ((AggregatorWtalVisitor<? extends T>)visitor).visitMulDiv(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class InContext extends ExpressionContext {
		public Token op;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode IN() { return getToken(AggregatorWtalParser.IN, 0); }
		public InContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).enterIn(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).exitIn(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AggregatorWtalVisitor ) return ((AggregatorWtalVisitor<? extends T>)visitor).visitIn(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class UnaryMinusContext extends ExpressionContext {
		public TerminalNode MINUS() { return getToken(AggregatorWtalParser.MINUS, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public UnaryMinusContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).enterUnaryMinus(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).exitUnaryMinus(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AggregatorWtalVisitor ) return ((AggregatorWtalVisitor<? extends T>)visitor).visitUnaryMinus(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AtomicContext extends ExpressionContext {
		public AtomContext atom() {
			return getRuleContext(AtomContext.class,0);
		}
		public AtomicContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).enterAtomic(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).exitAtomic(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AggregatorWtalVisitor ) return ((AggregatorWtalVisitor<? extends T>)visitor).visitAtomic(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class MapFunctionContext extends ExpressionContext {
		public TerminalNode MAP() { return getToken(AggregatorWtalParser.MAP, 0); }
		public TerminalNode LPAREN() { return getToken(AggregatorWtalParser.LPAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(AggregatorWtalParser.COMMA, 0); }
		public Lambda_expressionContext lambda_expression() {
			return getRuleContext(Lambda_expressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(AggregatorWtalParser.RPAREN, 0); }
		public MapFunctionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).enterMapFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).exitMapFunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AggregatorWtalVisitor ) return ((AggregatorWtalVisitor<? extends T>)visitor).visitMapFunction(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IsNullFunctionContext extends ExpressionContext {
		public TerminalNode ISNULL() { return getToken(AggregatorWtalParser.ISNULL, 0); }
		public TerminalNode LPAREN() { return getToken(AggregatorWtalParser.LPAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(AggregatorWtalParser.RPAREN, 0); }
		public IsNullFunctionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).enterIsNullFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).exitIsNullFunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AggregatorWtalVisitor ) return ((AggregatorWtalVisitor<? extends T>)visitor).visitIsNullFunction(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PlusMinusContext extends ExpressionContext {
		public Token op;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode PLUS() { return getToken(AggregatorWtalParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(AggregatorWtalParser.MINUS, 0); }
		public PlusMinusContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).enterPlusMinus(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).exitPlusMinus(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AggregatorWtalVisitor ) return ((AggregatorWtalVisitor<? extends T>)visitor).visitPlusMinus(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ParenthesisContext extends ExpressionContext {
		public TerminalNode LPAREN() { return getToken(AggregatorWtalParser.LPAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(AggregatorWtalParser.RPAREN, 0); }
		public ParenthesisContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).enterParenthesis(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).exitParenthesis(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AggregatorWtalVisitor ) return ((AggregatorWtalVisitor<? extends T>)visitor).visitParenthesis(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SqrtFunctionContext extends ExpressionContext {
		public TerminalNode SQRT() { return getToken(AggregatorWtalParser.SQRT, 0); }
		public TerminalNode LPAREN() { return getToken(AggregatorWtalParser.LPAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(AggregatorWtalParser.RPAREN, 0); }
		public SqrtFunctionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).enterSqrtFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).exitSqrtFunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AggregatorWtalVisitor ) return ((AggregatorWtalVisitor<? extends T>)visitor).visitSqrtFunction(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AccesorContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode LBRACKET() { return getToken(AggregatorWtalParser.LBRACKET, 0); }
		public TerminalNode RBRACKET() { return getToken(AggregatorWtalParser.RBRACKET, 0); }
		public TerminalNode COLON() { return getToken(AggregatorWtalParser.COLON, 0); }
		public AccesorContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).enterAccesor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).exitAccesor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AggregatorWtalVisitor ) return ((AggregatorWtalVisitor<? extends T>)visitor).visitAccesor(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ComparatorsContext extends ExpressionContext {
		public Token op;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode GREATER_THAN() { return getToken(AggregatorWtalParser.GREATER_THAN, 0); }
		public TerminalNode LESS_THAN() { return getToken(AggregatorWtalParser.LESS_THAN, 0); }
		public TerminalNode EQUALS() { return getToken(AggregatorWtalParser.EQUALS, 0); }
		public TerminalNode DISTINCT() { return getToken(AggregatorWtalParser.DISTINCT, 0); }
		public TerminalNode GREATER_OR_EQUALS() { return getToken(AggregatorWtalParser.GREATER_OR_EQUALS, 0); }
		public TerminalNode LESS_OR_EQUALS() { return getToken(AggregatorWtalParser.LESS_OR_EQUALS, 0); }
		public ComparatorsContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).enterComparators(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).exitComparators(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AggregatorWtalVisitor ) return ((AggregatorWtalVisitor<? extends T>)visitor).visitComparators(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class TernaryFunctionContext extends ExpressionContext {
		public TerminalNode TERNARY() { return getToken(AggregatorWtalParser.TERNARY, 0); }
		public TerminalNode LPAREN() { return getToken(AggregatorWtalParser.LPAREN, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(AggregatorWtalParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(AggregatorWtalParser.COMMA, i);
		}
		public TerminalNode RPAREN() { return getToken(AggregatorWtalParser.RPAREN, 0); }
		public TernaryFunctionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).enterTernaryFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).exitTernaryFunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AggregatorWtalVisitor ) return ((AggregatorWtalVisitor<? extends T>)visitor).visitTernaryFunction(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PowFunctionContext extends ExpressionContext {
		public TerminalNode POW() { return getToken(AggregatorWtalParser.POW, 0); }
		public TerminalNode LPAREN() { return getToken(AggregatorWtalParser.LPAREN, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode COMMA() { return getToken(AggregatorWtalParser.COMMA, 0); }
		public TerminalNode RPAREN() { return getToken(AggregatorWtalParser.RPAREN, 0); }
		public PowFunctionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).enterPowFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).exitPowFunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AggregatorWtalVisitor ) return ((AggregatorWtalVisitor<? extends T>)visitor).visitPowFunction(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AndOrContext extends ExpressionContext {
		public Token op;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode AND() { return getToken(AggregatorWtalParser.AND, 0); }
		public TerminalNode OR() { return getToken(AggregatorWtalParser.OR, 0); }
		public AndOrContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).enterAndOr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).exitAndOr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AggregatorWtalVisitor ) return ((AggregatorWtalVisitor<? extends T>)visitor).visitAndOr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 4;
		enterRecursionRule(_localctx, 4, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(83);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case POW:
				{
				_localctx = new PowFunctionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(36);
				match(POW);
				setState(37);
				match(LPAREN);
				setState(38);
				expression(0);
				setState(39);
				match(COMMA);
				setState(40);
				expression(0);
				setState(41);
				match(RPAREN);
				}
				break;
			case SQRT:
				{
				_localctx = new SqrtFunctionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(43);
				match(SQRT);
				setState(44);
				match(LPAREN);
				setState(45);
				expression(0);
				setState(46);
				match(RPAREN);
				}
				break;
			case MAP:
				{
				_localctx = new MapFunctionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(48);
				match(MAP);
				setState(49);
				match(LPAREN);
				setState(50);
				expression(0);
				setState(51);
				match(COMMA);
				setState(52);
				lambda_expression();
				setState(53);
				match(RPAREN);
				}
				break;
			case FILTER:
				{
				_localctx = new FilterFunctionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(55);
				match(FILTER);
				setState(56);
				match(LPAREN);
				setState(57);
				expression(0);
				setState(58);
				match(COMMA);
				setState(59);
				lambda_expression();
				setState(60);
				match(RPAREN);
				}
				break;
			case ISNULL:
				{
				_localctx = new IsNullFunctionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(62);
				match(ISNULL);
				setState(63);
				match(LPAREN);
				setState(64);
				expression(0);
				setState(65);
				match(RPAREN);
				}
				break;
			case TERNARY:
				{
				_localctx = new TernaryFunctionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(67);
				match(TERNARY);
				setState(68);
				match(LPAREN);
				setState(69);
				expression(0);
				setState(70);
				match(COMMA);
				setState(71);
				expression(0);
				setState(72);
				match(COMMA);
				setState(73);
				expression(0);
				setState(74);
				match(RPAREN);
				}
				break;
			case LPAREN:
				{
				_localctx = new ParenthesisContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(76);
				match(LPAREN);
				setState(77);
				expression(0);
				setState(78);
				match(RPAREN);
				}
				break;
			case MINUS:
				{
				_localctx = new UnaryMinusContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(80);
				match(MINUS);
				setState(81);
				expression(9);
				}
				break;
			case NUMBER:
			case LBRACKET:
			case LBRACE:
			case STRING:
			case BOOLEAN:
			case NULL:
			case IDENTIFIER:
				{
				_localctx = new AtomicContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(82);
				atom();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(114);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(112);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
					case 1:
						{
						_localctx = new MulDivContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(85);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(86);
						((MulDivContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==MULT || _la==DIV) ) {
							((MulDivContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(87);
						expression(8);
						}
						break;
					case 2:
						{
						_localctx = new PlusMinusContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(88);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(89);
						((PlusMinusContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==PLUS || _la==MINUS) ) {
							((PlusMinusContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(90);
						expression(7);
						}
						break;
					case 3:
						{
						_localctx = new AndOrContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(91);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(92);
						((AndOrContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==AND || _la==OR) ) {
							((AndOrContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(93);
						expression(6);
						}
						break;
					case 4:
						{
						_localctx = new ModContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(94);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(95);
						((ModContext)_localctx).op = match(MOD);
						setState(96);
						expression(5);
						}
						break;
					case 5:
						{
						_localctx = new ComparatorsContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(97);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(98);
						((ComparatorsContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 32256L) != 0)) ) {
							((ComparatorsContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(99);
						expression(4);
						}
						break;
					case 6:
						{
						_localctx = new InContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(100);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(101);
						((InContext)_localctx).op = match(IN);
						setState(102);
						expression(3);
						}
						break;
					case 7:
						{
						_localctx = new AccesorContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(103);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(104);
						match(LBRACKET);
						setState(105);
						expression(0);
						setState(108);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==COLON) {
							{
							setState(106);
							match(COLON);
							setState(107);
							expression(0);
							}
						}

						setState(110);
						match(RBRACKET);
						}
						break;
					}
					} 
				}
				setState(116);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AtomContext extends ParserRuleContext {
		public TerminalNode NUMBER() { return getToken(AggregatorWtalParser.NUMBER, 0); }
		public TerminalNode IDENTIFIER() { return getToken(AggregatorWtalParser.IDENTIFIER, 0); }
		public TerminalNode STRING() { return getToken(AggregatorWtalParser.STRING, 0); }
		public TerminalNode BOOLEAN() { return getToken(AggregatorWtalParser.BOOLEAN, 0); }
		public TerminalNode NULL() { return getToken(AggregatorWtalParser.NULL, 0); }
		public Json_objectContext json_object() {
			return getRuleContext(Json_objectContext.class,0);
		}
		public Json_arrayContext json_array() {
			return getRuleContext(Json_arrayContext.class,0);
		}
		public AtomContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atom; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).enterAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).exitAtom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AggregatorWtalVisitor ) return ((AggregatorWtalVisitor<? extends T>)visitor).visitAtom(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AtomContext atom() throws RecognitionException {
		AtomContext _localctx = new AtomContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_atom);
		try {
			setState(124);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NUMBER:
				enterOuterAlt(_localctx, 1);
				{
				setState(117);
				match(NUMBER);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				setState(118);
				match(IDENTIFIER);
				}
				break;
			case STRING:
				enterOuterAlt(_localctx, 3);
				{
				setState(119);
				match(STRING);
				}
				break;
			case BOOLEAN:
				enterOuterAlt(_localctx, 4);
				{
				setState(120);
				match(BOOLEAN);
				}
				break;
			case NULL:
				enterOuterAlt(_localctx, 5);
				{
				setState(121);
				match(NULL);
				}
				break;
			case LBRACE:
				enterOuterAlt(_localctx, 6);
				{
				setState(122);
				json_object();
				}
				break;
			case LBRACKET:
				enterOuterAlt(_localctx, 7);
				{
				setState(123);
				json_array();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Lambda_expressionContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(AggregatorWtalParser.IDENTIFIER, 0); }
		public TerminalNode ARROW() { return getToken(AggregatorWtalParser.ARROW, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public Lambda_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lambda_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).enterLambda_expression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).exitLambda_expression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AggregatorWtalVisitor ) return ((AggregatorWtalVisitor<? extends T>)visitor).visitLambda_expression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Lambda_expressionContext lambda_expression() throws RecognitionException {
		Lambda_expressionContext _localctx = new Lambda_expressionContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_lambda_expression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(126);
			match(IDENTIFIER);
			setState(127);
			match(ARROW);
			setState(128);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Json_objectContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(AggregatorWtalParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(AggregatorWtalParser.RBRACE, 0); }
		public List<Json_pairContext> json_pair() {
			return getRuleContexts(Json_pairContext.class);
		}
		public Json_pairContext json_pair(int i) {
			return getRuleContext(Json_pairContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(AggregatorWtalParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(AggregatorWtalParser.COMMA, i);
		}
		public Json_objectContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_json_object; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).enterJson_object(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).exitJson_object(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AggregatorWtalVisitor ) return ((AggregatorWtalVisitor<? extends T>)visitor).visitJson_object(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Json_objectContext json_object() throws RecognitionException {
		Json_objectContext _localctx = new Json_objectContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_json_object);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(130);
			match(LBRACE);
			setState(139);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STRING) {
				{
				setState(131);
				json_pair();
				setState(136);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(132);
					match(COMMA);
					setState(133);
					json_pair();
					}
					}
					setState(138);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(141);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Json_pairContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(AggregatorWtalParser.STRING, 0); }
		public TerminalNode COLON() { return getToken(AggregatorWtalParser.COLON, 0); }
		public Json_valueContext json_value() {
			return getRuleContext(Json_valueContext.class,0);
		}
		public Json_pairContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_json_pair; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).enterJson_pair(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).exitJson_pair(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AggregatorWtalVisitor ) return ((AggregatorWtalVisitor<? extends T>)visitor).visitJson_pair(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Json_pairContext json_pair() throws RecognitionException {
		Json_pairContext _localctx = new Json_pairContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_json_pair);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(143);
			match(STRING);
			setState(144);
			match(COLON);
			setState(145);
			json_value();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Json_arrayContext extends ParserRuleContext {
		public TerminalNode LBRACKET() { return getToken(AggregatorWtalParser.LBRACKET, 0); }
		public TerminalNode RBRACKET() { return getToken(AggregatorWtalParser.RBRACKET, 0); }
		public List<Json_valueContext> json_value() {
			return getRuleContexts(Json_valueContext.class);
		}
		public Json_valueContext json_value(int i) {
			return getRuleContext(Json_valueContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(AggregatorWtalParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(AggregatorWtalParser.COMMA, i);
		}
		public Json_arrayContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_json_array; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).enterJson_array(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).exitJson_array(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AggregatorWtalVisitor ) return ((AggregatorWtalVisitor<? extends T>)visitor).visitJson_array(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Json_arrayContext json_array() throws RecognitionException {
		Json_arrayContext _localctx = new Json_arrayContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_json_array);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(147);
			match(LBRACKET);
			setState(156);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 516083941376L) != 0)) {
				{
				setState(148);
				json_value();
				setState(153);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(149);
					match(COMMA);
					setState(150);
					json_value();
					}
					}
					setState(155);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(158);
			match(RBRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Json_valueContext extends ParserRuleContext {
		public AtomContext atom() {
			return getRuleContext(AtomContext.class,0);
		}
		public Json_objectContext json_object() {
			return getRuleContext(Json_objectContext.class,0);
		}
		public Json_arrayContext json_array() {
			return getRuleContext(Json_arrayContext.class,0);
		}
		public Json_valueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_json_value; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).enterJson_value(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AggregatorWtalListener ) ((AggregatorWtalListener)listener).exitJson_value(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AggregatorWtalVisitor ) return ((AggregatorWtalVisitor<? extends T>)visitor).visitJson_value(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Json_valueContext json_value() throws RecognitionException {
		Json_valueContext _localctx = new Json_valueContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_json_value);
		try {
			setState(163);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(160);
				atom();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(161);
				json_object();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(162);
				json_array();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 2:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 7);
		case 1:
			return precpred(_ctx, 6);
		case 2:
			return precpred(_ctx, 5);
		case 3:
			return precpred(_ctx, 4);
		case 4:
			return precpred(_ctx, 3);
		case 5:
			return precpred(_ctx, 2);
		case 6:
			return precpred(_ctx, 8);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001&\u00a6\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0001\u0000\u0004\u0000\u0014\b\u0000\u000b\u0000\f\u0000\u0015"+
		"\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0003\u0001\"\b\u0001"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0003\u0002T\b\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0003\u0002m\b\u0002\u0001\u0002\u0001\u0002\u0005\u0002"+
		"q\b\u0002\n\u0002\f\u0002t\t\u0002\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0003\u0003}\b\u0003"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0005\u0005\u0087\b\u0005\n\u0005\f\u0005\u008a"+
		"\t\u0005\u0003\u0005\u008c\b\u0005\u0001\u0005\u0001\u0005\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0005\u0007\u0098\b\u0007\n\u0007\f\u0007\u009b\t\u0007\u0003"+
		"\u0007\u009d\b\u0007\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\b\u0003"+
		"\b\u00a4\b\b\u0001\b\u0000\u0001\u0004\t\u0000\u0002\u0004\u0006\b\n\f"+
		"\u000e\u0010\u0000\u0004\u0001\u0000\u0003\u0004\u0001\u0000\u0001\u0002"+
		"\u0001\u0000\u0005\u0006\u0001\u0000\t\u000e\u00ba\u0000\u0013\u0001\u0000"+
		"\u0000\u0000\u0002!\u0001\u0000\u0000\u0000\u0004S\u0001\u0000\u0000\u0000"+
		"\u0006|\u0001\u0000\u0000\u0000\b~\u0001\u0000\u0000\u0000\n\u0082\u0001"+
		"\u0000\u0000\u0000\f\u008f\u0001\u0000\u0000\u0000\u000e\u0093\u0001\u0000"+
		"\u0000\u0000\u0010\u00a3\u0001\u0000\u0000\u0000\u0012\u0014\u0003\u0002"+
		"\u0001\u0000\u0013\u0012\u0001\u0000\u0000\u0000\u0014\u0015\u0001\u0000"+
		"\u0000\u0000\u0015\u0013\u0001\u0000\u0000\u0000\u0015\u0016\u0001\u0000"+
		"\u0000\u0000\u0016\u0017\u0001\u0000\u0000\u0000\u0017\u0018\u0005\u0000"+
		"\u0000\u0001\u0018\u0001\u0001\u0000\u0000\u0000\u0019\u001a\u0005&\u0000"+
		"\u0000\u001a\u001b\u0005\u0007\u0000\u0000\u001b\u001c\u0003\u0004\u0002"+
		"\u0000\u001c\u001d\u0005\u0011\u0000\u0000\u001d\"\u0001\u0000\u0000\u0000"+
		"\u001e\u001f\u0003\u0004\u0002\u0000\u001f \u0005\u0011\u0000\u0000 \""+
		"\u0001\u0000\u0000\u0000!\u0019\u0001\u0000\u0000\u0000!\u001e\u0001\u0000"+
		"\u0000\u0000\"\u0003\u0001\u0000\u0000\u0000#$\u0006\u0002\uffff\uffff"+
		"\u0000$%\u0005\u0012\u0000\u0000%&\u0005 \u0000\u0000&\'\u0003\u0004\u0002"+
		"\u0000\'(\u0005\u001a\u0000\u0000()\u0003\u0004\u0002\u0000)*\u0005!\u0000"+
		"\u0000*T\u0001\u0000\u0000\u0000+,\u0005\u0013\u0000\u0000,-\u0005 \u0000"+
		"\u0000-.\u0003\u0004\u0002\u0000./\u0005!\u0000\u0000/T\u0001\u0000\u0000"+
		"\u000001\u0005\u0014\u0000\u000012\u0005 \u0000\u000023\u0003\u0004\u0002"+
		"\u000034\u0005\u001a\u0000\u000045\u0003\b\u0004\u000056\u0005!\u0000"+
		"\u00006T\u0001\u0000\u0000\u000078\u0005\u0015\u0000\u000089\u0005 \u0000"+
		"\u00009:\u0003\u0004\u0002\u0000:;\u0005\u001a\u0000\u0000;<\u0003\b\u0004"+
		"\u0000<=\u0005!\u0000\u0000=T\u0001\u0000\u0000\u0000>?\u0005\u0016\u0000"+
		"\u0000?@\u0005 \u0000\u0000@A\u0003\u0004\u0002\u0000AB\u0005!\u0000\u0000"+
		"BT\u0001\u0000\u0000\u0000CD\u0005\u0017\u0000\u0000DE\u0005 \u0000\u0000"+
		"EF\u0003\u0004\u0002\u0000FG\u0005\u001a\u0000\u0000GH\u0003\u0004\u0002"+
		"\u0000HI\u0005\u001a\u0000\u0000IJ\u0003\u0004\u0002\u0000JK\u0005!\u0000"+
		"\u0000KT\u0001\u0000\u0000\u0000LM\u0005 \u0000\u0000MN\u0003\u0004\u0002"+
		"\u0000NO\u0005!\u0000\u0000OT\u0001\u0000\u0000\u0000PQ\u0005\u0002\u0000"+
		"\u0000QT\u0003\u0004\u0002\tRT\u0003\u0006\u0003\u0000S#\u0001\u0000\u0000"+
		"\u0000S+\u0001\u0000\u0000\u0000S0\u0001\u0000\u0000\u0000S7\u0001\u0000"+
		"\u0000\u0000S>\u0001\u0000\u0000\u0000SC\u0001\u0000\u0000\u0000SL\u0001"+
		"\u0000\u0000\u0000SP\u0001\u0000\u0000\u0000SR\u0001\u0000\u0000\u0000"+
		"Tr\u0001\u0000\u0000\u0000UV\n\u0007\u0000\u0000VW\u0007\u0000\u0000\u0000"+
		"Wq\u0003\u0004\u0002\bXY\n\u0006\u0000\u0000YZ\u0007\u0001\u0000\u0000"+
		"Zq\u0003\u0004\u0002\u0007[\\\n\u0005\u0000\u0000\\]\u0007\u0002\u0000"+
		"\u0000]q\u0003\u0004\u0002\u0006^_\n\u0004\u0000\u0000_`\u0005\u000f\u0000"+
		"\u0000`q\u0003\u0004\u0002\u0005ab\n\u0003\u0000\u0000bc\u0007\u0003\u0000"+
		"\u0000cq\u0003\u0004\u0002\u0004de\n\u0002\u0000\u0000ef\u0005\u0010\u0000"+
		"\u0000fq\u0003\u0004\u0002\u0003gh\n\b\u0000\u0000hi\u0005\u001b\u0000"+
		"\u0000il\u0003\u0004\u0002\u0000jk\u0005\u001f\u0000\u0000km\u0003\u0004"+
		"\u0002\u0000lj\u0001\u0000\u0000\u0000lm\u0001\u0000\u0000\u0000mn\u0001"+
		"\u0000\u0000\u0000no\u0005\u001c\u0000\u0000oq\u0001\u0000\u0000\u0000"+
		"pU\u0001\u0000\u0000\u0000pX\u0001\u0000\u0000\u0000p[\u0001\u0000\u0000"+
		"\u0000p^\u0001\u0000\u0000\u0000pa\u0001\u0000\u0000\u0000pd\u0001\u0000"+
		"\u0000\u0000pg\u0001\u0000\u0000\u0000qt\u0001\u0000\u0000\u0000rp\u0001"+
		"\u0000\u0000\u0000rs\u0001\u0000\u0000\u0000s\u0005\u0001\u0000\u0000"+
		"\u0000tr\u0001\u0000\u0000\u0000u}\u0005\u0018\u0000\u0000v}\u0005&\u0000"+
		"\u0000w}\u0005#\u0000\u0000x}\u0005$\u0000\u0000y}\u0005%\u0000\u0000"+
		"z}\u0003\n\u0005\u0000{}\u0003\u000e\u0007\u0000|u\u0001\u0000\u0000\u0000"+
		"|v\u0001\u0000\u0000\u0000|w\u0001\u0000\u0000\u0000|x\u0001\u0000\u0000"+
		"\u0000|y\u0001\u0000\u0000\u0000|z\u0001\u0000\u0000\u0000|{\u0001\u0000"+
		"\u0000\u0000}\u0007\u0001\u0000\u0000\u0000~\u007f\u0005&\u0000\u0000"+
		"\u007f\u0080\u0005\b\u0000\u0000\u0080\u0081\u0003\u0004\u0002\u0000\u0081"+
		"\t\u0001\u0000\u0000\u0000\u0082\u008b\u0005\u001d\u0000\u0000\u0083\u0088"+
		"\u0003\f\u0006\u0000\u0084\u0085\u0005\u001a\u0000\u0000\u0085\u0087\u0003"+
		"\f\u0006\u0000\u0086\u0084\u0001\u0000\u0000\u0000\u0087\u008a\u0001\u0000"+
		"\u0000\u0000\u0088\u0086\u0001\u0000\u0000\u0000\u0088\u0089\u0001\u0000"+
		"\u0000\u0000\u0089\u008c\u0001\u0000\u0000\u0000\u008a\u0088\u0001\u0000"+
		"\u0000\u0000\u008b\u0083\u0001\u0000\u0000\u0000\u008b\u008c\u0001\u0000"+
		"\u0000\u0000\u008c\u008d\u0001\u0000\u0000\u0000\u008d\u008e\u0005\u001e"+
		"\u0000\u0000\u008e\u000b\u0001\u0000\u0000\u0000\u008f\u0090\u0005#\u0000"+
		"\u0000\u0090\u0091\u0005\u001f\u0000\u0000\u0091\u0092\u0003\u0010\b\u0000"+
		"\u0092\r\u0001\u0000\u0000\u0000\u0093\u009c\u0005\u001b\u0000\u0000\u0094"+
		"\u0099\u0003\u0010\b\u0000\u0095\u0096\u0005\u001a\u0000\u0000\u0096\u0098"+
		"\u0003\u0010\b\u0000\u0097\u0095\u0001\u0000\u0000\u0000\u0098\u009b\u0001"+
		"\u0000\u0000\u0000\u0099\u0097\u0001\u0000\u0000\u0000\u0099\u009a\u0001"+
		"\u0000\u0000\u0000\u009a\u009d\u0001\u0000\u0000\u0000\u009b\u0099\u0001"+
		"\u0000\u0000\u0000\u009c\u0094\u0001\u0000\u0000\u0000\u009c\u009d\u0001"+
		"\u0000\u0000\u0000\u009d\u009e\u0001\u0000\u0000\u0000\u009e\u009f\u0005"+
		"\u001c\u0000\u0000\u009f\u000f\u0001\u0000\u0000\u0000\u00a0\u00a4\u0003"+
		"\u0006\u0003\u0000\u00a1\u00a4\u0003\n\u0005\u0000\u00a2\u00a4\u0003\u000e"+
		"\u0007\u0000\u00a3\u00a0\u0001\u0000\u0000\u0000\u00a3\u00a1\u0001\u0000"+
		"\u0000\u0000\u00a3\u00a2\u0001\u0000\u0000\u0000\u00a4\u0011\u0001\u0000"+
		"\u0000\u0000\f\u0015!Slpr|\u0088\u008b\u0099\u009c\u00a3";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}