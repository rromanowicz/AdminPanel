package ex.rr.adminpanel.ui.handler;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.server.ErrorEvent;
import com.vaadin.flow.server.ErrorHandler;
import ex.rr.adminpanel.ui.utils.Utils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleErrorHandler implements ErrorHandler {

    @Override
    public void error(ErrorEvent errorEvent) {
        log.error("Internal error.", errorEvent.getThrowable());
        if(UI.getCurrent() != null) {
            UI.getCurrent().access(() -> Utils.displayNotification(
                    NotificationVariant.LUMO_ERROR, errorEvent.getThrowable().getMessage()));
        }
    }
}