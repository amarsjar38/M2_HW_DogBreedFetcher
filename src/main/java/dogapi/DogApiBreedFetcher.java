package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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
    private static final String API_URL = "https://dog.ceo/api";
    private static final String STATUS = "status";
    private static final String SUCCESS = "success";

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        final List<String> subBreeds = new ArrayList<>();
        client.newBuilder().build();
        final Request request = new Request.Builder()
                .url(String.format("%s/breed/%s/list", API_URL, breed))
                .method("GET", null)
                .build();
        System.out.println(request);

        try{
            final Response response = client.newCall(request).execute();
            final JSONObject responseBody = new JSONObject(response.body().string());
            System.out.println(responseBody);

            if(responseBody.getString(STATUS).equals(SUCCESS)) {
                final JSONArray sub_breeds = responseBody.getJSONArray("message");
                for(int i = 0; i < sub_breeds.length(); i++) {
                    subBreeds.add(sub_breeds.getString(i));
                }
            }
            else{
                throw new BreedNotFoundException(breed);
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return subBreeds;
    }
}