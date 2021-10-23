package com.raminzare.jpodcatcher;

import com.raminzare.jpodcatcher.internal.PodCatcherServiceSaxParser;
import com.raminzare.jpodcatcher.model.Podcast;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertAll;
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

        assertAll(() -> assertEquals("Raw Data", actualPodcast.title()),
                () -> assertEquals("We’ve entered a new era.", actualPodcast.description()),
                () -> assertEquals("http://www.rawdatapodcast.com", actualPodcast.link()),
                () -> assertEquals("Thu, 21 Nov 2019 09:00:00 -0000", actualPodcast.pubDate()),
                () -> assertEquals("Wed, 17 Mar 2021 19:22:02 -0000", actualPodcast.lastBuildDate()),
                () -> assertEquals("en", actualPodcast.language()),
                () -> assertEquals("All rights reserved", actualPodcast.copyright()),
                () -> assertEquals("PRX Feeder v1.0.0", actualPodcast.generator()),
                () -> assertEquals("https://f.prxu.org/190/images/RawData_ForWeb_RGB.png", actualPodcast.image().url()),
                () -> assertEquals("Raw Data", actualPodcast.image().title()),
                () -> assertEquals("http://www.rawdatapodcast.com", actualPodcast.image().link())
        );
    }
}