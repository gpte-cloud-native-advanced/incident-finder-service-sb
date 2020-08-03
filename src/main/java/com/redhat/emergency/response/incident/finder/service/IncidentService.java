package com.redhat.emergency.response.incident.finder.service;

import java.util.Collections;
import java.util.List;

import com.redhat.emergency.response.incident.finder.model.Incident;
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
public class IncidentService {

    private static final Logger log = LoggerFactory.getLogger(IncidentService.class);

    @Value("${incident.service.scheme}")
    private String serviceScheme;

    @Value("${incident.service.url}")
    private String serviceUrl;

    @Value("${incident.service.path}")
    private String servicePath;

    private final RestTemplate restTemplate;

    @Autowired
    public IncidentService(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.errorHandler(new RestTemplateResponseErrorHandler()).build();
    }


    public List<Incident> incidentsByName(String name) {
        if (!name.startsWith("%")) {
            name = "%" + name;
        }
        if (!name.endsWith("%")) {
            name = name + "%";
        }
        try {
            UriComponents uriComponents = UriComponentsBuilder.newInstance().scheme(serviceScheme)
                    .host(serviceUrl).path(servicePath).build();
            ResponseEntity<List<Incident>> entity = restTemplate.exchange(uriComponents.toUriString(),
                    HttpMethod.GET, null, new ParameterizedTypeReference<List<Incident>>(){}, name);
            return entity.getBody();
        } catch (NotFoundException nfe) {
            log.warn("Not Found Exception when calling incident service");
            return Collections.emptyList();
        } catch (HttpClientErrorException hce) {
            log.error("Http Exception when calling incident service - status code = " + hce.getStatusCode(), hce);
            return Collections.emptyList();
        }
    }

}
