package ex.rr.adminpanel.database.config;

import ex.rr.adminpanel.enums.Env;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * The {@code DataSourceRouting} class represents the DataSource Environment Router. Only the currently selected datasource will be used to execute db instructions.
 *
 * @author  rromanowicz
 * @see     Env
 */
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
        this.afterPropertiesSet();
    }
}