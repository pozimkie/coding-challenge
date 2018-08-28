package com.test;

import org.junit.Before;
import org.junit.Test;


import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MyTests {


    private ReportGenerator generator;

    private static EventEntry entry1 = new EventEntry();
    private static EventEntry entry2 = new EventEntry();
    private static EventEntry entry3 = new EventEntry();
    private static EventEntry entry4 = new EventEntry();
    private static EventEntry entry5 = new EventEntry();
    private static EventEntry entry6 = new EventEntry();

    private static EventSummary es1 = new EventSummary();
    private static EventSummary es2 = new EventSummary();
    private static EventSummary es3 = new EventSummary();

    @Before
    public void setUp() {

        entry1.setId("scsmbstgra");
        entry1.setState("STARTED");
        entry1.setTimestamp(1491377495212L);
        entry1.setType("APPLICATION_LOG");
        entry1.setHost("12345");

        entry2.setId("scsmbstgrb");
        entry2.setState("STARTED");
        entry2.setTimestamp(1491377495213L);

        entry3.setId("scsmbstgrc");
        entry3.setState("FINISHED");
        entry3.setTimestamp(1491377495218L);

        entry4.setId("scsmbstgra");
        entry4.setState("FINISHED");
        entry4.setTimestamp(1491377495217L);
        entry4.setType("APPLICATION_LOG");
        entry4.setHost("12345");

        entry5.setId("scsmbstgrc");
        entry5.setState("FINISHED");
        entry5.setTimestamp(1491377495210L);

        entry6.setId("scsmbstgrb");
        entry6.setState("FINISHED");
        entry6.setTimestamp(1491377495216L);

        es1.setId(entry1.getId());
        es1.setDuration((int)(entry4.getTimestamp()-entry1.getTimestamp()));
        es1.setAlert(true);
        es1.setType("APPLICATION_LOG");
        es1.setHost("12345");

        es2.setId(entry2.getId());
        es2.setDuration((int)(entry6.getTimestamp()-entry2.getTimestamp()));
        es2.setAlert(false);

        es3.setId(entry3.getId());
        es3.setDuration((int)(entry3.getTimestamp()-entry5.getTimestamp()));
        es3.setAlert(true);

    }

    @Test
    public void testReportCompleteData() {

        List<EventEntry> entries =  Arrays.asList(entry1, entry2, entry3, entry4, entry5, entry6);
        List<EventSummary> expected =  Arrays.asList(es1,es2,es3);
        List<EventSummary> report = generator.createEventDurationReport(entries.stream());

        assertEquals(report, expected);
    }

    @Test
    public void testReportIncompleteData() {

        List<EventEntry> entries =  Arrays.asList(entry1, entry2, entry3, entry4, entry6);
        List<EventSummary> expected =  Arrays.asList(es1,es2);
        List<EventSummary> report = generator.createEventDurationReport(entries.stream());

        assertEquals(report, expected);
    }

    @Test
    public void testReportEmptyInput() {

        List<EventEntry> entries =  Arrays.asList();
        List<EventSummary> expected =  Arrays.asList();
        List<EventSummary> report = generator.createEventDurationReport(entries.stream());

        assertEquals(report, expected);
    }

}
