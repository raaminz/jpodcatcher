package com.raminzare.jpodcatcher.model;

import com.raminzare.jpodcatcher.model.itunes.ItunesChannelData;
import com.raminzare.jpodcatcher.model.spotify.SpotifyChannelData;

import java.util.ArrayList;
import java.util.List;

public record Channel(String title, String description, String link, String pubDate, String lastBuildDate,
                      String language, String copyright, String generator, Image image,
                      ItunesChannelData itunesChannelData, SpotifyChannelData spotifyChannelData, List<Item> items) {

    public static class Builder {
        private String title;
        private String description;
        private String link;
        private String pubDate;
        private String lastBuildDate;
        private String language;
        private String copyright;
        private String generator;
        private Image image;
        private ItunesChannelData itunesChannelData;
        private final List<Item> items = new ArrayList<>();

        public void setTitle(String title) {
            this.title = title;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public void setPubDate(String pubDate) {
            this.pubDate = pubDate;
        }

        public void setLastBuildDate(String lastBuildDate) {
            this.lastBuildDate = lastBuildDate;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public void setCopyright(String copyright) {
            this.copyright = copyright;
        }

        public void setGenerator(String generator) {
            this.generator = generator;
        }

        public void setImage(Image image) {
            this.image = image;
        }

        public void setItunesChannelData(ItunesChannelData itunesChannelData) {
            this.itunesChannelData = itunesChannelData;
        }

        public void addItem(Item item) {
            items.add(item);
        }

        public Channel build() {
            return new Channel(title, description, link, pubDate, lastBuildDate, language, copyright, generator, image
                    , itunesChannelData, new SpotifyChannelData(null), items);
        }
    }
}
