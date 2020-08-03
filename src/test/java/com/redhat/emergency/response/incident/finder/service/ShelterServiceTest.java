package com.redhat.emergency.response.incident.finder.service;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.math.BigDecimal;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.util.ReflectionTestUtils;

public class ShelterServiceTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(WireMockConfiguration.wireMockConfig().dynamicPort());

    private ShelterService service;

    @Before
    public void beforeTest() {
        service = new ShelterService(new RestTemplateBuilder());
        ReflectionTestUtils.setField(service, "serviceScheme", "http", null);
        ReflectionTestUtils.setField(service, "serviceUrl", "localhost:" + wireMockRule.port(), null);
        ReflectionTestUtils.setField(service, "servicePath", "/shelters", null);
    }

    @Test
    public void testGetShelters() {

        String json = "[{\"id\":\"1\",\"name\":\"Port City Marina\",\"lon\":-77.9519,\"lat\":34.2461,\"rescued\":0}, " +
                "{\"id\":\"2\",\"name\" :\"Wilmington Marine Center\",\"lon\" : -77.949,\"lat\" : 34.1706,\"rescued\":0}]";

        stubFor(get(urlEqualTo("/shelters")).willReturn(
                aResponse().withStatus(200).withHeader("Content-type", "application/json")
                        .withBody(json)));

        String shelter = service.shelterName(new BigDecimal("34.2461"), new BigDecimal("-77.9519"));
        assertThat(shelter, equalTo("Port City Marina"));
        verify(getRequestedFor(urlEqualTo("/shelters")));
    }

}
