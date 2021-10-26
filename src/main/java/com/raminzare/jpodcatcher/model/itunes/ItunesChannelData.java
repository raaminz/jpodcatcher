package com.raminzare.jpodcatcher.model.itunes;

import java.util.List;

public record ItunesChannelData(String author, String image, String explicit, List<String> categories,
								String complete, String type, Integer limit, String countryOfOrigin) {

}
