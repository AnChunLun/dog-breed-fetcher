package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        String url = "https://dog.ceo/api/breed/" + breed.toLowerCase() + "/list";
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                throw new BreedNotFoundException("Failed to fetch breed info");
            }

            String jsonData = response.body().string();
            return getSubBreedsList(breed, jsonData);

        } catch (IOException e) {
            throw new BreedNotFoundException("API call failed: " + e.getMessage());
        }
    }

    // Reads data from the jsonData and return a list of the subBreeds with key breed
    private static List<String> getSubBreedsList(String breed, String jsonData) throws BreedNotFoundException {
        JSONObject json = new JSONObject(jsonData);

        String status = json.getString("status");
        if (!"success".equals(status)) {
            throw new BreedNotFoundException("Breed not found: " + breed);
        }

        JSONArray subBreedsArray = json.getJSONArray("message");
        List<String> subBreeds = new ArrayList<>();
        for (int i = 0; i < subBreedsArray.length(); i++) {
            subBreeds.add(subBreedsArray.getString(i));
        }
        return subBreeds;
    }
}