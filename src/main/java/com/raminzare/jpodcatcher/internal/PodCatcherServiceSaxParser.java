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
import java.util.*;
import java.util.logging.Level;
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
        private enum Element{
           RSS ,
           CHANNEL,
           TITLE ,
            DESCRIPTION , LINK , PUB_DATE,LAST_BUILD_DATE,
            LANGUAGE,COPYRIGHT , GENERATOR,
           IMAGE ,
           ITEM ;

           static Element findElement(String name){
               Arrays.stream(values())

                       .map(Enum::name).map(str-> str.replace('_',''))

           }

        }

        private Queue<String> navigation = new LinkedList<>();

        private StringBuilder currentStringBuilder;
        private Podcast.PodcastBuilder podcastBuilder;
        private int elementIndexVisited = 0;

        @Override
        public void startDocument() throws SAXException {
            podcastBuilder = new Podcast.PodcastBuilder();
        }

        /* Podcast.PodcastBuilder builder = new Podcast.PodcastBuilder();
        builder.setTitle("Raw Data");
        builder.setDescription("We’ve entered a new era.");
        builder.setLink("http://www.rawdatapodcast.com");
        builder.setPubDate("Thu, 21 Nov 2019 09:00:00 -0000");
        builder.setLastBuildDate("Wed, 17 Mar 2021 19:22:02 -0000");
        builder.setLanguage("en");
        builder.setCopyright("en");
        builder.setGenerator("PRX Feeder v1.0.0");*/

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            navigation.offer(qName);

            var supportedElement =
                     Element.valueOf(qName.toUpperCase());
            if(supportedElement != null){
                switch (supportedElement) {
                    case TITLE -> currentStringBuilder = new StringBuilder();

                    case RSS  -> {
                        if(elementIndexVisited == 0){
                            throw new SAXException("No RSS element found in the XML");
                        }
                    }
                    case CHANNEL -> {
                        if(elementIndexVisited == 1){
                            throw new SAXException("No RSS element found in the XML");
                        }
                    }
                }
            }
            else{
                LOG.fine(()-> "Element %s not supported yet".formatted(qName));
            }


            if((elementIndexVisited == 0 && !qName.equals("rss")) || (elementIndexVisited == 1 && !qName.equals(CHANNEL))){
                throw new SAXException("Not a valid Podcast rss");
            }


            elementIndexVisited++;
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            navigation.poll();
            switch (qName) {
                case TITLE -> {
                    //Parent open element
                    if (CHANNEL.equals(navigation.peek())) {
                        podcastBuilder.setTitle(currentStringBuilder.toString());
                    }
                    currentStringBuilder = null;
                }
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