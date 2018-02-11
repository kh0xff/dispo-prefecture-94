package com.github.zg2pro.dispo.prefecture;

import com.github.zg2pro.spring.rest.basis.template.Zg2proRestTemplate;
import java.util.Collections;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author zg2pro
 */
@SpringBootApplication
public class Application {

    @Bean
    public Boolean checkAvailibility() {
        Zg2proRestTemplate restTemplate = new Zg2proRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_XHTML_XML));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        ResponseEntity<String> resp = restTemplate.getForEntity("http://www.val-de-marne.gouv.fr/booking/create/4963/0", String.class);
        List<String> lStr = resp.getHeaders().get("Set-Cookie");
        String cookie = lStr.get(0);
        cookie = cookie.replaceAll("path=/", "");
        cookie += "xtvrn=$481980$; xtan481980=-; xtant481980=1";
        headers.set("Cookie", cookie);
        //Cookie:eZSESSID=9lp2b5rfmpn7eus77h094qu9b1; xtvrn=$481980$; xtan481980=-; xtant481980=1
        HttpEntity<String> entity = new HttpEntity<>("condition=on&nextButton=Effectuer+une+demande+de+rendez-vous", headers);
        resp = restTemplate.postForEntity("http://www.val-de-marne.gouv.fr/booking/create/4963/0", entity, String.class);
        resp = restTemplate.getForEntity("http://www.val-de-marne.gouv.fr/booking/create/4963/1", String.class);
        entity= new HttpEntity<>("planning=5984&nextButton=Etape+suivante", headers);
        resp = restTemplate.postForEntity("http://www.val-de-marne.gouv.fr/booking/create/4963/1", entity, String.class);
        resp = restTemplate.getForEntity("http://www.val-de-marne.gouv.fr/booking/create/4963/2", String.class);
        String body = resp.getBody();
        System.out.println(body);
        return !body.contains("plus de plage horaire");
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
