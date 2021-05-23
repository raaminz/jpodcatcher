package com.raminzare.jpodcatcher.model;

public record Podcast(String title, String description, String link, String pubDate, String lastBuildDate,
                      String language, String copyright, String generator) {

    public static class PodcastBuilder {
        private String title;
        private String description;
        private String link;
        private String pubDate;
        private String lastBuildDate;
        private String language;
        private String copyright;
        private String generator;

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


        public Podcast build() {
            return new Podcast(title, description, link, pubDate, lastBuildDate, language, copyright, generator);
        }
    }
}
