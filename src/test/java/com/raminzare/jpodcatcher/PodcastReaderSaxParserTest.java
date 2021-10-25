package com.raminzare.jpodcatcher;

import com.raminzare.jpodcatcher.internal.PodcastReaderSaxParserImpl;
import com.raminzare.jpodcatcher.model.Item;
import com.raminzare.jpodcatcher.model.Podcast;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class PodcastReaderSaxParserTest {

    static String samplePodcastURI;
    static PodcastReader parser;

    @BeforeAll
    static void setup() {
        parser = new PodcastReaderSaxParserImpl();
        samplePodcastURI = Objects.requireNonNull(PodcastReaderSaxParserTest.class
                .getClassLoader().getResource("simple_podcast.xml")).toString();
    }

    @Test
    void wrongRSSURIShouldThrowException() {
        Assertions.assertThrows(PodcastReaderException.class, () -> parser.loadRSS("BLAH_BLAH_URI"));
    }

    @Test
    void testLoadRssCorePodcastData() throws PodcastReaderException {
        Podcast actualPodcast = parser.loadRSS(samplePodcastURI);

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

    @Test
    void testLoadRssCoreItemData() throws PodcastReaderException {
        Podcast actualPodcast = parser.loadRSS(samplePodcastURI);
        assertEquals(2, actualPodcast.items().size());
        Item firstItem = actualPodcast.items().get(0);
        assertAll(
                () -> assertEquals("b970-9f45620b0fd1", firstItem.guid()),
                () -> assertEquals("Technically Sweet", firstItem.title()),
                () -> assertEquals("Thu, 21 Nov 2019 09:00:00 -0000", firstItem.pubDate()),
                () -> assertEquals("https://beta.prx.org/stories/295275", firstItem.link()),
                () -> assertNotNull(firstItem.enclosure()),
                () -> assertEquals("https://dts.podtrac.com/Technically_Sweet_P1_Raw_Data.mp3", firstItem.enclosure().url()),
                () -> assertEquals("audio/mpeg", firstItem.enclosure().type()),
                () -> assertEquals(39374396L, firstItem.enclosure().length()),
                () -> assertEquals(Arrays.asList("Blockchain", "Charity ryerson"), firstItem.categories())
        );
    }
}