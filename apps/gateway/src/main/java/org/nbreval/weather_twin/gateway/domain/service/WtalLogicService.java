package org.nbreval.weather_twin.gateway.domain.service;

import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;

import org.nbreval.weather_twin.gateway.application.port.in.WTALLogicPort;
import org.nbreval.weather_twin.gateway.domain.entity.Json;
import org.nbreval.weather_twin.gateway.domain.entity.JsonArray;
import org.nbreval.weather_twin.gateway.domain.entity.JsonValue;
import org.nbreval.weather_twin.gateway.infrastructure.exception.WtalIllegalOperationException;

/**
 * Implements a {@link WTALLogicPort}.
 * 
 * Most of operations have raw objects as inputs, because this adapter has two
 * responsibilities, first, check if input types are correct for the operation
 * to apply and apply the logic related to the operation.
 */
public class WtalLogicService implements WTALLogicPort {

  @Override
  public Object applyAdd(Object left, Object right) {
    if (left instanceof Integer li) {
      if (right instanceof Integer ri) {
        return li + ri;
      } else if (right instanceof Float rf) {
        return li + rf;
      }
    } else if (left instanceof Float lf) {
      if (right instanceof Integer ri) {
        return lf + ri;
      } else if (right instanceof Float rf) {
        return lf + rf;
      }
    } else if (left instanceof String ls) {
      return ls + right.toString();
    } else if (left instanceof JsonArray la) {
      return la.add(new JsonValue<>(right));
    }

    throw new IllegalArgumentException("Illegal add operation for types '%s' and '%s'"
        .formatted(left.getClass().getSimpleName(), right.getClass().getSimpleName()));
  }

  @Override
  public Object applySubstract(Object left, Object right) {
    if (left instanceof Integer li) {
      if (right instanceof Integer ri) {
        return li - ri;
      } else if (right instanceof Float rf) {
        return li - rf;
      }
    } else if (left instanceof Float lf) {
      if (right instanceof Integer ri) {
        return lf - ri;
      }
      if (right instanceof Float rf) {
        return lf - rf;
      }
    }

    throw new IllegalArgumentException("Illegal substract operation for types '%s' and '%s'"
        .formatted(left.getClass().getSimpleName(), right.getClass().getSimpleName()));
  }

  @Override
  public Object applyProduct(Object left, Object right) {
    if (left instanceof Integer li) {
      if (right instanceof Integer ri) {
        return li * ri;
      } else if (right instanceof Float rf) {
        return li * rf;
      }
    } else if (left instanceof Float lf) {
      if (right instanceof Integer ri) {
        return lf * ri;
      } else if (right instanceof Float rf) {
        return lf * rf;
      }
    } else if (left instanceof String s && right instanceof Integer i) {
      return s.repeat(i);
    }

    throw new IllegalArgumentException("Illegal product operation for types '%s' and '%s'"
        .formatted(left.getClass().getSimpleName(), right.getClass().getSimpleName()));
  }

  @Override
  public Object applyDivision(Object left, Object right) {
    if (right instanceof Number n && n.intValue() == 0)
      throw new IllegalArgumentException("Illegal division operation by zero");

    if (left instanceof Integer li) {
      if (right instanceof Integer ri) {
        return li / ri;
      } else if (right instanceof Float rf) {
        return li / rf;
      }
    } else if (left instanceof Float lf) {
      if (right instanceof Integer ri) {
        return lf / ri;
      } else if (right instanceof Float rf) {
        return lf / rf;
      }
    }

    throw new IllegalArgumentException("Illegal division operation for types '%s' and '%s'"
        .formatted(left.getClass().getSimpleName(), right.getClass().getSimpleName()));
  }

  @Override
  public Object applyAnd(Object left, Object right) {
    if (left instanceof Boolean lb && right instanceof Boolean rb) {
      return lb && rb;
    } else if (left instanceof JsonArray la && right instanceof JsonArray ra) {
      return la.concat(ra);
    } else if (left instanceof Json lj && right instanceof Json rj) {
      return lj.concat(rj);
    }

    throw new IllegalArgumentException("Illegal and operation for types '%s' and '%s'"
        .formatted(left.getClass().getSimpleName(), right.getClass().getSimpleName()));
  }

  @Override
  public Object applyOr(Object left, Object right) {
    if (left instanceof Boolean lb && right instanceof Boolean rb)
      return lb || rb;

    throw new IllegalArgumentException("Illegal or operation for types '%s' and '%s'"
        .formatted(left.getClass().getSimpleName(), right.getClass().getSimpleName()));
  }

  @Override
  public Object applyGreaterThan(Object left, Object right) {
    if (left instanceof Integer li) {
      if (right instanceof Integer ri) {
        return li > ri;
      }
      if (right instanceof Float rf) {
        return li > rf;
      }
    } else if (left instanceof Float lf) {
      if (right instanceof Integer ri) {
        return lf > ri;
      } else if (right instanceof Float rf) {
        return lf > rf;
      }
    }

    throw new IllegalArgumentException("Illegal gt operation for types '%s' and '%s'"
        .formatted(left.getClass().getSimpleName(), right.getClass().getSimpleName()));
  }

  @Override
  public Object applyLessThan(Object left, Object right) {
    if (left instanceof Integer li) {
      if (right instanceof Integer ri) {
        return li < ri;
      }
      if (right instanceof Float rf) {
        return li < rf;
      }
    } else if (left instanceof Float lf) {
      if (right instanceof Integer ri) {
        return lf < ri;
      } else if (right instanceof Float rf) {
        return lf < rf;
      }
    }

    throw new IllegalArgumentException("Illegal lt operation for types '%s' and '%s'"
        .formatted(left.getClass().getSimpleName(), right.getClass().getSimpleName()));
  }

