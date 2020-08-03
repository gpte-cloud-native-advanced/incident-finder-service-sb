package com.redhat.emergency.response.incident.finder;

import java.math.BigDecimal;
import java.util.List;

import com.redhat.emergency.response.incident.finder.model.Incident;
import com.redhat.emergency.response.incident.finder.model.Mission;
import com.redhat.emergency.response.incident.finder.model.ResponderLocationHistory;
import com.redhat.emergency.response.incident.finder.service.IncidentService;
import com.redhat.emergency.response.incident.finder.service.MissionService;
import com.redhat.emergency.response.incident.finder.service.ShelterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/incidents")
public class IncidentController {

    @Autowired
    IncidentService incidentService;

    @Autowired
    MissionService missionService;

    @Autowired
    ShelterService shelterService;

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Incident>> getIncidents(@RequestParam String name) {

        List<Incident> incidents = incidentService.incidentsByName(name);
        incidents.forEach(i -> {
            Mission mission = missionService.missionByIncidentId(i.getId());
            if (mission != null) {
                i.setDestinationLat(mission.getDestinationLat());
                i.setDestinationLon(mission.getDestinationLong());
                i.setDestinationName(shelterService.shelterName(mission.getDestinationLat(), mission.getDestinationLong()));
                List<ResponderLocationHistory> locationHistory = mission.getResponderLocationHistory();
                if (locationHistory != null && !locationHistory.isEmpty()) {
                    ResponderLocationHistory responderLocation = locationHistory.get(locationHistory.size() - 1);
                    i.setCurrentPositionLat(responderLocation.getLat());
                    i.setCurrentPositionLon(responderLocation.getLon());
                } else {
                    i.setCurrentPositionLat(new BigDecimal(i.getLat()));
                    i.setCurrentPositionLon(new BigDecimal(i.getLon()));
                }
            }
        });

        return new ResponseEntity<>(incidents, HttpStatus.OK);
    }
}
