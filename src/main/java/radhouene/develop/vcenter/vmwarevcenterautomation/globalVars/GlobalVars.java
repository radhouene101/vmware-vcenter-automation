package radhouene.develop.vcenter.vmwarevcenterautomation.globalVars;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GlobalVars {
    public static HttpHeaders cookies;
    public static String sessionId;
    // put username of vsphere
    public static String username="username";
   //password of vsphere
    public static String password="password";
    //put the ip of the server
    public static String serverIP="serverIP";



}
