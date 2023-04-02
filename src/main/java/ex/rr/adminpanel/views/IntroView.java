package ex.rr.adminpanel.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import ex.rr.adminpanel.layouts.MainLayout;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Intro")
@AnonymousAllowed
public class IntroView extends VerticalLayout {
    IntroView() {

        String text = """
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Pellentesque elit ullamcorper dignissim cras tincidunt lobortis feugiat vivamus. Scelerisque fermentum dui faucibus in ornare. Ac feugiat sed lectus vestibulum mattis ullamcorper. Rutrum tellus pellentesque eu tincidunt tortor. Maecenas volutpat blandit aliquam etiam erat velit scelerisque in dictum. Eu consequat ac felis donec. Amet cursus sit amet dictum. Amet tellus cras adipiscing enim. Blandit massa enim nec dui nunc mattis enim. Vel pretium lectus quam id leo in vitae. Purus gravida quis blandit turpis cursus in. Euismod elementum nisi quis eleifend quam. Pulvinar etiam non quam lacus suspendisse. Integer vitae justo eget magna. Est ultricies integer quis auctor elit. Morbi tristique senectus et netus. Sapien nec sagittis aliquam malesuada bibendum arcu vitae.<br>
                <br>
                Nibh sit amet commodo nulla facilisi nullam vehicula ipsum. Massa sed elementum tempus egestas sed. Ipsum a arcu cursus vitae congue mauris. Sem nulla pharetra diam sit. Sed turpis tincidunt id aliquet risus feugiat in ante metus. Ligula ullamcorper malesuada proin libero nunc consequat interdum. Tincidunt ornare massa eget egestas purus viverra. At imperdiet dui accumsan sit amet nulla facilisi. Tristique nulla aliquet enim tortor at auctor. Lectus mauris ultrices eros in cursus. Amet facilisis magna etiam tempor orci eu lobortis elementum. Amet nisl purus in mollis nunc sed id semper. Ridiculus mus mauris vitae ultricies leo integer. Mi eget mauris pharetra et ultrices. Commodo sed egestas egestas fringilla.<br>
                <br>
                Sagittis id consectetur purus ut faucibus pulvinar. Lacus laoreet non curabitur gravida arcu ac tortor dignissim. Nunc mattis enim ut tellus elementum sagittis vitae et leo. Consectetur a erat nam at. Mauris in aliquam sem fringilla ut morbi. Velit egestas dui id ornare. Magna etiam tempor orci eu lobortis. Cursus metus aliquam eleifend mi. Quam pellentesque nec nam aliquam sem et tortor consequat. Facilisi cras fermentum odio eu. Quis hendrerit dolor magna eget est lorem ipsum dolor. Sapien faucibus et molestie ac feugiat sed lectus. Habitant morbi tristique senectus et netus et. A pellentesque sit amet porttitor eget dolor morbi non arcu. Vitae sapien pellentesque habitant morbi tristique senectus.<br>
                <br>
                Massa enim nec dui nunc mattis enim ut tellus elementum. Ac turpis egestas integer eget. Eget nunc scelerisque viverra mauris in aliquam sem. Tincidunt lobortis feugiat vivamus at augue eget arcu dictum varius. Sagittis id consectetur purus ut faucibus pulvinar. Mauris ultrices eros in cursus turpis massa tincidunt dui. Nunc scelerisque viverra mauris in aliquam. Euismod quis viverra nibh cras. Diam vel quam elementum pulvinar. Pretium vulputate sapien nec sagittis. Morbi non arcu risus quis varius quam quisque id. Amet cursus sit amet dictum sit amet justo donec. Aliquet risus feugiat in ante metus dictum at. Viverra nibh cras pulvinar mattis nunc sed blandit libero. Rhoncus dolor purus non enim praesent elementum facilisis. Vulputate dignissim suspendisse in est ante in nibh mauris cursus. Dignissim enim sit amet venenatis urna cursus eget nunc scelerisque.<br>
                <br>
                Sagittis id consectetur purus ut faucibus pulvinar. Lacus laoreet non curabitur gravida arcu ac tortor dignissim. Nunc mattis enim ut tellus elementum sagittis vitae et leo. Consectetur a erat nam at. Mauris in aliquam sem fringilla ut morbi. Velit egestas dui id ornare. Magna etiam tempor orci eu lobortis. Cursus metus aliquam eleifend mi. Quam pellentesque nec nam aliquam sem et tortor consequat. Facilisi cras fermentum odio eu. Quis hendrerit dolor magna eget est lorem ipsum dolor. Sapien faucibus et molestie ac feugiat sed lectus. Habitant morbi tristique senectus et netus et. A pellentesque sit amet porttitor eget dolor morbi non arcu. Vitae sapien pellentesque habitant morbi tristique senectus.<br>
                <br>
                Massa enim nec dui nunc mattis enim ut tellus elementum. Ac turpis egestas integer eget. Eget nunc scelerisque viverra mauris in aliquam sem. Tincidunt lobortis feugiat vivamus at augue eget arcu dictum varius. Sagittis id consectetur purus ut faucibus pulvinar. Mauris ultrices eros in cursus turpis massa tincidunt dui. Nunc scelerisque viverra mauris in aliquam. Euismod quis viverra nibh cras. Diam vel quam elementum pulvinar. Pretium vulputate sapien nec sagittis. Morbi non arcu risus quis varius quam quisque id. Amet cursus sit amet dictum sit amet justo donec. Aliquet risus feugiat in ante metus dictum at. Viverra nibh cras pulvinar mattis nunc sed blandit libero. Rhoncus dolor purus non enim praesent elementum facilisis. Vulputate dignissim suspendisse in est ante in nibh mauris cursus. Dignissim enim sit amet venenatis urna cursus eget nunc scelerisque.<br>
                <br>
                Sagittis id consectetur purus ut faucibus pulvinar. Lacus laoreet non curabitur gravida arcu ac tortor dignissim. Nunc mattis enim ut tellus elementum sagittis vitae et leo. Consectetur a erat nam at. Mauris in aliquam sem fringilla ut morbi. Velit egestas dui id ornare. Magna etiam tempor orci eu lobortis. Cursus metus aliquam eleifend mi. Quam pellentesque nec nam aliquam sem et tortor consequat. Facilisi cras fermentum odio eu. Quis hendrerit dolor magna eget est lorem ipsum dolor. Sapien faucibus et molestie ac feugiat sed lectus. Habitant morbi tristique senectus et netus et. A pellentesque sit amet porttitor eget dolor morbi non arcu. Vitae sapien pellentesque habitant morbi tristique senectus.<br>
                <br>
                Massa enim nec dui nunc mattis enim ut tellus elementum. Ac turpis egestas integer eget. Eget nunc scelerisque viverra mauris in aliquam sem. Tincidunt lobortis feugiat vivamus at augue eget arcu dictum varius. Sagittis id consectetur purus ut faucibus pulvinar. Mauris ultrices eros in cursus turpis massa tincidunt dui. Nunc scelerisque viverra mauris in aliquam. Euismod quis viverra nibh cras. Diam vel quam elementum pulvinar. Pretium vulputate sapien nec sagittis. Morbi non arcu risus quis varius quam quisque id. Amet cursus sit amet dictum sit amet justo donec. Aliquet risus feugiat in ante metus dictum at. Viverra nibh cras pulvinar mattis nunc sed blandit libero. Rhoncus dolor purus non enim praesent elementum facilisis. Vulputate dignissim suspendisse in est ante in nibh mauris cursus. Dignissim enim sit amet venenatis urna cursus eget nunc scelerisque.<br>
                <br>
                Ac odio tempor orci dapibus ultrices in iaculis. Sed blandit libero volutpat sed cras ornare. Dignissim enim sit amet venenatis urna cursus eget. Morbi tristique senectus et netus. Vitae aliquet nec ullamcorper sit amet risus. Fermentum iaculis eu non diam phasellus vestibulum. Lacus sed viverra tellus in. Id velit ut tortor pretium viverra suspendisse. Mi quis hendrerit dolor magna eget. Malesuada fames ac turpis egestas maecenas pharetra. Et odio pellentesque diam volutpat commodo sed egestas egestas fringilla.<br>
                """;

        Paragraph p = new Paragraph();
        p.getElement().setProperty("innerHTML", text);
        p.getElement().getStyle().set("font-size", "20px");

        setAlignItems(Alignment.CENTER);
        VerticalLayout layout = new VerticalLayout();
        layout.setWidth("80%");
        layout.setAlignItems(Alignment.CENTER);
        layout.add(new H1("Hello"));
        layout.add(p);

        add(layout);
    }
}
