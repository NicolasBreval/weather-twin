package org.nbreval.weather_twin.gateway.application.port.in;

import java.util.function.Function;

/**
 * Defines the entity which performs all operations required for the WTAL
 * evaluator.
 * 
 * It contains all methods required by WTAL evaluator to interpret an
 * aggregation
 * or flush expression and obtain the result as a Java object.
 */
public interface WTALLogicPort {

  /**
   * Performs an add operation between two objects.
   * 
   * @param left  Left addend.
   * @param right Right addend.
   * @return The result of add both objects.
   */
  Object applyAdd(Object left, Object right);

  /**
   * Performs a substract operation between two objects.
   * 
   * @param left  Minuend.
   * @param right Subtrahend.
   * @return The result of substract both objects.
   */
  Object applySubstract(Object left, Object right);

  /**
   * Performs a product between two objects.
   * 
   * @param left  Multiplicand.
   * @param right Multiplier.
   * @return The result of multiply both objects.
   */
  Object applyProduct(Object left, Object right);

  /**
   * Performs a division between two objects.
   * 
   * @param left  Dividend.
   * @param right Divisor.
   * @return The result of divide both objects.
   */
  Object applyDivision(Object left, Object right);

  /**
   * Performs an 'and' operation between two objects.
   * 
   * @param left  Left operand.
   * @param right Right operand.
   * @return The result of apply the 'and' operation between two objects.
   */
  Object applyAnd(Object left, Object right);

  /**
   * Performs an 'or' operation between two objects.
   * 
   * @param left  Left operand.
   * @param right Right operand.
   * @return The result of apply the 'or' operation between two objects.
   */
  Object applyOr(Object left, Object right);

  /**
   * Performs the 'greater than' operation between two objects.
   * 
   * @param left  Left operand.
   * @param right Right operand.
   * @return The result of apply 'greater than' operation between two objects.
   */
  Object applyGreaterThan(Object left, Object right);

  /**
   * Performs the 'less than' operation between two objects.
   * 
   * @param left  Left operand.
   * @param right Right operand.
   * @return The result of apply 'less than' operation between two objects.
   */
  Object applyLessThan(Object left, Object right);

  /**
   * Performs the 'greater than or equals' operation between two objects.
   * 
   * @param left  Left operand.
   * @param right Right operand.
   * @return The result of apply 'greater than or equals' operation between two
   *         objects.
   */
  Object applyGreaterThanOrEquals(Object left, Object right);

  /**
   * Performs the 'less than or equals' operation between two objects.
   * 
   * @param left  Left operand.
   * @param right Right operand.
   * @return The result of apply 'less than or equals' operation between two
   *         objects.
   */
  Object applyLessThanOrEquals(Object left, Object right);

  /**
   * Performs the 'equals' operation between two objects.
   * 
   * @param left  Left operand.
   * @param right Right operand.
   * @return The result of apply 'equals' operation between two objects.
   */
  Object applyEquals(Object left, Object right);

  /**
   * Performs the negation of 'equals' operation between two objects.
   * 
   * @param left  Left operand.
   * @param right Right operand.
   * @return The result of apply the negation of 'equals' operation between two
   *         objects.
   */
  Object applyNotEquals(Object left, Object right);

  /**
   * Performs a negation of an object.
   * 
   * @param value Object to negate.
   * @return The result of apply a negation in an object.
   */
  Object applyNegative(Object value);

  /**
   * Performs a modulo operation between two objects.
   * 
   * @param left  Left operand.
   * @param right Right operand.
   * @return The result of apply modulo between two objects.
   */
  Object applyModulo(Object left, Object right);

  /**
   * Performs a indexed access in an object.
   * 
   * @param iterable Iterable object to apply indexed accessor.
   * @param from     Start position of accessor.
   * @param to       End position of accessor.
   * @return A subset of iterable object, or a single value.
   */
  Object applyNumericAccessor(Object iterable, int from, Integer to);

  /**
   * Performs the 'in' operation, to check if an value is present an iterable
   * object.
   * 
   * @param iterable Iterable object to apply operation.
   * @param item     Item to find inside iterable object.
   * @return True if item is inside iterable object, else false.
   */
  boolean applyIn(Object iterable, Object item);

  /**
   * Performs an acces operation by key.
   * 
   * @param groupable Object to which to perform the access operation.
   * @param key       The key to find inside object.
   * @return The value related to key, or null if it doesn't exist.
   */
  Object applyKeyAccessor(Object groupable, String key);

  /**
   * Performs a filter function over an iterable object.
   * 
   * @param iterable Object to which to perform filter function.
   * @param lambda   Function to execute over each iterm on iterable object.
   * @return A new iterable object, with only items which passes the filter
   *         function.
   */
  Object applyFilterFunction(Object iterable, Function<Object, Object> lambda);

  /**
   * Performs a map function over an iterable object.
   * 
   * @param iterable Object to which to perform map function.
   * @param lambda   Function to execute over each iterm on iterable object.
   * @return A new iterable object, with all items modified by map function.
   */
  Object applyMapFunction(Object iterable, Function<Object, Object> lambda);

  /**
   * Performs a methematical pow function.
   * 
   * @param base     Base value.
   * @param exponent Exponent value.
   * @return The result of apply pow function.
   */
  Object applyPow(Object base, Object exponent);

  /**
   * Performs a mathematical sqrt function.
   * 
   * @param num Number to which to apply function.
   * @return The result of apply sqrt function.
   */
  Object applySqrt(Object num);

  /**
   * Checks if an object is null.
   * 
   * @param any Object to check.
   * @return True if object is null, else false.
   */
  boolean applyIsNull(Object any);

  /**
   * Apply a tertiary operator.
   * 
   * @param toCheck Object to check if is true or false.
   * @param onOk    Result if object to check is true.
   * @param onNok   Result if object to check is false.
   * @return If toCheck is true, returns onOk, else returns onNok.
   */
  Object applyTertiary(Object toCheck, Object onOk, Object onNok);
}
