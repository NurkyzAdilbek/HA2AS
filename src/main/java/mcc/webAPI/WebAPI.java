package mcc.webAPI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class WebAPI {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper mapper=new ObjectMapper();
        System.out.println("Wetter client startet.Wenn du fertig bist tippe 'Ende' zum beenden" );
        while(true) {

            System.out.println("Fur welche Stadt willst du das Wetter wissen");
            String stadt = scanner.nextLine();
            String stadtR= URLEncoder.encode(stadt,"UTF-8");
            if(stadt.equalsIgnoreCase("Ende")) {
                System.out.println("Programm beendet");
                break;
            }
            String url="https://geocoding-api.open-meteo.com/v1/search?name="+stadtR;
            HttpRequest request=HttpRequest.newBuilder().GET().uri(URI.create(url)).build();
            HttpResponse<String> response=client.send(request,HttpResponse.BodyHandlers.ofString());
            JsonNode json=mapper.readTree(response.body());
            JsonNode results=json.get("results");
            if (results==null||results.isEmpty()){
                System.out.println("Stadt "+stadt+ " nicht gefunden");
                continue;
            }
            JsonNode erstelle=results.get(0);
            double lat=erstelle.get("latitude").asDouble();
            double lng=erstelle.get("longitude").asDouble();
            String gefund=erstelle.get("name").asText();
            System.out.println( "Es wurde mit Daten latitude: "+lat+ " longitude: "+lng + " Stadt: "+gefund+ " gefunden");
            String url2= "https://api.open-meteo.com/v1/forecast?latitude=" + lat
                    + "&longitude=" + lng + "&daily=temperature_2m_max&timezone=auto";

            HttpRequest request2=HttpRequest.newBuilder().GET().uri(URI.create(url2)).build();
            HttpResponse<String>response2=client.send(request2,HttpResponse.BodyHandlers.ofString());
            JsonNode json2=mapper.readTree(response2.body());
            double temperature=json2.get("daily").get("temperature_2m_max").get(0).asDouble();
            System.out.println("Heute in "+stadt+" hochstens "+ temperature+ " C");

        }

    }
}
