package ex.rr.adminpanel.ui.views;

import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ex.rr.adminpanel.services.TemplateService;
import ex.rr.adminpanel.ui.layouts.MainLayout;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "templates", layout = MainLayout.class)
@PageTitle("Templates")
@RolesAllowed("ADMIN")
public class TemplateView extends VerticalLayout  {

    private final TemplateService templateService;

    TemplateView(TemplateService templateService){
        setAlignItems(FlexComponent.Alignment.CENTER);
        VerticalLayout layout = new VerticalLayout();
        layout.setWidth("80%");

        this.templateService = templateService;
    }

}
