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

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.redhat.emergency.response.incident.finder.model.Mission;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.util.ReflectionTestUtils;

public class MissionServiceTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(WireMockConfiguration.wireMockConfig().dynamicPort());

    private MissionService service;

    @Before
    public void beforeTest() {
        service = new MissionService(new RestTemplateBuilder());
        ReflectionTestUtils.setField(service, "serviceScheme", "http", null);
        ReflectionTestUtils.setField(service, "serviceUrl", "localhost:" + wireMockRule.port(), null);
        ReflectionTestUtils.setField(service, "servicePath", "/api/missions/incident/{id}", null);
    }

    @Test
    public void testMissionService() {
        String json = "{\"id\":\"f5a9bc5e-408c-4f86-8592-6f67bb73c5fd\",\"incidentId\":\"5d9b2d3a-136f-414f-96ba-1b2a445fee5d\"," +
                "\"responderId\":\"64\",\"responderStartLat\":\"40.12345\",\"responderStartLong\":\"-80.98765\"," +
                "\"incidentLat\":\"30.12345\",\"incidentLong\":\"-70.98765\"," +
                "\"destinationLat\":\"50.12345\",\"destinationLong\":\"-90.98765\"," +
                "\"responderLocationHistory\":[{\"lat\":30.78452,\"lon\":-70.85252,\"timestamp\":1593872667576}," +
                "{\"lat\":30.78445,\"lon\":-70.85276,\"timestamp\":1593872667580}]," +
                "\"steps\":[],\"status\":\"CREATED\"}";;

        stubFor(get(urlEqualTo("/api/missions/incident/5d9b2d3a-136f-414f-96ba-1b2a445fee5d")).willReturn(
                aResponse().withStatus(200).withHeader("Content-type", "application/json")
                        .withBody(json)));

        Mission mission = service.missionByIncidentId("5d9b2d3a-136f-414f-96ba-1b2a445fee5d");
        assertThat(mission, notNullValue());
        assertThat(mission.getId(), equalTo("f5a9bc5e-408c-4f86-8592-6f67bb73c5fd"));
        assertThat(mission.getResponderLocationHistory(), notNullValue());
        assertThat(mission.getResponderLocationHistory().size(), equalTo(2));
        verify(getRequestedFor(urlEqualTo("/api/missions/incident/5d9b2d3a-136f-414f-96ba-1b2a445fee5d")));
    }
}
