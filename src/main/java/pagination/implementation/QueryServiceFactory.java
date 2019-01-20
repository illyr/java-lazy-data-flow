package pagination.implementation;

import pagination.api.QueryService;

public enum QueryServiceFactory {
    ;

    public static QueryService newQueryService() {
        return new FakeQueryService();
    }
}
