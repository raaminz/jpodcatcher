module com.raminzare.jpodcatcher {
    requires java.xml;
    requires java.logging;

    exports com.raminzare.jpodcatcher;
    exports com.raminzare.jpodcatcher.model;
    exports com.raminzare.jpodcatcher.model.itunes;
    provides com.raminzare.jpodcatcher.PodcastReader with com.raminzare.jpodcatcher.internal.PodcastReaderSaxParserImpl;
}