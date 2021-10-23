package com.raminzare.jpodcatcher.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record Podcast(String title, String description, String link, String pubDate, String lastBuildDate,
                      String language, String copyright, String generator, Image image , List<Item> items) {

    public static class PodcastBuilder {
        private String title;
        private String description;
        private String link;
        private String pubDate;
        private String lastBuildDate;
        private String language;
        private String copyright;
        private String generator;
        private Image image;
        private final List<Item> items =  new ArrayList<>();

        public PodcastBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public PodcastBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public PodcastBuilder setLink(String link) {
            this.link = link;
            return this;
        }

        public PodcastBuilder setPubDate(String pubDate) {
            this.pubDate = pubDate;
            return this;
        }

        public PodcastBuilder setLastBuildDate(String lastBuildDate) {
            this.lastBuildDate = lastBuildDate;
            return this;
        }

        public PodcastBuilder setLanguage(String language) {
            this.language = language;
            return this;
        }

        public PodcastBuilder setCopyright(String copyright) {
            this.copyright = copyright;
            return this;
        }

        public PodcastBuilder setGenerator(String generator) {
            this.generator = generator;
            return this;
        }

        public PodcastBuilder setImage(Image image) {
            this.image = image;
            return this;
        }

        public PodcastBuilder addItem(Item item) {
            this.items.add(item);
            return this;
        }

        public Podcast build() {
            Objects.requireNonNull(title);
            Objects.requireNonNull(description);
            Objects.requireNonNull(link);
            return new Podcast(title, description, link, pubDate, lastBuildDate, language, copyright, generator, image
            ,items);
        }
    }
}
