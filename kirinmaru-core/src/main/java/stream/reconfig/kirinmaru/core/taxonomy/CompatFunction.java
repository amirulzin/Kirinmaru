package stream.reconfig.kirinmaru.core.taxonomy;

import java.util.ArrayList;
import java.util.List;

/**
 * Basic map function analogous to Java 8/RxJava map, with added concrete transform method for collections
 */

@SuppressWarnings("WeakerAccess")
public abstract class CompatFunction<T, R> {
  public static <T, R> List<R> transformFrom(List<T> collections, CompatFunction<T, R> function) {
    List<R> result = new ArrayList<>(collections.size());
    for (T t : collections) {
      result.add(function.map(t));
    }
    return result;
  }

  public abstract R map(T t);
}
