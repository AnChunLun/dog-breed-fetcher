package dogapi;

import java.util.*;

public class CachingBreedFetcher implements BreedFetcher {
    private final BreedFetcher fetcher;
    private final Map<String, List<String>> cache = new HashMap<>();
    private int callsMade = 0;

    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.fetcher = fetcher;
    }

    @Override
    public List<String> getSubBreeds(String breed) {
        String key = breed.toLowerCase();

        // Return cached result if exists
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        // Count this as a call even if it fails because of test cases idk why tho
        callsMade++;
        List<String> subBreeds = fetcher.getSubBreeds(key);
        cache.put(key, subBreeds);
        return subBreeds;
    }

    public int getCallsMade() {
        return callsMade;
    }
}