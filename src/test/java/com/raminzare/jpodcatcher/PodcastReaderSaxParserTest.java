package com.raminzare.jpodcatcher.internal;

import com.raminzare.jpodcatcher.PodcastReaderException;
import com.raminzare.jpodcatcher.model.Item;
import com.raminzare.jpodcatcher.model.Podcast;
import org.junit.jupiter.api.*;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class PodcastReaderSaxParserImplTest {

    static PodcastReaderSaxParserImpl saxParser;
    static String samplePodcastURI;

    @BeforeAll
    static void beforeAll() {
        saxParser = new PodcastReaderSaxParserImpl();
        samplePodcastURI = Objects.requireNonNull(PodcastReaderSaxParserImplTest.class.getClassLoader().getResource("sample_podcast.xml")).toString();
    }

    @Test
    void loadWrongRSSURIShouldThrowException(){
        Assertions.assertThrows(PodcastReaderException.class
                , () -> saxParser.loadRSS("WRONG_URI"));
    }

    @Test
    void loadRSSShouldContainChannelData() throws PodcastReaderException {
        Podcast podcast = saxParser.loadRSS(samplePodcastURI);
        assertAll(() -> assertEquals("Raw Data", podcast.title()),
                () -> assertEquals("We’ve entered a new era.", podcast.description()),
                () -> assertEquals("http://www.rawdatapodcast.com", podcast.link()),
                () -> assertEquals("Thu, 21 Nov 2019 09:00:00 -0000", podcast.pubDate()),
                () -> assertEquals("Wed, 17 Mar 2021 19:22:02 -0000", podcast.lastBuildDate()),
                () -> assertEquals("en", podcast.language()),
                () -> assertEquals("All rights reserved", podcast.copyright()),
                () -> assertEquals("PRX Feeder v1.0.0", podcast.generator()),
                () -> assertEquals("https://f.prxu.org/190/images/RawData_ForWeb_RGB.png", podcast.image().url()),
                () -> assertEquals("Raw Data", podcast.image().title()),
                () -> assertEquals("http://www.rawdatapodcast.com", podcast.image().link())
        );
    }

    @Test
    void loadRSSShouldContainEpisodesData() throws PodcastReaderException {
        Podcast podcast = saxParser.loadRSS(samplePodcastURI);
        assertEquals(2, podcast.items().size());
        Item firstItem = podcast.items().get(0);
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