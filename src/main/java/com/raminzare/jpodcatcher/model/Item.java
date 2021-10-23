package com.raminzare.jpodcatcher.model;

import java.util.ArrayList;
import java.util.List;

public record Item(String guid, String title, String pubDate, String link, String description,
                   String author, List<String> categories) {
    public static class ItemBuilder {
        private String guid;
        private String title;
        private String pubDate;
        private String link;
        private String description;
        private String author;
        private final List<String> categories = new ArrayList<>();

        public ItemBuilder setGuid(String guid) {
            this.guid = guid;
            return this;
        }

        public ItemBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public ItemBuilder setPubDate(String pubDate) {
            this.pubDate = pubDate;
            return this;
        }

        public ItemBuilder setLink(String link) {
            this.link = link;
            return this;
        }

        public ItemBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public ItemBuilder setAuthor(String author) {
            this.author = author;
            return this;
        }

        public ItemBuilder addCategory(String category) {
            this.categories.add(category);
            return this;
        }

        public Item build() {
            return new Item(guid, title, pubDate, link, description, author, categories);
        }
    }
}
