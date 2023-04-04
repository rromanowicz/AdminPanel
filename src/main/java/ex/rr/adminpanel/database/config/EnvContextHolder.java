package ex.rr.adminpanel.database.config;

import ex.rr.adminpanel.enums.Env;
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

    public static void clearEnvContext() {
        env = null;
    }
}
