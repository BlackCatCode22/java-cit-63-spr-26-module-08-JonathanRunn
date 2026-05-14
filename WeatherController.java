package com.example.weather;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@Controller
public class WeatherController {

    @GetMapping("/")
    public String getDashboard(Model model) {
        RestTemplate restTemplate = new RestTemplate();
        
        // National Weather Service requires a User-Agent header
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "FCC-Student-App");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Fresno, CA (NWS uses grid points, but we'll fetch the station or forecast)
        String fresnoUrl = "https://api.weather.gov/gridpoints/HNX/53,100/forecast";
        // New York, NY
        String nyUrl = "https://api.weather.gov/gridpoints/OKX/33,35/forecast";
        // London, UK (Open-Meteo Bonus)
        String londonUrl = "https://api.open-meteo.com/v1/forecast?latitude=51.5085&longitude=-0.1257&current_weather=true";

        try {
            ResponseEntity<String> fresnoRes = restTemplate.exchange(fresnoUrl, HttpMethod.GET, entity, String.class);
            ResponseEntity<String> nyRes = restTemplate.exchange(nyUrl, HttpMethod.GET, entity, String.class);
            String londonRes = restTemplate.getForObject(londonUrl, String.class);

            model.addAttribute("fresnoJson", fresnoRes.getBody());
            model.addAttribute("nyJson", nyRes.getBody());
            model.addAttribute("londonJson", londonRes);
        } catch (Exception e) {
            model.addAttribute("error", "Error fetching data: " + e.getMessage());
        }

        return "index";
    }
}
