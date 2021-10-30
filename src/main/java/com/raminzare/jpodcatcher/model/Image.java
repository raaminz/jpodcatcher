package com.raminzare.jpodcatcher.model;

public record Image(String url, String title, String link) {

    public static class Builder {
        private String url;
        private String title;
        private String link;

        public void setUrl(String url) {
            this.url = url;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public Image build() {
            return new Image(url, title, link);
        }
    }
}
