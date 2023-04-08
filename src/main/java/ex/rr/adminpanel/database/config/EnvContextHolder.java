package ex.rr.adminpanel.database.config;

import ex.rr.adminpanel.enums.ActionType;
import ex.rr.adminpanel.enums.Env;
import ex.rr.adminpanel.enums.InputType;
import org.springframework.stereotype.Component;

/**
 * The {@code EnvContextHolder} class represents container to store the Environment property value.
 *
 * @author  rromanowicz
 */
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
