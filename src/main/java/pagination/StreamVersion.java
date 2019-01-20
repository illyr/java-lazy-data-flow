package pagination;

import pagination.api.data.QueryResult;

import java.util.*;
import java.util.Spliterators.AbstractSpliterator;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.lang.Long.MAX_VALUE;
import static java.util.Spliterator.NONNULL;
import static java.util.stream.Stream.concat;
import static java.util.stream.StreamSupport.stream;
import static pagination.api.data.QueryResult.emptyHavingNext;

public enum StreamVersion {

    ;

    public static <T> Stream<T> queryAllResults(final Supplier<QueryResult<T>> queryExecutor) {
        return chainLazily(emptyHavingNext(), queryExecutor)
                .filter(v -> !v.results().isEmpty())
                .flatMap(r -> r.results().stream());
    }

    private static <T> Stream<QueryResult<T>> chainLazily(final QueryResult<T> r,
                                                          final Supplier<QueryResult<T>> queryExecutor) {

        final Stream<QueryResult<T>> first = Stream.of(r);

        final Stream<QueryResult<T>> nextResults = stream(
            new AbstractSpliterator<QueryResult<T>>(MAX_VALUE, NONNULL) {

                private final Supplier<Iterator<QueryResult<T>>> queryIterator =
                        () -> toIterator(chainLazily(queryExecutor.get(), queryExecutor));

                @Override
                public boolean tryAdvance(final Consumer<? super QueryResult<T>> action) {
                    final QueryResult<T> nextResult = queryIterator.get().next();
                    action.accept(nextResult);
                    return nextResult.hasPotentiallyOtherResults();
                }
            },
            false
        );
        return concat(first, nextResults);
    }

    private static <T> Iterator<T> toIterator(final Stream<T> stream) {
        return Spliterators.iterator(stream.spliterator());
    }
}
