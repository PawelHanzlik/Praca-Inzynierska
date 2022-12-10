package engineering.thesis.PSR;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class PerformanceTest {
    public static final String URL_PATH = "http://localhost:8080/api/maxsat";

    public static void main(String[] args) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .uri(URI.create(URL_PATH + "/GenerateData"))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        HttpRequest request2 = HttpRequest.newBuilder()
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .uri(URI.create(URL_PATH + "/AssignCity"))
                .build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());

        long startTime = System.nanoTime();
        for (int i = 0 ; i < 10000; i++) {
            sendRequest();
        }
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        long seconds = TimeUnit.SECONDS.convert(
                Duration.ofNanos(totalTime));
        System.out.println("10000 records in : " + seconds + " seconds");

    }

    public static void sendRequest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .uri(URI.create(URL_PATH + "/sfps?CordX=0&CordY=0&usersChoices=Yes,Yes,Yes,Yes"))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

    }
}
