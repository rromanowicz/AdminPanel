package ex.rr.adminpanel.data.database.config;

import ex.rr.adminpanel.data.enums.Env;
import ex.rr.adminpanel.ui.utils.Utils;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The {@code DataSourceRouting} class represents the DataSource Environment Router. Only the currently selected datasource will be used to execute db instructions.
 *
 * @author rromanowicz
 * @see Env
 */
public class DataSourceRouting extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return Objects.requireNonNull(Utils.getUserSession()).getEnv();
    }

    /**
     * Initializes data connections available in Router
     *
     * @param defaultEnv        Default environment. Selected on initialization.
     * @param localDataSource
     * @param devDataSource
     * @param sitDataSource
     * @param satDataSource
     * @param prodDataSource
     *
     * @see AbstractRoutingDataSource
     */
    public void initDatasource(Env defaultEnv,
                               DataSource localDataSource,
                               DataSource devDataSource,
                               DataSource sitDataSource,
                               DataSource satDataSource,
                               DataSource prodDataSource) {
        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put(Env.LOCAL, localDataSource);
        dataSourceMap.put(Env.DEV, devDataSource);
        dataSourceMap.put(Env.SIT, sitDataSource);
        dataSourceMap.put(Env.SAT, satDataSource);
        dataSourceMap.put(Env.PROD, prodDataSource);
        this.setTargetDataSources(dataSourceMap);
        this.setDefaultTargetDataSource(dataSourceMap.get(defaultEnv));
        this.afterPropertiesSet();
    }

}