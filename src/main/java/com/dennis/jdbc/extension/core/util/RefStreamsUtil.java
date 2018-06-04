package com.dennis.jdbc.extension.core.util;

import java8.util.stream.RefStreams;
import java8.util.stream.Stream;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RefStreamsUtil {
    public static <T> Stream<T> createStream(List<T> list) {
        Stream.Builder<T> builder = RefStreams.builder();
        for (T item : list)
            builder.add(item);
        return builder.build();
    }

    public static <T> Stream<T> createStream(T[] list) {
        Stream.Builder<T> builder = RefStreams.builder();
        for (T item : list)
            builder.add(item);
        return builder.build();
    }

    public static <T> Stream<T> createStream(Set<T> set) {
        Stream.Builder<T> builder = RefStreams.builder();
        for (T item : set)
            builder.add(item);
        return builder.build();
    }
}
