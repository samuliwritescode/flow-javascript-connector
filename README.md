# Vaadin Flow JavaScript Connector

## Setting

Client side components of Vaadin Flow are usually Lit or React based web components.
This is usually a good starting point for your own client side components.

See the documentation at https://vaadin.com/docs/latest/flow/create-ui/web-components/an-in-project-web-component

## Problem

What if you would like to use pure JS for Vaadin Flow components without Lit, React or anything else.
If in your use case the Lit or React life cycle or state management is inconvenient or even conflicting something that
you must do. Or let's say that you are integrating some existing JS library that may conflict and having anything extra
in between is redundant.
Or you just want something simple and straightforward and you much rather prefer to use lower level APIs.

## Solution

Use pure Javascript connector. No additional dependencies and you have full control over everything.

Consider an example where you have a server side component and client side JS connector.

### Flow Component:

```java
/*
  JSComponent is backed up by a Div and pure JS connector. No Lit, no Polymer, no React or whatsoever. Just pure JS.
*/
@JavaScript("js-connector.js")
public class JSComponent extends Div {
    public JSComponent() {
        // Make sure that the connector is initialized
        getElement().getNode().runWhenAttached(ui -> ui.getPage().executeJs("window.Vaadin.Flow.jsConnector.initLazy($0)", getElement()));
        getElement().getNode().runWhenAttached(ui -> getElement().callJsFunction("$connector.showHello", "Hello world!"));
    }
}
```

### js-connector.js:

```javascript
/*
  While window.Vaadin.Flow.jsConnector is globally available, 
  it's initLazy takes the element as a parameter and
  decorates it with the actual per element connector.
*/
window.Vaadin.Flow.jsConnector = {
    initLazy: element => {
        // Skip if already initialized
        if (element.$connector) {
            return;
        }

        // Server can now call Element.callJsFunction by referring this.$connector
        element.$connector = {
            showHello: content => {
                alert(content);
            }
        };
    }
}
```

## To run the example application

Run the application by

`mvn spring-boot:run`

then open your browser at http://localhost:8080/