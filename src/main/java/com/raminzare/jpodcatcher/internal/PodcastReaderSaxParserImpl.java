package com.raminzare.jpodcatcher.internal;

import com.raminzare.jpodcatcher.PodcastReader;
import com.raminzare.jpodcatcher.PodcastReaderException;
import com.raminzare.jpodcatcher.model.Enclosure;
import com.raminzare.jpodcatcher.model.Image;
import com.raminzare.jpodcatcher.model.Item;
import com.raminzare.jpodcatcher.model.Podcast;
import com.raminzare.jpodcatcher.model.itunes.ItunesChannelData;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class PodcastReaderSaxParserImpl implements PodcastReader {

    private static final Logger LOG = Logger.getLogger(PodcastReaderSaxParserImpl.class.getName());
    private final SAXParser saxParser;

    public PodcastReaderSaxParserImpl() {
        var factory = SAXParserFactory.newInstance();
        try {
            this.saxParser = factory.newSAXParser();
        } catch (ParserConfigurationException | SAXException e) {
            throw new IllegalStateException("Trouble while initializing sax parser", e);
        }
    }

    @Override
    public Podcast loadRSS(String uri) throws PodcastReaderException {
        try {
            var handler = new RSSHandler();
            saxParser.parse(uri, handler);
            return handler.getPodcast();
        } catch (IOException | SAXException e) {
            throw new PodcastReaderException(e);
        }
    }

    private static class RSSHandler extends DefaultHandler {
        private final Deque<String> navigation = new LinkedList<>();

        private StringBuilder currentStringBuilder;
        private Podcast.PodcastBuilder podcastBuilder;
        private Image.ImageBuilder imageBuilder;
        private Item.ItemBuilder itemBuilder;
        private ItunesChannelData.ItemChannelDataBuilder itemChannelDataBuilder;

        @Override
        public void startDocument() throws SAXException {
            podcastBuilder = new Podcast.PodcastBuilder();
            imageBuilder = new Image.ImageBuilder();
            itemBuilder = new Item.ItemBuilder();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            var supportedElement =
                    Element.getElement(qName).orElse(null);
            Predicate<Element> checkParent = elm -> navigation.isEmpty() || navigation.peek().equals(elm.getElementName());

            if (supportedElement != null) {
                switch (supportedElement) {
                    case RSS -> {
                        if (!navigation.isEmpty()) {
                            throw new SAXException("No RSS element found in the XML");
                        }
                    }
                    case CHANNEL -> {
                        if (!checkParent.test(Element.RSS)) {
                            throw new SAXException("No RSS element found in the XML");
                        }
                    }
                    case IMAGE -> {
                        if (!checkParent.test(Element.CHANNEL)) {
                            throw new SAXException("No Channel element found in the XML");
                        }
                        imageBuilder = new Image.ImageBuilder();
                    }
                    case ITEM -> {
                        if (!checkParent.test(Element.CHANNEL)) {
                            throw new SAXException("No Channel element found in the XML");
                        }
                        itemBuilder = new Item.ItemBuilder();
                    }
                    case ENCLOSURE -> {
                        if (checkParent.test(Element.ITEM) && itemBuilder != null) {
                            readEnclosureElement(attributes);
                        }
                    }
                    default -> currentStringBuilder = new StringBuilder();
                }

            } else {
                //TODO is logging enough?
                LOG.fine(() -> "Element %s not supported yet".formatted(qName));
            }

            navigation.push(qName);
        }

        private void readEnclosureElement(Attributes attributes) {
            var enclosure = new Enclosure.EnclosureBuilder()
                    .setLength(Optional.ofNullable(attributes.getValue("length"))
                            .map(Long::valueOf).orElse(null))
                    .setType(attributes.getValue("type"))
                    .setUrl(attributes.getValue("url")).build();
            itemBuilder.setEnclosure(enclosure);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            navigation.poll();
            var element = Element.getElement(qName);
            if (!navigation.isEmpty() && element.isPresent()) {
                Predicate<Element> checkParent = elm -> elm.getElementName().equals(navigation.peek());
                Supplier<String> content = () -> currentStringBuilder == null ? null : currentStringBuilder.toString().trim();

                if (checkParent.test(Element.CHANNEL)) {
                    channelSwitches(element.get(), content.get());
                } else if (checkParent.test(Element.IMAGE)) {
                    imageSwitches(element.get(), content.get());
                } else if (checkParent.test(Element.ITEM)) {
                    itemSwitches(element.get(), content.get());
                }
                currentStringBuilder = null;
            } else {
                //TODO decide
            }
        }

        private void itemSwitches(Element element, String content) {
            switch (element) {
                case GUID -> itemBuilder.setGuid(content);
                case TITLE -> itemBuilder.setTitle(content);
                case PUB_DATE -> itemBuilder.setPubDate(content);
                case LINK -> itemBuilder.setLink(content);
                case DESCRIPTION -> itemBuilder.setDescription(content);
                case CATEGORY -> itemBuilder.addCategory(content);
                case ENCLOSURE -> {/*Already handled with attributes*/}
                default -> LOG.warning(() -> "%s element with value %s is not supported as ITEM info".formatted(element, content));
            }
        }

        private void imageSwitches(Element element, String content) {
            switch (element) {
                case URL -> imageBuilder.setUrl(content);
                case TITLE -> imageBuilder.setTitle(content);
                case LINK -> imageBuilder.setLink(content);
                default -> LOG.warning(() -> "%s element with value %s is not supported as IMAGE info".formatted(element, content));
            }
        }

        private void channelSwitches(Element element, String content) {
            switch (element) {
                case TITLE -> podcastBuilder.setTitle(content);
                case DESCRIPTION -> podcastBuilder.setDescription(content);
                case LINK -> podcastBuilder.setLink(content);
                case COPYRIGHT -> podcastBuilder.setCopyright(content);
                case LANGUAGE -> podcastBuilder.setLanguage(content);
                case GENERATOR -> podcastBuilder.setGenerator(content);
                case PUB_DATE -> podcastBuilder.setPubDate(content);
                case LAST_BUILD_DATE -> podcastBuilder.setLastBuildDate(content);
                case IMAGE -> {
                    podcastBuilder.setImage(imageBuilder.build());
                    imageBuilder = null;
                }
                case ITEM -> {
                    podcastBuilder.addItem(itemBuilder.build());
                    itemBuilder = null;
                }
                default -> LOG.warning(() -> "%s element with value %s is not supported as CHANNEL info".formatted(element, content));
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
            //Do Nothing
        }

        public Podcast getPodcast() {
            return podcastBuilder.build();
        }

        private enum Element {
            RSS, CHANNEL, TITLE, DESCRIPTION, LINK, PUB_DATE("pubDate"),
            LAST_BUILD_DATE("lastBuildDate"), LANGUAGE, COPYRIGHT, GENERATOR, IMAGE, ITEM,
            URL, GUID, AUTHOR, CATEGORY, ENCLOSURE;

            private final String elementName;

            Element() {
                this.elementName = name().toLowerCase();
            }

            Element(String elementName) {
                this.elementName = elementName;
            }

            static Optional<Element> getElement(String elementName) {
                return Arrays.stream(Element.values())
                        .filter(el -> el.elementName.equalsIgnoreCase(elementName)).findFirst();
            }

            String getElementName() {
                return elementName;
            }

        }
    }
}