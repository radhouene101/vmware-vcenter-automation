package radhouene.develop.vcenter.vmwarevcenterautomation.services;


import lombok.*;
import org.apache.commons.codec.binary.Base64;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import radhouene.develop.vcenter.vmwarevcenterautomation.globalVars.GlobalVars;

import java.nio.charset.Charset;

@Getter
@Setter
@Service
@RequiredArgsConstructor

public class AuthenticationManagement {


    private static RestTemplate restTemplate = new RestTemplate();
    public static HttpHeaders createHeaders(String username, String password){
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("US-ASCII")) );
            String authHeader = "Basic " + new String( encodedAuth );
            set( "Authorization", authHeader );
        }};
    }
    public static HttpHeaders loginToVCenter(String username, String password) throws JSONException {
        // Endpoint URL
        String url = "https://"+GlobalVars.serverIP+"/rest/com/vmware/cis/session";

        // Set up headers with basic authentication
        HttpHeaders headers = new HttpHeaders(createHeaders(username,password));
        // Create the HTTP entity with headers
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Send the POST request
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class);

        // Extract session token from the response body (typically JSON)
        GlobalVars.cookies=headers;
        JSONObject j = new JSONObject(response.getBody());
        GlobalVars.sessionId = (String) j.get("value");
        // Return or use the session token as needed
        return headers;
    }
    @Scheduled(fixedRate = 600000)
    void login() throws JSONException {
        System.out.println(loginToVCenter(GlobalVars.username,GlobalVars.password).get("Authorization"));

    }
    public static HttpEntity<String> factoryHeader(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("vmware-api-session-id", GlobalVars.sessionId);
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        return entity;
    }


}