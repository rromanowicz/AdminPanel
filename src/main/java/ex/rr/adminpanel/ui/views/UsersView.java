package ex.rr.adminpanel.ui.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ex.rr.adminpanel.configuration.UserService;
import ex.rr.adminpanel.database.User;
import ex.rr.adminpanel.ui.forms.UserForm;
import ex.rr.adminpanel.ui.layouts.MainLayout;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "users", layout = MainLayout.class)
@PageTitle("Users")
@RolesAllowed("ADMIN")
public class UsersView extends VerticalLayout {

    private final UserService userService;

    private final Grid<User> grid = new Grid<>(User.class, false);
    private Dialog dialog;

    UsersView(UserService userService) {
        setAlignItems(FlexComponent.Alignment.CENTER);
        VerticalLayout layout = new VerticalLayout();
        layout.setWidth("80%");

        setSizeFull();
        expand(layout);
        expand(grid);

        this.userService = userService;

        Button addTrigger = new Button("Add User");
        addTrigger.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addTrigger.addClickListener(event -> editUser(null));
        HorizontalLayout header = new HorizontalLayout(addTrigger);
        header.getStyle().set("flex-wrap", "wrap");
        header.setJustifyContentMode(JustifyContentMode.END);

        configureGrid();
        updateGrid();

//        getThemeList().clear();
//        getThemeList().add("spacing-s");

        layout.setAlignItems(Alignment.STRETCH);
        layout.add(header, grid);
        add(layout);
    }

    private void configureGrid() {
        grid.addColumn(User::getId).setHeader("Id").setFlexGrow(0);
        grid.addColumn(User::getUsername).setHeader("Username").setFlexGrow(1);
        grid.addColumn(User::getPassword).setHeader("Password").setFlexGrow(1);
        grid.addColumn(User::getEmail).setHeader("Email").setFlexGrow(2);
        grid.addColumn(User::getRoles).setHeader("Roles").setFlexGrow(2);
        grid.addColumn(User::isActive).setHeader("Enabled").setFlexGrow(0);
        grid.addComponentColumn(trigger -> {
            Button editButton = new Button("Edit");
            editButton.addClickListener(e -> editUser(trigger));
            return editButton;
        }).setWidth("150px").setFlexGrow(0);
    }

    private void updateGrid() {
        grid.setItems(userService.findAll());
    }

    private void editUser(User user) {
        User tempUser = (user == null) ? new User() : user;
        dialog = new Dialog();
        dialog.setHeaderTitle("User");

        UserForm form = new UserForm();
        form.setUser(tempUser);
        form.addListener(UserForm.SaveEvent.class, e -> {
            userService.createUser(e.getUser());
            updateGrid();
            dialog.close();
        });
        form.addListener(UserForm.DeleteEvent.class, e -> {
            userService.disable(e.getUser().getId());
            updateGrid();
            dialog.close();
        });
        form.addListener(UserForm.CloseEvent.class, e -> dialog.close());

        dialog.add(form);
        dialog.open();
    }

}
