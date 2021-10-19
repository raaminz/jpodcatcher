package com.raminzare.jpodcatcher.internal;

import com.raminzare.jpodcatcher.PodCatcherService;
import com.raminzare.jpodcatcher.model.Podcast;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class PodCatcherServiceSaxParser implements PodCatcherService {

    private final SAXParser saxParser;
    private static final Logger LOG = Logger.getLogger(PodCatcherServiceSaxParser.class.getName());

    public PodCatcherServiceSaxParser() {
        var factory = SAXParserFactory.newInstance();
        try {
            this.saxParser = factory.newSAXParser();
        } catch (ParserConfigurationException | SAXException e) {
            throw new IllegalStateException("Trouble while initializing sax parser", e);
        }
    }

    @Override
    public Podcast loadRSS(String uri) {
        try {
            var handler = new RSSHandler();
            saxParser.parse(uri, handler);
            return handler.getPodcast();
        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static class RSSHandler extends DefaultHandler {
        private final LinkedList<String> navigation = new LinkedList<>();

        private enum Element{
           RSS ,
           CHANNEL,
           TITLE ,
            DESCRIPTION , LINK , PUB_DATE("pubDate"),LAST_BUILD_DATE("lastBuildDate"),
            LANGUAGE,COPYRIGHT , GENERATOR,
           IMAGE ,
           ITEM  ;

           private final String elementName;
           Element(){
               this.elementName= name().toLowerCase();
           }
           Element(String elementName){
               this.elementName = elementName;
           }

           String getElementName() {
                return elementName;
            }

           static Optional<Element> getElement(String elementName){
             return Arrays.stream(Element.values())
                     .filter(el-> el.elementName.equalsIgnoreCase(elementName)).findFirst();
           }

        }

        private StringBuilder currentStringBuilder;
        private Podcast.PodcastBuilder podcastBuilder;
        private int elementIndexVisited = 0;

        @Override
        public void startDocument() throws SAXException {
            podcastBuilder = new Podcast.PodcastBuilder();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            navigation.addLast(qName);

            var supportedElement =
                     Element.getElement(qName).orElse(null);
            if(supportedElement != null){
                switch (supportedElement) {
                    case RSS  -> {
                        if(elementIndexVisited != 0){
                            throw new SAXException("No RSS element found in the XML");
                        }
                    }
                    case CHANNEL -> {
                        if(elementIndexVisited != 1){
                            throw new SAXException("No RSS element found in the XML");
                        }
                    }
                    default -> currentStringBuilder = new StringBuilder();
                }
            }
            else{
                //TODO is logging enough?
                LOG.fine(()-> "Element %s not supported yet".formatted(qName));
            }


            if((elementIndexVisited == 0 && !qName.equals("rss")) ||
                    (elementIndexVisited == 1 && !qName.equals(
                    Element.CHANNEL.getElementName()))){
                throw new SAXException("Not a valid Podcast rss");
            }

            elementIndexVisited++;
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            navigation.removeLast();
            var element = Element.getElement(qName).orElse(null);
            if(!navigation.isEmpty() && element != null) {
                Predicate<Element> checkParent = (elm) -> elm.getElementName().equals(navigation.getLast());
                Supplier<String> content = () -> currentStringBuilder == null ? null : currentStringBuilder.toString().trim();

                if(checkParent.test(Element.CHANNEL)){
                    channelSwitches(element, content.get());
                }
                currentStringBuilder = null;
            }else{
                //TODO decide
            }
        }

        private void channelSwitches(Element element , String content){
            switch (element) {
                case TITLE -> podcastBuilder.setTitle(content);
                case DESCRIPTION -> podcastBuilder.setDescription(content);
                case LINK -> podcastBuilder.setLink(content);
                case COPYRIGHT -> podcastBuilder.setCopyright(content);
                case LANGUAGE -> podcastBuilder.setLanguage(content);
                case GENERATOR -> podcastBuilder.setGenerator(content);
                case PUB_DATE -> podcastBuilder.setPubDate(content);
                case LAST_BUILD_DATE -> podcastBuilder.setLastBuildDate(content);
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (currentStringBuilder != null) {
                currentStringBuilder.append(ch, start, length);
            }
        }

        @Override
        public void endDocument() throws SAXException {

        }

        public Podcast getPodcast() {
            return podcastBuilder.build();
        }
    }
}