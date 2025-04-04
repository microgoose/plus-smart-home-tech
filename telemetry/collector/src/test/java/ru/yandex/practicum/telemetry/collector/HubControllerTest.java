package ru.yandex.practicum.telemetry.collector;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import ru.yandex.practicum.telemetry.collector.controller.HubController;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(HubController.class)
class HubControllerTest {

//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private SensorService sensorService;
//
//    @MockBean
//    private HubService hubService;
//
//    /*** Тесты для /events/sensors ***/
//
//    @Test
//    void collectLightSensorEvent_shouldReturn200() throws Exception {
//        String json = """
//                {
//                  "id": "sensor.light.3",
//                  "hubId": "hub-2",
//                  "timestamp": "2024-08-06T16:54:03.129Z",
//                  "type": "LIGHT_SENSOR_EVENT",
//                  "linkQuality": 75,
//                  "luminosity": 59
//                }
//                """;
//
//        doNothing().when(sensorService).collectSensorEvent(Mockito.any(SensorEvent.class));
//
//        mockMvc.perform(post("/events/sensors")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void collectMotionSensorEvent_shouldReturn200() throws Exception {
//        String json = """
//                {
//                  "id": "sensor.motion.1",
//                  "hubId": "hub-2",
//                  "timestamp": "2024-08-06T17:00:00.000Z",
//                  "type": "MOTION_SENSOR_EVENT",
//                  "linkQuality": 85,
//                  "motion": true,
//                  "voltage": 3600
//                }
//                """;
//
//        doNothing().when(sensorService).collectSensorEvent(Mockito.any(SensorEvent.class));
//
//        mockMvc.perform(post("/events/sensors")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void collectSwitchSensorEvent_shouldReturn200() throws Exception {
//        String json = """
//                {
//                  "id": "sensor.switch.4356",
//                  "hubId": "hub-1",
//                  "timestamp": "2024-08-06T17:54:03.129Z",
//                  "type": "SWITCH_SENSOR_EVENT",
//                  "state": true
//                }
//                """;
//
//        doNothing().when(sensorService).collectSensorEvent(Mockito.any(SensorEvent.class));
//
//        mockMvc.perform(post("/events/sensors")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void collectTemperatureSensorEvent_shouldReturn200() throws Exception {
//        String json = """
//                {
//                  "id": "sensor.temp.9",
//                  "hubId": "hub-5",
//                  "timestamp": "2024-08-06T18:10:03.129Z",
//                  "type": "TEMPERATURE_SENSOR_EVENT",
//                  "temperatureC": 22,
//                  "temperatureF": 71
//                }
//                """;
//
//        doNothing().when(sensorService).collectSensorEvent(Mockito.any(SensorEvent.class));
//
//        mockMvc.perform(post("/events/sensors")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isOk());
//    }
//
//    /*** Тесты для /events/hubs ***/
//
//    @Test
//    void collectDeviceAddedEvent_shouldReturn200() throws Exception {
//        String json = """
//                {
//                  "hubId": "hub.12345",
//                  "timestamp": "2024-08-06T15:11:24.157Z",
//                  "type": "DEVICE_ADDED",
//                  "id": "sensor.light.3",
//                  "deviceType": "MOTION_SENSOR"
//                }
//                """;
//
//        doNothing().when(hubService).collectHubEvent(Mockito.any(HubEvent.class));
//
//        mockMvc.perform(post("/events/hubs")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void collectScenarioAddedEvent_shouldReturn200() throws Exception {
//        String json = """
//                {
//                  "hubId": "hub.789",
//                  "timestamp": "2024-08-06T16:45:03.129Z",
//                  "type": "SCENARIO_ADDED",
//                  "name": "Night Mode",
//                  "conditions": [
//                    {
//                      "sensorId": "sensor.motion.1",
//                      "type": "MOTION",
//                      "operation": "EQUALS",
//                      "value": 1
//                    }
//                  ],
//                  "actions": [
//                    {
//                      "sensorId": "sensor.light.3",
//                      "type": "ACTIVATE"
//                    }
//                  ]
//                }
//                """;
//
//        doNothing().when(hubService).collectHubEvent(Mockito.any(HubEvent.class));
//
//        mockMvc.perform(post("/events/hubs")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isOk());
//    }
//
//    /*** Тесты на ошибки ***/
//
//    @Test
//    void collectSensorEvent_missingRequiredField_shouldReturn400() throws Exception {
//        String json = """
//                {
//                  "hubId": "hub-1",
//                  "timestamp": "2024-08-06T17:54:03.129Z",
//                  "type": "SWITCH_SENSOR_EVENT"
//                }
//                """;
//
//        mockMvc.perform(post("/events/sensors")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void collectHubEvent_invalidType_shouldReturn400() throws Exception {
//        String json = """
//                {
//                  "hubId": "hub.12345",
//                  "timestamp": "2024-08-06T15:11:24.157Z",
//                  "type": "UNKNOWN_EVENT",
//                  "id": "sensor.light.3",
//                  "deviceType": "MOTION_SENSOR"
//                }
//                """;
//
//        mockMvc.perform(post("/events/hubs")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isBadRequest());
//    }
}
