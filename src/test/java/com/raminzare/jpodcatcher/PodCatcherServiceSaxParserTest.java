package com.raminzare.jpodcatcher;

import com.raminzare.jpodcatcher.internal.PodCatcherServiceSaxParser;
import com.raminzare.jpodcatcher.model.Podcast;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

class PodCatcherServiceSaxParserTest {

    PodCatcherService parser;
    public static final String SAMPLE_PODCAST = "podcast.xml";

    String getSampleRSSURI(){
        return Objects.requireNonNull(this.getClass().getClassLoader().getResource(SAMPLE_PODCAST)).toString();
    }

    @Test
    void aRSSUrl_loadRSS_returnPodcastName(){
        parser = new PodCatcherServiceSaxParser();
        Podcast podcast = parser.loadRSS(getSampleRSSURI());
        Assertions.assertEquals( "Compile Podcast / پادکست کامپایل",podcast.title());
    }
}