package org.shimado.basicutils.license;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class LicenseManager {

    private final String serverIP;
    private final String apiUrl;

    public LicenseManager(String serverIP, String apiUrl) {
        this.serverIP = serverIP;
        this.apiUrl = apiUrl;
    }

    public boolean validate() throws Exception {
        String requestUrl = apiUrl + "/" + serverIP;
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestUrl))
                .header("User-Agent", "YourPluginLicenseCheck/1.0")
                .timeout(Duration.ofSeconds(10))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.statusCode() == 200;
    }

}
