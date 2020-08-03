package com.redhat.emergency.response.incident.finder.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import com.redhat.emergency.response.incident.finder.model.Shelter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class ShelterService {

    private static final Logger log = LoggerFactory.getLogger(ShelterService.class);

    @Value("${disaster.service.scheme}")
    private String serviceScheme;

    @Value("${disaster.service.url}")
    private String serviceUrl;

    @Value("${disaster.service.path}")
    private String servicePath;

    private final RestTemplate restTemplate;

    @Autowired
    public ShelterService(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.errorHandler(new RestTemplateResponseErrorHandler()).build();
    }

    public String shelterName(BigDecimal lat, BigDecimal lon) {
        try {
            UriComponents uriComponents = UriComponentsBuilder.newInstance().scheme(serviceScheme)
                    .host(serviceUrl).path(servicePath).build();
            ResponseEntity<List<Shelter>> entity = restTemplate.exchange(uriComponents.toUriString(),
                    HttpMethod.GET, null, new ParameterizedTypeReference<List<Shelter>>(){});
            return Objects.requireNonNull(entity.getBody()).stream().filter(shelter -> shelter.getLat().equals(lat) && shelter.getLon().equals(lon))
                    .map(Shelter::getName).findFirst().orElse("");
        } catch (NotFoundException nfe) {
            log.warn("Not Found Exception when calling disaster service");
            return "";
        } catch (HttpClientErrorException hce) {
            log.error("Http Exception when calling disaster service - status code = " + hce.getStatusCode(), hce);
            return "";
        }
    }

}
