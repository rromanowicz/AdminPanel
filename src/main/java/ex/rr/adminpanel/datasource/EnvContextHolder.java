package ex.rr.adminpanel.datasource;

import org.springframework.stereotype.Component;

@Component
public class EnvContextHolder {
    private static Env env;

    public static void setEnvContext(Env envss) {
        env=envss;
    }

    public static Env getEnvContext() {
        return env;
    }

    public static void clearBranchContext() {
        env = null;
    }
}
