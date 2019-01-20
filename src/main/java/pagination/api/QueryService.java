package pagination.api;

import pagination.api.data.QueryResult;

public interface QueryService {

    QueryResult<String> doSingleQuery();
}
