package ex.rr.adminpanel.datasource;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DataSourceRouting extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return EnvContextHolder.getEnvContext();
    }

    public void initDatasource(DataSource localDataSource,
                               DataSource devDataSource) {
        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put(Env.DEV, devDataSource);
        dataSourceMap.put(Env.LOCAL, localDataSource);
        this.setTargetDataSources(dataSourceMap);
        // Here we have to provide default datasource details.
        EnvContextHolder.setEnvContext(Env.DEV);
        this.setDefaultTargetDataSource(devDataSource);
    }
}