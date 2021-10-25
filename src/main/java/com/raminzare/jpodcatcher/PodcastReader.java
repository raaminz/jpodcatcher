package com.raminzare.jpodcatcher;

import com.raminzare.jpodcatcher.model.Podcast;

public interface PodcastReader {
    Podcast loadRSS(String uri) throws PodcastReaderException;
}
