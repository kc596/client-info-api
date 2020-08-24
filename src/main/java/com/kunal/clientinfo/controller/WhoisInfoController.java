package com.kunal.clientinfo.controller;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.apache.commons.net.whois.WhoisClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@RestController
@RequestMapping("/whoisinfo")
public class WhoisInfoController {

    private Map<String, String> whoisServerMap = new HashMap<>();

    @RequestMapping(method = RequestMethod.POST, path = "/bulk")
    public ResponseEntity<List<String>> whoisInfoBulk(@RequestBody String[] urls) {
        List<String> answer = new ArrayList<>();

        Observable.fromArray(urls)
                .map(this::getDomainFromUrl)
                .flatMap(domain -> createWhoisClient()
                        .map(whoisClient -> calculateWhoisInfo(whoisClient, domain).orElse(""))
                        .repeat()
                        .takeUntil(whoisInfoStr -> {
                            if (whoisInfoStr.contains("refer:")) {
                                System.out.println("refer is present: "+domain);
                                whoisServerMap.put(getTldFromDomain(domain), WhoisClient.DEFAULT_HOST); // TODO: parse refer
                                return false;
                            } else {
                                answer.add(whoisInfoStr);
                            }
                            return true;
                        }))
                //.observeOn(Schedulers.computation())
                .doOnError(Throwable::printStackTrace)
                .subscribe();

        return ResponseEntity.ok(answer);
    }

    Optional<String> calculateWhoisInfo(WhoisClient whoisClient, final String domain) {
        try {
            whoisClient.connect(getWhoisServer(domain));
            String result = whoisClient.query(domain);
            try { whoisClient.disconnect(); } catch (Exception ignored) {}
            return Optional.of(result);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private Observable<WhoisClient> createWhoisClient() {
        WhoisClient whoisClient = new WhoisClient();
        whoisClient.setConnectTimeout(2000);
        return Observable.fromOptional(Optional.of(whoisClient));
    }

    private String getDomainFromUrl(String urlStr) {
        try {
            URL url = new URL(urlStr);
            return url.getHost();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return urlStr;
        }
    }

    private String getTldFromDomain(String domain) {
        // WARNING: use only when refer is present in whois info
        int lastDot = domain.lastIndexOf(".");
        while (lastDot >= 0) {
            String tld = domain.substring(lastDot);
            String newWhoisServer = whoisServerMap.get(tld);
            if (Objects.nonNull(newWhoisServer)) {
                lastDot = domain.substring(0, lastDot).lastIndexOf(".");
            } else {
                return tld;
            }
        }
        throw new RuntimeException("Invalid domain: "+domain);
    }

    private String getWhoisServer(String domain) {
        String whoisServer = "whois.iana.org";
        int lastDot = domain.lastIndexOf(".");
        while (lastDot > 0) {
            String tld = domain.substring(lastDot);
            String newWhoisServer = whoisServerMap.get(tld);
            if (Objects.nonNull(newWhoisServer)) {
                whoisServer = newWhoisServer;
                lastDot = domain.substring(0, lastDot).lastIndexOf(".");
            } else {
                break;
            }
        }
        return whoisServer;
    }
}
