package com.kunal.clientinfo.service;

import com.google.common.net.InternetDomainName;
import com.kunal.clientinfo.config.ApplicationConfig;
import io.reactivex.rxjava3.core.Observable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.whois.WhoisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@Service
public class WhoisInfoService {
    private final ApplicationConfig config;

    private Map<String, String> whoisServerMap = new HashMap<>();

    @Autowired
    public WhoisInfoService(ApplicationConfig config) {
        this.config = config;
    }

    public List<String> getWhoisInfoOfUrls(String[] urls) {
        List<String> answer = new ArrayList<>();

        //noinspection ResultOfMethodCallIgnored
        Observable.fromArray(urls)
                .map(this::getDomainFromUrl)
                .map(InternetDomainName::from)
                .map(InternetDomainName::topPrivateDomain)
                .map(InternetDomainName::toString)
                .map(this::calculateWhoisInfo)
                .map(whoisInfoOptional -> whoisInfoOptional.orElse("Error!"))
                .doOnError(Throwable::printStackTrace)
                .subscribe(answer::add);

        return answer;
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

    private Optional<String> calculateWhoisInfo(final String domain) {
        try {
            boolean whoisFound = false;
            WhoisClient whoisClient = createWhoisClient();
            String result = null;
            do {
                whoisClient.connect(getWhoisServer(domain));
                result = whoisClient.query(domain);
                Optional<String> whoisRefer = getWhoisRefer(result);
                if (whoisRefer.isPresent()) {
                    String nextWhoisServer = whoisRefer.get();
                    whoisServerMap.put(getNextTldInDomain(domain), nextWhoisServer);
                } else {
                    whoisFound = true;
                }
            } while (!whoisFound);
            try { whoisClient.disconnect(); } catch (Exception ignored) {}
            return Optional.of(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private WhoisClient createWhoisClient() {
        WhoisClient whoisClient = new WhoisClient();
        whoisClient.setConnectTimeout(2000);
        return whoisClient;
    }

    private String getWhoisServer(String domain) {
        String whoisServer = config.getDefaultWhoisHost();
        int lastDot = domain.lastIndexOf(".");
        while (lastDot > 0) {
            String tld = domain.substring(lastDot);
            String newWhoisServer = whoisServerMap.get(tld);
            if (Objects.nonNull(newWhoisServer) && !newWhoisServer.isBlank()) {
                whoisServer = newWhoisServer;
                lastDot = domain.substring(0, lastDot).lastIndexOf(".");
            } else {
                break;
            }
        }
        return whoisServer;
    }

    private Optional<String> getWhoisRefer(String whoisInfo) {
        if (Objects.isNull(whoisInfo) || whoisInfo.isBlank()) {
            return Optional.empty();
        }
        return whoisInfo.lines()
                .filter(str -> Objects.nonNull(str) && StringUtils.startsWithIgnoreCase(str,"refer:"))
                .findFirst()
                .map(s -> s.substring("refer:".length()).trim())
                .filter(this::isFormOfDomain);
    }
    private boolean isFormOfDomain(String domain) {
        if (Objects.isNull(domain) || domain.isBlank()) { return false; }
        try {
            URL url = new URL("http://"+domain);
            return url.getHost().equals(domain);
        } catch (Exception e) {
            return false;
        }
    }

    private String getNextTldInDomain(String domain) {
        int lastDot = domain.lastIndexOf(".");
        while (lastDot >= 0) {
            String tld = domain.substring(lastDot);
            String newWhoisServer = whoisServerMap.get(tld);
            if (Objects.nonNull(newWhoisServer) && !newWhoisServer.isBlank()) {
                lastDot = domain.substring(0, lastDot).lastIndexOf(".");
            } else {
                return tld;
            }
        }
        throw new RuntimeException("Invalid domain: "+domain);
    }
}
