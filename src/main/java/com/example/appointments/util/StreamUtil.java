package com.example.appointments.util;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamUtil {
    public static <T> Stream<T> stream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    public static <T, K, R> List<R> groupBy(Collection<T> collection, Function<T, K> keyFn, BiFunction<K, List<T>, R> keyGroupMapFn) {
        return collection.stream()
                .collect(Collectors.groupingBy(keyFn, LinkedHashMap::new, Collectors.toList()))
                .entrySet()
                .stream()
                .map(entry -> keyGroupMapFn.apply(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
