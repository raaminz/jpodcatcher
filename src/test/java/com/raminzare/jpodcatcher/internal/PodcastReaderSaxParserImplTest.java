package com.raminzare.jpodcatcher.internal;

import com.raminzare.jpodcatcher.PodcastReaderException;
import com.raminzare.jpodcatcher.api.RSSURI;
import com.raminzare.jpodcatcher.api.URIParameterResolverExtension;
import com.raminzare.jpodcatcher.model.Channel;
import com.raminzare.jpodcatcher.model.Item;
import com.raminzare.jpodcatcher.model.itunes.ItunesChannelData;
import com.raminzare.jpodcatcher.model.itunes.ItunesItemData;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Podcast reader test using SAX parser")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(URIParameterResolverExtension.class)
class PodcastReaderSaxParserImplTest {

    PodcastReaderSaxParserImpl saxParser;

    PodcastReaderSaxParserImplTest(TestInfo info){
        System.out.println(info);
    }

    @BeforeEach
    void beforeEach(TestInfo info ,@TempDir File tempDir) {
        saxParser = new PodcastReaderSaxParserImpl();
    }

    @Test
    @DisplayName("loading a wrong RSS should throw exception")
    void load_wrong_RSS_URI_should_throw_exception(TestInfo info , TestReporter testReporter) {
        testReporter.publishEntry("The program doesn't let to load wrong URIs");
        Assertions.assertThrows(PodcastReaderException.class
                , () -> saxParser.loadRSS("WRONG_URI"));
    }

    @Test
    void load_RSS_should_contain_channel_data(@RSSURI("simple_podcast.rss") String simplePodcastURI) throws PodcastReaderException {
        Channel channel = saxParser.loadRSS(simplePodcastURI);
        assertAll(() -> assertEquals("Raw Data", channel.title()),
                () -> assertEquals("We’ve entered a new era.", channel.description()),
                () -> assertEquals("http://www.rawdatapodcast.com", channel.link()),
                () -> assertEquals("Thu, 21 Nov 2019 09:00:00 -0000", channel.pubDate()),
                () -> assertEquals("Wed, 17 Mar 2021 19:22:02 -0000", channel.lastBuildDate()),
                () -> assertEquals("en", channel.language()),
                () -> assertEquals("All rights reserved", channel.copyright()),
                () -> assertEquals("PRX Feeder v1.0.0", channel.generator()),
                () -> assertEquals("https://f.prxu.org/190/images/RawData_ForWeb_RGB.png", channel.image().url()),
                () -> assertEquals("Raw Data", channel.image().title()),
                () -> assertEquals("http://www.rawdatapodcast.com", channel.image().link())
        );
    }

    @Test
    void load_RSS_should_contain_episode_data(@RSSURI("simple_podcast.rss") String simplePodcastURI) throws PodcastReaderException {
        Channel channel = saxParser.loadRSS(simplePodcastURI);
        assertEquals(2, channel.items().size());
        Item firstItem = channel.items().get(0);
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

    @Nested
    @DisplayName("Unit tests related to itunes elements")
    class ItunesPodcastElementTest {
        @Test
        void load_RSS_should_contain_itunes_data(@RSSURI("podcast_with_itunes.rss") String podcastWithItunesURI) throws PodcastReaderException {
            Channel channel = saxParser.loadRSS(podcastWithItunesURI);
            ItunesChannelData itunes = channel.itunesChannelData();
            assertAll(
                    () -> assertEquals("FeedForAll Mac OS Team", itunes.author())
                    , () -> assertEquals("RSS Feed Podcast", itunes.title())
                    , () -> assertEquals("serial", itunes.type())
                    , () -> assertEquals("No", itunes.block())
                    , () -> assertEquals("No", itunes.complete())
                    , () -> assertEquals("False", itunes.explicit())
                    , () -> assertEquals("https://applehosted.podcasts.apple.com/hiking_treks/artwork.png", itunes.image())
                    , () -> assertEquals("https://newlocation.com/example.rss", itunes.newFeedUrl())
                    , () -> assertEquals("Technology", itunes.category().category())
                    , () -> assertEquals(Collections.singletonList("Information Technology"), itunes.category().subCategories())
                    , () -> assertEquals("FeedForAll Mac OS Team", itunes.owner().name())
                    , () -> assertEquals("macsupport@feedforall.com", itunes.owner().email())
            );
        }

        @Test
        void load_RSS_should_contain_itunes_episode_data(@RSSURI("podcast_with_itunes.rss") String podcastWithItunesURI) throws PodcastReaderException {
            Channel channel = saxParser.loadRSS(podcastWithItunesURI);
            ItunesItemData itunes = channel.items().get(0).itunesItemData();
            assertAll(
                    () -> assertEquals("4", itunes.episode())
                    , () -> assertEquals("1", itunes.season())
                    , () -> assertEquals("trailer", itunes.episodeType())
                    , () -> assertEquals("Hiking Treks Trailer", itunes.title())
                    , () -> assertEquals("1079", itunes.duration())
                    , () -> assertEquals("https://applehosted.podcasts.apple.com/hiking_treks/artwork2.png", itunes.image())
                    , () -> assertEquals("No", itunes.block())
            );
        }
    }


}