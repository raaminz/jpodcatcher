package com.raminzare.jpodcatcher;

import com.raminzare.jpodcatcher.internal.PodCatcherServiceSaxParser;
import com.raminzare.jpodcatcher.model.Podcast;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PodCatcherServiceSaxParserTest {

    public static final String SAMPLE_PODCAST = "simple_podcast.xml";
    PodCatcherService parser;

    String getSampleRSSURI() {
        return Objects.requireNonNull(this.getClass().getClassLoader().getResource(SAMPLE_PODCAST)).toString();
    }

    @Test
    void aRSSUrl_loadRSS_returnPodcastObject() {
        parser = new PodCatcherServiceSaxParser();
        Podcast actualPodcast = parser.loadRSS(getSampleRSSURI());

        Podcast.PodcastBuilder builder = new Podcast.PodcastBuilder();
        builder.setTitle("Raw Data");
        builder.setDescription("We’ve entered a new era.");
        builder.setLink("http://www.rawdatapodcast.com");
        builder.setPubDate("Thu, 21 Nov 2019 09:00:00 -0000");
        builder.setLastBuildDate("Wed, 17 Mar 2021 19:22:02 -0000");
        builder.setLanguage("en");
        builder.setCopyright("All rights reserved");
        builder.setGenerator("PRX Feeder v1.0.0");

        Podcast expectedPodcast = builder.build();
        assertEquals(expectedPodcast, actualPodcast);
    }
}