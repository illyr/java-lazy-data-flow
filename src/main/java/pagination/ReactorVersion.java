package pagination;

import pagination.api.data.QueryResult;
import reactor.core.publisher.Flux;
import java.util.function.Supplier;

import static java.util.stream.Stream.of;
import static reactor.core.publisher.Flux.*;

public enum ReactorVersion {
    ;

    public static <T> Flux<T> queryAllResults(final Supplier<QueryResult<T>> queryExecutor) {
        return chainLazily(queryExecutor)
                .flatMap(re -> fromIterable(re.results()));
    }

    public static <T> Flux<QueryResult<T>> chainLazily(final Supplier<QueryResult<T>> queryExecutor) {
        return fromStream(() -> of(queryExecutor.get()))
                .flatMap(r -> {
                    if(r.hasPotentiallyOtherResults()) {
                        return concat(just(r),  chainLazily(queryExecutor));
                    }
                    return just(r);
                });
    }
}
