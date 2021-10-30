package com.raminzare.jpodcatcher.model;

import com.raminzare.jpodcatcher.model.itunes.ItunesItemData;

import java.util.ArrayList;
import java.util.List;

public record Item(String guid, String title, String pubDate, String link, String description,
                   Enclosure enclosure, List<String> categories, ItunesItemData itunesItemData) {
    public static class Builder {
        private String guid;
        private String title;
        private String pubDate;
        private String link;
        private String description;
        private Enclosure enclosure;
        private ItunesItemData itunesItemData;
        private final List<String> categories = new ArrayList<>();

        public void setGuid(String guid) {
            this.guid = guid;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setPubDate(String pubDate) {
            this.pubDate = pubDate;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setEnclosure(Enclosure enclosure) {
            this.enclosure = enclosure;
        }

        public void addCategory(String category) {
            categories.add(category);
        }

        public void setItunesItemData(ItunesItemData itunesItemData) {
            this.itunesItemData = itunesItemData;
        }

        public Item build() {
            return new Item(guid, title, pubDate, link, description, enclosure, categories, itunesItemData);
        }
    }
}
