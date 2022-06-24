package uz.exadel.telegramwebhook.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/telegram")
@Slf4j
public class BotController {

    String TOKEN = "5311954887:AAF46Q9pIPVFbysk6-Ff7-CICkGSjzolgEw";

    @GetMapping
    public void getTelegramRequest(@RequestBody Object object){
        log.info("Request has come");
    }
}
