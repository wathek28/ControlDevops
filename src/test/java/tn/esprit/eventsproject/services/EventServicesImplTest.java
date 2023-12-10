package tn.esprit.eventsproject.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.eventsproject.entities.Event;
import tn.esprit.eventsproject.entities.Logistics;
import tn.esprit.eventsproject.entities.Participant;
import tn.esprit.eventsproject.repositories.EventRepository;
import tn.esprit.eventsproject.repositories.LogisticsRepository;
import tn.esprit.eventsproject.repositories.ParticipantRepository;

import java.time.LocalDate;
import java.util.*;

import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class EventServicesImplTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private LogisticsRepository logisticsRepository;

    @InjectMocks
    private EventServicesImpl eventServices;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddParticipant() {
        // Créez un participant pour le test
        Participant participant = new Participant();

        // Configurez le comportement simulé du repository pour save.
        when(participantRepository.save(participant)).thenReturn(participant);

        // Appelez la méthode à tester
        Participant result = eventServices.addParticipant(participant);

        // Vérifiez si la méthode save du repository a été appelée avec le participant en argument
        verify(participantRepository, times(1)).save(participant);

        // Vous pouvez ajouter d'autres assertions selon vos besoins.
        // Par exemple, vérifiez que le résultat renvoyé par la méthode est le même que le participant simulé.
        assertEquals(participant, result);

    }
    @Test
    public void testAddAffectEvenParticipant() {
        // Créez un participant et un événement pour le test
        Participant participant = new Participant();
        Event event = new Event();

        // Configurez le comportement simulé du repository pour findById.
        when(participantRepository.findById(anyInt())).thenReturn(Optional.of(participant));

        // Appelez la méthode à tester
        Event result = eventServices.addAffectEvenParticipant(event, 1);

        // Vérifiez si la méthode findById du repository a été appelée avec le bon argument
        verify(participantRepository, times(1)).findById(1);

        // Vérifiez si la méthode save du repository a été appelée avec le bon argument
        verify(eventRepository, times(1)).save(event);

        // Vous pouvez ajouter d'autres assertions selon vos besoins.
        // Par exemple, vérifiez que l'événement est correctement ajouté à la liste des événements du participant.
        Set<Event> participantEvents = participant.getEvents();
        assertEquals(1, participantEvents.size());
        assertTrue(participantEvents.contains(event));
    }


    @Test
    public void testAddAffectLog() {
        // Créez un événement et une logistique pour le test
        Event event = new Event();
        Logistics logistics = new Logistics();
        logistics.setDescription("Sample Logistics");

        // Configurez le comportement simulé du repository pour findByDescription.
        when(eventRepository.findByDescription("Sample Event")).thenReturn(event);

        // Appelez la méthode à tester
        Logistics result = eventServices.addAffectLog(logistics, "Sample Event");

        // Vérifiez si la méthode findByDescription du repository a été appelée avec le bon argument
        verify(eventRepository, times(1)).findByDescription("Sample Event");

        // Vérifiez si la méthode save du repository a été appelée avec la bonne logistique
        verify(logisticsRepository, times(1)).save(logistics);

        // Vous pouvez ajouter d'autres assertions selon vos besoins.
        // Par exemple, vérifiez que la logistique est correctement ajoutée à la liste des logistiques de l'événement.
        Set<Logistics> eventLogistics = event.getLogistics();
        assertEquals(1, eventLogistics.size());
        assertTrue(eventLogistics.contains(logistics));
    }

    @Test
    public void testGetLogisticsDates() {
        // Créez des événements pour le test
        Event event1 = new Event();
        Event event2 = new Event();
        Logistics logistics1 = new Logistics();
        Logistics logistics2 = new Logistics();

        // Configurez le comportement simulé du repository pour findByDateDebutBetween.
        when(eventRepository.findByDateDebutBetween(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(event1, event2));

        // Configurez le comportement simulé pour getLogistics() de chaque événement.
        Set<Logistics> logisticsSet1 = new HashSet<>();
        logisticsSet1.add(logistics1);
        event1.setLogistics(logisticsSet1);

        Set<Logistics> logisticsSet2 = new HashSet<>();
        logisticsSet2.add(logistics2);
        event2.setLogistics(logisticsSet2);

        // Appelez la méthode à tester
        List<Logistics> result = eventServices.getLogisticsDates(LocalDate.now(), LocalDate.now().plusDays(1));

        // Vérifiez si la méthode findByDateDebutBetween du repository a été appelée avec le bon argument
        verify(eventRepository, times(1)).findByDateDebutBetween(any(LocalDate.class), any(LocalDate.class));

        // Vous pouvez ajouter d'autres assertions selon vos besoins.
        // Par exemple, vérifiez que la liste de logistiques résultante contient les logistiques attendues.
        assertEquals(2, result.size());
        for (Logistics logistics : Arrays.asList(logistics1, logistics2)) {
            assertTrue(result.contains(logistics));
        }
    }



    }
