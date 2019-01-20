package pagination;

import pagination.api.QueryService;
import pagination.api.data.QueryResult;
import pagination.implementation.QueryServiceFactory;

import java.util.function.Supplier;

public class Test {

    public static void main(String[] args) {

        testReactorVersion();

        testStreamVersion();

        testLazySeqVersion();

    }

    private static void testStreamVersion() {
        System.err.println("Testing Stream version:");

        StreamVersion.queryAllResults(newQueryExecutor())
                .forEach(System.out::println);
    }

    private static void testLazySeqVersion() {
        System.err.println("Testing LazySeq version:");

        LazySeqVersion.queryAllResults(newQueryExecutor())
                .forEach(System.out::println);
    }

    private static void testReactorVersion() {
        System.err.println("Testing Reactor version:");

        ReactorVersion.queryAllResults(newQueryExecutor())
                .subscribe(System.out::println);
    }

    private static Supplier<QueryResult<String>> newQueryExecutor() {
        final QueryService queryService = QueryServiceFactory.newQueryService();
        return queryService::doSingleQuery;
    }
}
