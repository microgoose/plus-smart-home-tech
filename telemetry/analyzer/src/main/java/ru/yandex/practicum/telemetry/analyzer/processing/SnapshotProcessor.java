package ru.yandex.practicum.telemetry.analyzer.processing;

import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.analyzer.service.snapshot.SnapshotEventService;

import java.util.List;

@Component
@Slf4j
public class SnapshotProcessor implements EventProcessor<SensorsSnapshotAvro> {

    private final SnapshotEventService snapshotEventService;
    private final HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient;

    @Autowired
    public SnapshotProcessor(@GrpcClient("hub-router")
                             HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient,
                             SnapshotEventService snapshotEventService) {
        this.snapshotEventService = snapshotEventService;
        this.hubRouterClient = hubRouterClient;
    }

    @Override
    public void process(ConsumerRecord<String, SensorsSnapshotAvro> record) {
        SensorsSnapshotAvro event = record.value();
        log.debug("Получен снапшот: {}", event);

        try {
            List<DeviceActionRequest> actions = snapshotEventService.makeRequestsFromSnapshot(event);

            if (actions.isEmpty()) {
                log.debug("Снапшот обработан, отправка действий не требуются: {}", event);
            } else {
                log.debug("Снапшот обработан, будет отправлено действий: {}", actions.size());
                actions.forEach(hubRouterClient::handleDeviceAction);
            }
        } catch (Exception ex) {
            log.error("Ошибка при обработке события снапшота", ex);
        }
    }

}
