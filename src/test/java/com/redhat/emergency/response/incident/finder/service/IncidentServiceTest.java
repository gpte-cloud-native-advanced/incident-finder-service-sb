package com.redhat.emergency.response.incident.finder.service;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import java.util.List;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.redhat.emergency.response.incident.finder.model.Incident;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.util.ReflectionTestUtils;

public class IncidentServiceTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(WireMockConfiguration.wireMockConfig().dynamicPort());

    private IncidentService service;

    @Before
    public void beforeTest() {
        service = new IncidentService(new RestTemplateBuilder());
        ReflectionTestUtils.setField(service, "serviceScheme", "http", null);
        ReflectionTestUtils.setField(service, "serviceUrl", "localhost:" + wireMockRule.port(), null);
        ReflectionTestUtils.setField(service, "servicePath", "/incidents/byname/{name}", null);
    }

    @Test
    public void testIncidentService() {
        String json = "[{\"id\":\"incident123\",\"lat\":\"30.12345\",\"lon\":\"-77.98765\",\"medicalNeeded\":true," +
                "\"numberOfPeople\": 5,\"victimName\":\"John Doe\",\"victimPhoneNumber\":\"123-456-789\"," +
                "\"timestamp\":123456789,\"status\":\"REPORTED\"}," +
                "{\"id\":\"incident456\",\"lat\":\"30.456789\",\"lon\":\"-77.654321\",\"medicalNeeded\":true," +
                "\"numberOfPeople\": 6,\"victimName\":\"John Doo\",\"victimPhoneNumber\":\"222-333-444\"," +
                "\"timestamp\":123456788,\"status\":\"ASSIGNED\"}]";

        stubFor(get(urlEqualTo("/incidents/byname/%25John%20D%25")).willReturn(
                aResponse().withStatus(200).withHeader("Content-type", "application/json")
                .withBody(json)));

        List<Incident> incidents = service.incidentsByName("John D");
        assertThat(incidents, notNullValue());
        assertThat(incidents.size(), equalTo(2));
        verify(getRequestedFor(urlEqualTo("/incidents/byname/%25John%20D%25")));
    }

}
