package pagination.api.data;

import com.google.common.collect.ImmutableList;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
public interface QueryResult<T> {

    @Value.Parameter
    List<T> results();

    @Value.Parameter
    boolean hasPotentiallyOtherResults();

    static <T> QueryResult<T> emptyHavingNext() {
        return ImmutableQueryResult.of(ImmutableList.of(), true);
    }
}
