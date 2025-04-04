package ru.yandex.practicum.telemetry.collector;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HubServiceTest {

//    @Mock
//    private KafkaTemplate<String, HubEventAvro> kafkaTemplate;
//
//    @InjectMocks
//    private HubService hubService;
//
//    @Test
//    void collectHubEvent_shouldAddSensorToHub() {
//        DeviceAddedEvent event = new DeviceAddedEvent("sensor.light.1", "hub.12345", DeviceType.LIGHT_SENSOR);
//
//        CompletableFuture<SendResult<String, HubEventAvro>> futureMock = CompletableFuture.completedFuture(null);
//        when(kafkaTemplate.send(anyString(), anyString(), any(HubEventAvro.class)))
//                .thenReturn(futureMock);
//
//        hubService.collectHubEvent(event);
//
//        ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
//        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
//        ArgumentCaptor<HubEventAvro> messageCaptor = ArgumentCaptor.forClass(HubEventAvro.class);
//
//        verify(kafkaTemplate, times(1))
//                .send(topicCaptor.capture(), keyCaptor.capture(), messageCaptor.capture());
//
//        assertEquals("hub.12345", messageCaptor.getValue().getHubId());
//        Assertions.assertEquals(HubEventType.DEVICE_ADDED.name(), keyCaptor.getValue());
//    }
//
//    @Test
//    void collectHubEvent_shouldRemoveSensorFromHub() {
//        DeviceRemovedEvent event = new DeviceRemovedEvent("sensor.light.2", "hub.67890");
//
//        CompletableFuture<SendResult<String, HubEventAvro>> futureMock = CompletableFuture.completedFuture(null);
//        when(kafkaTemplate.send(anyString(), anyString(), any(HubEventAvro.class)))
//                .thenReturn(futureMock);
//
//        hubService.collectHubEvent(event);
//
//        ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
//        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
//        ArgumentCaptor<HubEventAvro> messageCaptor = ArgumentCaptor.forClass(HubEventAvro.class);
//
//        verify(kafkaTemplate, times(1))
//                .send(topicCaptor.capture(), keyCaptor.capture(), messageCaptor.capture());
//
//        assertEquals("hub.67890", messageCaptor.getValue().getHubId());
//        assertEquals(HubEventType.DEVICE_REMOVED.name(), keyCaptor.getValue());
//    }
//
//    @Test
//    void collectHubEvent_shouldAddScenarioToHub() {
//        ScenarioAddedEvent event = new ScenarioAddedEvent("hub.11111", "Lights On",
//                Collections.emptyList(), Collections.emptyList());
//
//        CompletableFuture<SendResult<String, HubEventAvro>> futureMock = CompletableFuture.completedFuture(null);
//        when(kafkaTemplate.send(anyString(), anyString(), any(HubEventAvro.class)))
//                .thenReturn(futureMock);
//
//        hubService.collectHubEvent(event);
//
//        ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
//        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
//        ArgumentCaptor<HubEventAvro> messageCaptor = ArgumentCaptor.forClass(HubEventAvro.class);
//
//        verify(kafkaTemplate, times(1))
//                .send(topicCaptor.capture(), keyCaptor.capture(), messageCaptor.capture());
//
//        assertEquals("hub.11111", messageCaptor.getValue().getHubId());
//        assertEquals(HubEventType.SCENARIO_ADDED.name(), keyCaptor.getValue());
//    }
//
//    @Test
//    void collectHubEvent_shouldRemoveScenarioFromHub() {
//        ScenarioRemovedEvent event = new ScenarioRemovedEvent("hub.22222", "hub.22222");
//
//        CompletableFuture<SendResult<String, HubEventAvro>> futureMock = CompletableFuture.completedFuture(null);
//        when(kafkaTemplate.send(anyString(), anyString(), any(HubEventAvro.class)))
//                .thenReturn(futureMock);
//
//        hubService.collectHubEvent(event);
//
//        ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
//        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
//        ArgumentCaptor<HubEventAvro> messageCaptor = ArgumentCaptor.forClass(HubEventAvro.class);
//
//        verify(kafkaTemplate, times(1))
//                .send(topicCaptor.capture(), keyCaptor.capture(), messageCaptor.capture());
//
//        assertEquals("hub.22222", messageCaptor.getValue().getHubId());
//        assertEquals(HubEventType.SCENARIO_REMOVED.name(), keyCaptor.getValue());
//    }
}
