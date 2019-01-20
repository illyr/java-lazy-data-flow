package pagination;

import com.nurkiewicz.lazyseq.LazySeq;
import pagination.api.data.QueryResult;

import java.util.function.Supplier;
import java.util.stream.Stream;

public enum LazySeqVersion {
    ;

    public static <T> Stream<T> queryAllResults(final Supplier<QueryResult<T>> queryExecutor) {
        return chainLazily(queryExecutor)
                .stream()
                .flatMap(r -> r.results().stream());
    }

    public static <T> LazySeq<QueryResult<T>> chainLazily(final Supplier<QueryResult<T>> queryExecutor) {
        final QueryResult<T> result = queryExecutor.get();
        return LazySeq.concat(
                LazySeq.of(result),
                () -> result.hasPotentiallyOtherResults() ? chainLazily(queryExecutor): LazySeq.of()
        );
    }
}
