package client;

import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;

public class Starter {
    public static void main(String[] args) throws Exception {

        String url = "http://localhost:200/login/";//"http://nikitamedvedev.ddns.net";
        HttpClient httpClient = new HttpClient();
        httpClient.start();
        ContentResponse response = httpClient.POST(url)
                .param("login", "nikita_medvedev")
                .param("password", "123456")
//                .param("login","nikita_medvedev")
//                .param("password","123456")
//                .param("name","Nikita Medvedev")
//                .param("email","mymail@gmail.ru")
//                .param("info","Cool!")
                .send();
        System.out.println(response.getContentAsString());
        httpClient.stop();
    }
}
