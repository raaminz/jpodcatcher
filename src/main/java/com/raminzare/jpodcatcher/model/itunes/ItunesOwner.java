package com.raminzare.jpodcatcher.model.itunes;

public record ItunesOwner(String name, String email) {

    public static class Builder {
        private String name;
        private String email;

        public void setName(String name) {
            this.name = name;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public ItunesOwner build() {
            return new ItunesOwner(name, email);
        }
    }
}
