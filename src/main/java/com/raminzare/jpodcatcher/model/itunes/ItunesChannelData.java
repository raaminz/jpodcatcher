package com.raminzare.jpodcatcher.model.itunes;

public record ItunesChannelData(String explicit, ItunesCategory category,
                                String complete, String type, String author, String title,
                                String block, ItunesOwner owner,
                                String image, String newFeedUrl) {
    public static class Builder {
        String explicit;
        ItunesCategory category;
        String complete;
        String type;
        String author;
        String title;
        String block;
        ItunesOwner owner;
        String image;
        String newFeedUrl;

        public void setExplicit(String explicit) {
            this.explicit = explicit;
        }

        public void setCategory(ItunesCategory category) {
            this.category = category;
        }

        public void setComplete(String complete) {
            this.complete = complete;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setBlock(String block) {
            this.block = block;
        }

        public void setOwner(ItunesOwner owner) {
            this.owner = owner;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public void setNewFeedUrl(String newFeedUrl) {
            this.newFeedUrl = newFeedUrl;
        }

        public ItunesChannelData build() {
            return new ItunesChannelData(explicit, category, complete, type, author, title, block, owner, image, newFeedUrl);
        }
    }
}
