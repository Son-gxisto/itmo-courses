package ru.itmo.wp.model.service;

import ru.itmo.wp.model.domain.Event;
import ru.itmo.wp.model.repository.EventRepository;
import ru.itmo.wp.model.repository.impl.EventRepositoryImpl;

public class EventService {
    private final EventRepository eventRepository = new EventRepositoryImpl();
    public void saveEvent(Enum type, long userId) {
        Event event = new Event();
        event.setEventType(type);
        event.setUserId(userId);
        eventRepository.save(event);
    }
}
