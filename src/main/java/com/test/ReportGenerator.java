package com.test;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.abs;

public class ReportGenerator {
    private static final Logger logger = LoggerFactory.getLogger(ReportGenerator.class);

    /**
     * Takes input stream of event entries and created summary report for each unique event
     * @param event_entries
     * @return List of individual events
     */
    public static List<EventSummary> createEventDurationReport(Stream<EventEntry> event_entries) {

        //Parallelisation of input stream should allow to utilize multiple cores
        Map<String, List<EventEntry>> events = event_entries.
                parallel()
                .collect( Collectors.groupingBy(EventEntry::getId));
        List<EventSummary> events_summary_list = new ArrayList<EventSummary>();

        events.forEach(
            (id, e) -> {
                if(e.size() != 2) {
                    logger.warn("Unexpected number of events {} for id {}", e.size(), id);
                    return;
                }
                EventEntry event_entry = e.get(0);
                int duration = (int)abs(e.get(0).getTimestamp()-e.get(1).getTimestamp());
                boolean alert = false;
                if(duration > 4) alert = true;

                EventSummary es = new EventSummary();
                es.setId(id);
                es.setDuration(duration);
                es.setHost(event_entry.getHost());
                es.setType(event_entry.getType());
                es.setAlert(alert);
                logger.debug("Adding to report: " + es);
                events_summary_list.add(es);
            }
        );

        return events_summary_list;

    }


}
