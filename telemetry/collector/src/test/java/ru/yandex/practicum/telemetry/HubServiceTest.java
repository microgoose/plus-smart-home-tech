package ru.yandex.practicum.telemetry;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.enums.DeviceType;
import ru.yandex.practicum.telemetry.enums.HubEventType;
import ru.yandex.practicum.telemetry.model.hub.DeviceAddedEvent;
import ru.yandex.practicum.telemetry.model.hub.DeviceRemovedEvent;
import ru.yandex.practicum.telemetry.model.hub.ScenarioAddedEvent;
import ru.yandex.practicum.telemetry.model.hub.ScenarioRemovedEvent;
import ru.yandex.practicum.telemetry.service.HubService;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HubServiceTest {

    @Mock
    private KafkaTemplate<String, HubEventAvro> kafkaTemplate;

    @InjectMocks
    private HubService hubService;

    @Test
    void collectHubEvent_shouldAddSensorToHub() {
        DeviceAddedEvent event = new DeviceAddedEvent("sensor.light.1", "hub.12345", DeviceType.LIGHT_SENSOR);

        CompletableFuture<SendResult<String, HubEventAvro>> futureMock = CompletableFuture.completedFuture(null);
        when(kafkaTemplate.send(anyString(), anyString(), any(HubEventAvro.class)))
                .thenReturn(futureMock);

        hubService.collectHubEvent(event);

        ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<HubEventAvro> messageCaptor = ArgumentCaptor.forClass(HubEventAvro.class);

        verify(kafkaTemplate, times(1))
                .send(topicCaptor.capture(), keyCaptor.capture(), messageCaptor.capture());

        assertEquals("hub.12345", messageCaptor.getValue().getHubId());
        assertEquals(HubEventType.DEVICE_ADDED.name(), keyCaptor.getValue());
    }

    @Test
    void collectHubEvent_shouldRemoveSensorFromHub() {
        DeviceRemovedEvent event = new DeviceRemovedEvent("sensor.light.2", "hub.67890");

        CompletableFuture<SendResult<String, HubEventAvro>> futureMock = CompletableFuture.completedFuture(null);
        when(kafkaTemplate.send(anyString(), anyString(), any(HubEventAvro.class)))
                .thenReturn(futureMock);

        hubService.collectHubEvent(event);

        ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<HubEventAvro> messageCaptor = ArgumentCaptor.forClass(HubEventAvro.class);

        verify(kafkaTemplate, times(1))
                .send(topicCaptor.capture(), keyCaptor.capture(), messageCaptor.capture());

        assertEquals("hub.67890", messageCaptor.getValue().getHubId());
        assertEquals(HubEventType.DEVICE_REMOVED.name(), keyCaptor.getValue());
    }

    @Test
    void collectHubEvent_shouldAddScenarioToHub() {
        ScenarioAddedEvent event = new ScenarioAddedEvent("hub.11111", "Lights On",
                Collections.emptyList(), Collections.emptyList());

        CompletableFuture<SendResult<String, HubEventAvro>> futureMock = CompletableFuture.completedFuture(null);
        when(kafkaTemplate.send(anyString(), anyString(), any(HubEventAvro.class)))
                .thenReturn(futureMock);

        hubService.collectHubEvent(event);

        ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<HubEventAvro> messageCaptor = ArgumentCaptor.forClass(HubEventAvro.class);

        verify(kafkaTemplate, times(1))
                .send(topicCaptor.capture(), keyCaptor.capture(), messageCaptor.capture());

        assertEquals("hub.11111", messageCaptor.getValue().getHubId());
        assertEquals(HubEventType.SCENARIO_ADDED.name(), keyCaptor.getValue());
    }

    @Test
    void collectHubEvent_shouldRemoveScenarioFromHub() {
        ScenarioRemovedEvent event = new ScenarioRemovedEvent("hub.22222", "hub.22222");

        CompletableFuture<SendResult<String, HubEventAvro>> futureMock = CompletableFuture.completedFuture(null);
        when(kafkaTemplate.send(anyString(), anyString(), any(HubEventAvro.class)))
                .thenReturn(futureMock);

        hubService.collectHubEvent(event);

        ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<HubEventAvro> messageCaptor = ArgumentCaptor.forClass(HubEventAvro.class);

        verify(kafkaTemplate, times(1))
                .send(topicCaptor.capture(), keyCaptor.capture(), messageCaptor.capture());

        assertEquals("hub.22222", messageCaptor.getValue().getHubId());
        assertEquals(HubEventType.SCENARIO_REMOVED.name(), keyCaptor.getValue());
    }
}
