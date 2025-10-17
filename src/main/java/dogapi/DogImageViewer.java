package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class DogImageViewer {

    private static final OkHttpClient client = new OkHttpClient();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                String breed = "shiba"; // Change breed here!!!!!!!!!
                String imageUrl = getRandomBreedImageUrl(breed);
                showImage(imageUrl, breed);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    // Fetch a random image URL for the given breed from the Dog API
    public static String getRandomBreedImageUrl(String breed) throws IOException {
        String url = "https://dog.ceo/api/breed/" + breed.toLowerCase() + "/images/random";
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                throw new IOException("Failed to fetch image for breed: " + breed);
            }

            String json = response.body().string();
            JSONObject obj = new JSONObject(json);
            if (!"success".equals(obj.getString("status"))) {
                throw new IOException("Breed not found: " + breed);
            }

            return obj.getString("message"); // <- Dis the image URL
        }
    }

    // Display an image via simple GUI
    public static void showImage(String imageUrl, String breed) {
        JFrame frame = new JFrame("Random Dog Image: " + breed);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        try {
            URL url = new URL(imageUrl);
            ImageIcon imageIcon = new ImageIcon(url);
            JLabel label = new JLabel(imageIcon);
            frame.add(label, BorderLayout.CENTER);
        } catch (Exception e) {
            frame.add(new JLabel("Failed to load image."), BorderLayout.CENTER);
        }

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}