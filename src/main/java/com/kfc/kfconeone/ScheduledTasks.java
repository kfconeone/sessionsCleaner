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
    public void reportCurrentTime() {
//        String fileName = DateTimeFormatter.ofPattern("yyyyMMdd_hh:mm:ss").format(ZonedDateTime.now(ZoneOffset.UTC).plusHours(8));
        log.info("現在時間： {}", DateTimeFormatter.ofPattern("yyyyMMdd_hh:mm:ss").format(ZonedDateTime.now(ZoneOffset.UTC).plusHours(8)));
        try
        {
            Unirest.get("http://localhost:8081/CleanSessionMap")
                    .asJsonAsync(new Callback<>() {

                        public void failed(UnirestException e) {
                            System.out.println("sessions清除失敗");
                            e.printStackTrace();
                        }

                        public void completed(HttpResponse<JsonNode> response) {
                            int code = response.getStatus();
                            JsonNode body = response.getBody();
                            System.out.println(String.format("code : %d , body : %s",code,body));

                        }

                        public void cancelled() {
                            System.out.println("取消清除sessions");
                        }

                    });


        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }



}
