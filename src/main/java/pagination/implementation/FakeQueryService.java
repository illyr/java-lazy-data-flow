package pagination.implementation;

import pagination.api.QueryService;
import pagination.api.data.QueryResult;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static pagination.api.data.ImmutableQueryResult.of;


public final class FakeQueryService implements QueryService {

    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger();

    private static LongAdder longAdder = new LongAdder();

    @Override
    public QueryResult<String> doSingleQuery() {
        // do query
        System.out.println("Do query taking 1 second");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        longAdder.increment();
        if (longAdder.intValue() == 5) {
            try {
                return of(buildRandomNames(), false);
            } finally {
                ATOMIC_INTEGER.set(0);
                longAdder.reset();
            }
        }

        return of(buildRandomNames(), true);
    }

    private static List<String> buildRandomNames() {
        return IntStream.range(0, 2)
                .boxed()
                .map(i -> ATOMIC_INTEGER.incrementAndGet() + "")
                .collect(Collectors.toList());
    }
}
