/*
    While window.Vaadin.Flow.jsConnector is globally available it's initLazy takes the element as a parameter and
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
            setContent: content => {
                element.innerHTML = content;

                //Because Flow decorated the element, there is $server available and annotation @ClientCallable will work.
                element.$server.showNotificationOnServer(content);
            }
        };
    }
}