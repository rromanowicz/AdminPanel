package ex.rr.adminpanel.views;

import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.Command;
import ex.rr.adminpanel.layouts.MainLayout;
import ex.rr.adminpanel.services.ChatService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;

import java.util.ArrayList;

@Route(value = "chat", layout = MainLayout.class)
@PageTitle("Chat")
@RolesAllowed("USER")
public class ChatView extends VerticalLayout {

    ChatView(ChatService service) {

        var messageList = new MessageList();
        var textInput = new MessageInput();

        setSizeFull();
        add(messageList, textInput);
        expand(messageList);
        textInput.setWidthFull();

        service.join().subscribe(message -> {
            var nl = new ArrayList<>(messageList.getItems());
            nl.add(new MessageListItem(message.text(), message.time(), message.username()));
            getUI().ifPresent(ui -> ui.access((Command) () -> messageList.setItems(nl)));
        });
        textInput.addSubmitListener(event -> service.add(event.getValue()));

    }

}