  @Override
  public Object applyGreaterThanOrEquals(Object left, Object right) {
    if (left instanceof Integer li) {
      if (right instanceof Integer ri) {
        return li >= ri;
      }
      if (right instanceof Float rf) {
        return li >= rf;
      }
    } else if (left instanceof Float lf) {
      if (right instanceof Integer ri) {
        return lf >= ri;
      } else if (right instanceof Float rf) {
        return lf >= rf;
      }
    }

    throw new IllegalArgumentException("Illegal gte operation for types '%s' and '%s'"
        .formatted(left.getClass().getSimpleName(), right.getClass().getSimpleName()));
  }

  @Override
  public Object applyLessThanOrEquals(Object left, Object right) {
    if (left instanceof Integer li) {
      if (right instanceof Integer ri) {
        return li <= ri;
      }
      if (right instanceof Float rf) {
        return li <= rf;
      }
    } else if (left instanceof Float lf) {
      if (right instanceof Integer ri) {
        return lf <= ri;
      } else if (right instanceof Float rf) {
        return lf <= rf;
      }
    }

    throw new IllegalArgumentException("Illegal lte operation for types '%s' and '%s'"
        .formatted(left.getClass().getSimpleName(), right.getClass().getSimpleName()));
  }

  @Override
  public Object applyEquals(Object left, Object right) {
    return left.equals(right);
  }

  @Override
  public Object applyNotEquals(Object left, Object right) {
    return !left.equals(right);
  }

  @Override
  public Object applyNegative(Object value) {
    if (value instanceof Integer i) {
      return i * -1;
    } else if (value instanceof Float f) {
      return f * -1;
    }

    throw new IllegalArgumentException("Illegal negate operation for type '%s'"
        .formatted(value.getClass().getSimpleName()));
  }

  @Override
  public Object applyModulo(Object left, Object right) {
    if (left instanceof Integer li) {
      if (right instanceof Integer ri) {
        return li % ri;
      } else if (right instanceof Float rf) {
        return li % rf;
      }
    } else if (left instanceof Float lf) {
      if (right instanceof Integer ri) {
        return lf % ri;
      } else if (right instanceof Float rf) {
        return lf % rf;
      }
    }

    throw new IllegalArgumentException("Illegal lte operation for types '%s' and '%s'"
        .formatted(left.getClass().getSimpleName(), right.getClass().getSimpleName()));
  }

  @Override
  public Object applyNumericAccessor(Object iterable, int from, Integer to) {
    if (iterable instanceof JsonArray array) {
      if (to == null) {
        return array.get(from);
      } else {
        return array.get(from, to);
      }
    } else if (iterable instanceof String s) {
      var fixedFrom = from < 0 ? s.length() + from : from;

      if (to == null) {
        return fixedFrom > s.length() ? null : s.substring(fixedFrom, fixedFrom + 1);
      } else {
        var fixedTo = to < 0 ? s.length() + to : to;
        return s.substring(Math.max(0, fixedFrom), Math.min(s.length(), fixedTo));
      }
    }

    throw new IllegalArgumentException("Illegal numeric access operation for type '%s'"
        .formatted(iterable.getClass().getSimpleName()));
  }

  @Override
  public boolean applyIn(Object iterable, Object item) {
    if (iterable instanceof JsonArray a) {
      return a.contains(item);
    } else if (iterable instanceof String s && item instanceof String i) {
      return s.contains(i) || Pattern.compile(i).matcher(s).find();
    } else if (iterable instanceof Json j && item instanceof String key) {
      return j.contains(key);
    }

    throw new IllegalArgumentException("Illegal in operation for type '%s'"
        .formatted(iterable.getClass().getSimpleName()));
  }

  @Override
  public Object applyKeyAccessor(Object groupable, String key) {
    if (groupable instanceof Json j) {
      return j.get(key);
    }

    throw new IllegalArgumentException("Illegal key access operation for type '%s'"
        .formatted(groupable.getClass().getSimpleName()));
  }

  @Override
  public Object applyFilterFunction(Object iterable, Function<Object, Object> lambda) {
    if (iterable instanceof JsonArray a) {
      return a.filter(lambda);
    }

    throw new IllegalArgumentException("Illegal filter operation for type '%s'"
        .formatted(iterable.getClass().getSimpleName()));
  }

  @Override
  public Object applyMapFunction(Object iterable, Function<Object, Object> lambda) {
    if (iterable instanceof JsonArray a) {
      return a.map(lambda);
    }

    throw new IllegalArgumentException("Illegal map operation for type '%s'"
        .formatted(iterable.getClass().getSimpleName()));
  }

  @Override
  public Object applyPow(Object base, Object exponent) {

    if (base instanceof Number b && exponent instanceof Number e) {
      return Math.pow(b.doubleValue(), e.doubleValue());
    }

    throw new IllegalArgumentException("Illegal pow operation for type '%s'"
        .formatted(base.getClass().getSimpleName()));
  }

  @Override
  public Object applySqrt(Object num) {
    if (num instanceof Number n) {
      return Math.sqrt(n.doubleValue());
    }

    throw new IllegalArgumentException("Illegal num operation for type '%s'"
        .formatted(num.getClass().getSimpleName()));
  }

  @Override
  public boolean applyIsNull(Object any) {
    return Objects.isNull(any);
  }

  @Override
  public Object applyTertiary(Object toCheck, Object onOk, Object onNok) {
    if (toCheck instanceof Boolean b) {
      return b ? onOk : onNok;
    }

    throw new WtalIllegalOperationException(
        "Invalid tertiary operation for value of type '%s'".formatted(toCheck.getClass().getSimpleName()));
  }

}
