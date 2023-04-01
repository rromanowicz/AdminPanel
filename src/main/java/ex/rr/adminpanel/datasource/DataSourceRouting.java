package ex.rr.adminpanel.datasource;

import ex.rr.adminpanel.enums.Env;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class DataSourceRouting extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return EnvContextHolder.getEnvContext();
    }

    public void initDatasource(DataSource localDataSource,
                               DataSource devDataSource,
                               DataSource sitDataSource,
                               DataSource satDataSource,
                               DataSource prodDataSource) {
        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put(Env.DEV, devDataSource);
        dataSourceMap.put(Env.SIT, sitDataSource);
        dataSourceMap.put(Env.SAT, satDataSource);
        dataSourceMap.put(Env.PROD, prodDataSource);
        dataSourceMap.put(Env.LOCAL, localDataSource);
        this.setTargetDataSources(dataSourceMap);
        EnvContextHolder.setEnvContext(Env.LOCAL);
        this.setDefaultTargetDataSource(devDataSource);
    }
}