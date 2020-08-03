package com.redhat.emergency.response.incident.finder.service;

import com.redhat.emergency.response.incident.finder.model.Mission;
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
public class MissionService {

    private static final Logger log = LoggerFactory.getLogger(MissionService.class);

    @Value("${mission.service.scheme}")
    private String serviceScheme;

    @Value("${mission.service.url}")
    private String serviceUrl;

    @Value("${mission.service.path}")
    private String servicePath;

    private final RestTemplate restTemplate;

    @Autowired
    public MissionService(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.errorHandler(new RestTemplateResponseErrorHandler()).build();
    }

    public Mission missionByIncidentId(String incidentId) {
        try {
            UriComponents uriComponents = UriComponentsBuilder.newInstance().scheme(serviceScheme)
                    .host(serviceUrl).path(servicePath).build();
            ResponseEntity<Mission> entity = restTemplate.exchange(uriComponents.toUriString(),
                    HttpMethod.GET, null, new ParameterizedTypeReference<Mission>(){}, incidentId);
            return entity.getBody();
        } catch (NotFoundException nfe) {
            log.warn("Not Found Exception when calling mission service");
            return null;
        } catch (HttpClientErrorException hce) {
            log.error("Http Exception when calling mission service - status code = " + hce.getStatusCode(), hce);
            return null;
        }
    }

}
