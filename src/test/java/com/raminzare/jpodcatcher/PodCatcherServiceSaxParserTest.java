package com.raminzare.jpodcatcher;

import com.raminzare.jpodcatcher.internal.PodCatcherServiceSaxParser;
import com.raminzare.jpodcatcher.model.Podcast;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Objects;

class PodCatcherServiceSaxParserTest {

    PodCatcherService parser;
    public static final String SAMPLE_PODCAST = "podcast.xml";

    String getSampleRSSURI(){
        return Objects.requireNonNull(this.getClass().getClassLoader().getResource(SAMPLE_PODCAST)).toString();
    }

    @Test
    void aRSSUrl_loadRSS_returnPodcastObject(){
        parser = new PodCatcherServiceSaxParser();
        Podcast podcast = parser.loadRSS(getSampleRSSURI());
        assertAll(
                ()-> assertEquals( "Compile Podcast / پادکست کامپایل",podcast.title()),
                ()-> assertTrue( podcast.description().startWith("پادکستی از طرف یه برنامه نویس")),
                ()-> assertEquals( "https://anchor.fm/compile-podcast",podcast.link()),
                ()-> assertEquals( "https://anchor.fm/compile-podcast",podcast.link()),
                ()-> assertNotNull(podcast.image()),
                ()-> assertEquals( "Anchor Podcasts",podcast.generator()),
                ()-> assertEquals( "Thu, 13 May 2021 22:12:02 GMT",podcast.lastBuildDate()),
                ()-> assertEquals( "Ramin Zare",podcast.author()),
                ()-> assertEquals( "Ramin Zare",podcast.copyright()),
                ()-> assertEquals( "fa",podcast.language())

        );
    }
}