package org.nbreval.weather_twin.gateway.computing.domain.model;

import java.io.Serializable;
import java.util.function.Function;

/**
 * Custom class used to represent a tuple of three elements. It's an immutable
 * object, so none attribute can be modified after the object creation.
 */
public class Triple<L extends Serializable, M extends Serializable, R extends Serializable> implements Serializable {
  private final L left;
  private final M middle;
  private final R right;

  Triple(L left, M middle, R right) {
    this.left = left;
    this.middle = middle;
    this.right = right;
  }

  public L getLeft() {
    return left;
  }

  public M getMiddle() {
    return middle;
  }

  public R getRight() {
    return right;
  }

  /**
   * Creates a new Triple object based on another one, modifying each element of
   * it using a mapping function
   * 
   * @param lMapper Mapping function for left element
   * @param mMapper Mapping function for middle element
   * @param rMapper Mapping function for right element
   * @return The new Triple object based on current
   */
  public Triple<L, M, R> set(Function<L, L> lMapper, Function<M, M> mMapper, Function<R, R> rMapper) {
    return new Triple<L, M, R>(lMapper.apply(left), mMapper.apply(middle), rMapper.apply(right));
  }

  /**
   * Creates a new Triple object using parameters as values
   * 
   * @param <L>    Type for left element
   * @param <M>    Type for middle element
   * @param <R>    Type for right element
   * @param left   Left element
   * @param middle Middle element
   * @param right  Right element
   * @return
   */
  public static <L extends Serializable, M extends Serializable, R extends Serializable> Triple<L, M, R> of(L left,
      M middle, R right) {
    return new Triple<L, M, R>(left, middle, right);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((left == null) ? 0 : left.hashCode());
    result = prime * result + ((middle == null) ? 0 : middle.hashCode());
    result = prime * result + ((right == null) ? 0 : right.hashCode());
    return result;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Triple other = (Triple) obj;
    if (left == null) {
      if (other.left != null)
        return false;
    } else if (!left.equals(other.left))
      return false;
    if (middle == null) {
      if (other.middle != null)
        return false;
    } else if (!middle.equals(other.middle))
      return false;
    if (right == null) {
      if (other.right != null)
        return false;
    } else if (!right.equals(other.right))
      return false;
    return true;
  }

}
