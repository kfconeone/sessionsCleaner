package com.kfc.kfconeone;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);


    @Scheduled(fixedRate = 3000000)
    public void SessionMapCleaner() {
//        String fileName = DateTimeFormatter.ofPattern("yyyyMMdd_hh:mm:ss").format(ZonedDateTime.now(ZoneOffset.UTC).plusHours(8));
        System.out.println("========[CleanSessionMap Start]========");
        log.info("Current time： {}", DateTimeFormatter.ofPattern("yyyyMMdd_hh:mm:ss").format(ZonedDateTime.now(ZoneOffset.UTC).plusHours(8)));
        try
        {
            Unirest.get("http://localhost:8081/CleanSessionMap")
                    .asJsonAsync(new Callback<>() {

                        public void failed(UnirestException e) {
                            System.out.println("Clean sessions failed,check server status");
                            e.printStackTrace();
                        }

                        public void completed(HttpResponse<JsonNode> response) {
                            int code = response.getStatus();
                            JsonNode body = response.getBody();
                            System.out.println(String.format("code : %d , body : %s",code,body));
                            System.out.println(String.format("next cleaning time : %s",DateTimeFormatter.ofPattern("yyyyMMdd_hh:mm:ss").format(ZonedDateTime.now(ZoneOffset.UTC).plusHours(8).plusMinutes(30))));
                            System.out.println("========[CleanSessionMap]========");
                        }

                        public void cancelled() {
                            System.out.println("Cancel session-cleaning ");
                        }

                    });


        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        System.out.println("========[CleanSessionMap End]========");
    }



}
