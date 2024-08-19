package org.samuliwritescode.purejsconnector;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Route("/")
    public static class MainRoute extends Div {
        public MainRoute() {
            add(new JSComponent() {{
                setContent("""                     
                        Content here below is a Flow component backed up by a <b>pure Javascript</b> component. <br>
                        For demonstration purposes this allows raw HTML content from user, which is a <span style="color: red; font-weight: bold;">bad idea</span> to do in 
                        real life!
                        """);
            }});
            JSComponent jsComponent = new JSComponent();
            add(new TextArea() {{
                addValueChangeListener(e -> jsComponent.setContent(e.getValue()));
                setHeight("300px");
                setWidthFull();
            }});
            add(jsComponent);
        }
    }

    /**
     * JSComponent is backed up by a Div and pure JS connector. No Lit, no Polymer, no React or whatsoever. Just pure JS.
     */
    @JavaScript("js-connector.js")
    public static class JSComponent extends Div {
        public JSComponent() {
            // Make sure that the connector is initialized
            getElement().getNode().runWhenAttached(ui -> ui.beforeClientResponse(this, ctx -> ui.getPage().executeJs("window.Vaadin.Flow.jsConnector.initLazy($0)", getElement())));
        }

        public void setContent(String unsanitizedHtml) {
            //js-connector.js created $connector for the element.
            getElement().getNode().runWhenAttached(ui -> ui.beforeClientResponse(this, ctx -> getElement().callJsFunction("$connector.setContent", unsanitizedHtml)));
        }

        @ClientCallable
        public void showNotificationOnServer(String content) {
            Notification.show(content);
        }
    }
}
