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

public class PodCatcherServiceSaxParser implements PodCatcherService {

    private final SAXParser saxParser;

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
            saxParser.parse(uri , handler);
            return handler.getPodcast();
        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static class RSSHandler extends DefaultHandler {
        public static final String CHANNEL = "channel";
        public static final String TITLE = "title";
        public static final String IMAGE = "image";
        public static final String ITEM = "item";

        private String currentParentElement;
        private boolean channelVisited;
        private StringBuilder currentStringBuilder;
        private Podcast.PodcastBuilder podcastBuilder;

        @Override
        public void startDocument() throws SAXException {
            podcastBuilder = new Podcast.PodcastBuilder();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            switch (qName){
                case CHANNEL -> {
                    channelVisited = true;
                    currentParentElement = CHANNEL;
                }
                case IMAGE -> currentParentElement = IMAGE;
                case ITEM -> currentParentElement = ITEM;
                case TITLE ->  currentStringBuilder = new StringBuilder();

            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            switch (qName){
                case TITLE -> {
                    if(currentParentElement .equals(CHANNEL)) {
                        podcastBuilder.setTitle(currentStringBuilder.toString());
                    }
                    currentStringBuilder = null;
                }
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if(currentStringBuilder != null){
                currentStringBuilder.append(ch , start, length);
            }
        }

        @Override
        public void endDocument() throws SAXException {
            if(!channelVisited){
                throw new SAXException("The file is not a Podcast RSS");
            }
        }

        public Podcast getPodcast() {
            return podcastBuilder.build();
        }
    }
}