package com.raminzare.jpodcatcher;

import com.raminzare.jpodcatcher.model.Channel;

public interface PodcastReader {
    Channel loadRSS(String uri) throws PodcastReaderException;
}
