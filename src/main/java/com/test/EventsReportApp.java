package com.test;

import com.michaelwitbrock.jacksonstream.JsonArrayStreamDataSupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.*;
import java.util.List;
import java.util.stream.Stream;


public class EventsReportApp {

    private static final Logger logger = LoggerFactory.getLogger(EventsReportApp.class);
    private static ReportGenerator report_generator = new ReportGenerator();

    private static String db_name = "events_db";
    private static String drop_table_sql = "DROP TABLE IF EXISTS events_report;";
    private static String create_table_sql = "CREATE TABLE IF NOT EXISTS events_report (" +
            "event_id VARCHAR(256) NOT NULL, " +
            "duration INT NOT NULL, " +
            "host VARCHAR(256), " +
            "type VARCHAR(256), " +
            "alert BOOLEAN, " +
            "PRIMARY KEY (event_id));";

    private static String insert_sql = "INSERT INTO events_report VALUES (?, ?, ?, ?, ?);";

    private static Connection connection = null;
    ResultSet resultSet = null;
    private static Statement statement = null;

    public static void main(String[] args) {
        logger.info("Starting app...");
        File input_file;

        try {
            input_file = getInputFile(args);
            logger.info("Using input file {}", input_file.getAbsoluteFile());
            getDbConnection();

            //Usage of jacksonstream third party library provides ability
            // to leverage power of Java 8 Streams API and
            // allows to load and parse large JSON files keeping relatively small memory usage
            Stream<EventEntry> event_entries = new JsonArrayStreamDataSupplier(input_file, EventEntry.class).getStream();
            logger.info("Input file parsed succesfully");
            List<EventSummary> summary = report_generator.createEventDurationReport(event_entries);
            logger.info("Report with {} entries created", summary.size());
            PreparedStatement pstmt = connection.prepareStatement(insert_sql);
            summary.forEach(
                    event -> {
                        try {

                            pstmt.setString(1, event.getId());
                            pstmt.setInt(2, event.getDuration());
                            pstmt.setString(3, event.getHost());
                            pstmt.setString(4, event.getType());
                            pstmt.setBoolean(5, event.getAlert());
                            pstmt.executeUpdate();

                            logger.debug("{} written to db", event);
                        } catch (SQLException e) {
                            logger.warn("Exception during writing to db: ", e);
                        }
                    }
            );
            logger.info("Report written to DB");
            connection.close();

        } catch (InputException e) {
            logger.error("Got Exception: ", e);
            logger.info("Please provide valid input file as a first argument");
        } catch (SQLException e) {
            logger.error("Got DB Exception: ", e);
        }

    }


    private static File getInputFile(String[] args) throws InputException {
        File input;
        if (args.length == 0) throw new InputException("Input file not provided!");
        input = new File(args[0]);
        if (!input.exists() || !input.isFile())
            throw new InputException("Input file " + input.getAbsolutePath() + " does not exists!");
        return input;
    }

    private static void getDbConnection() {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            connection = DriverManager.getConnection(
                    "jdbc:hsqldb:file:./" + db_name, "SA", "");
            logger.info("Connection to database " + db_name + " established sucessfully.");
            statement = connection.createStatement();
            statement.executeUpdate(drop_table_sql);
            statement.executeUpdate(create_table_sql);

        } catch (SQLException e) {
            logger.error("Got Exception: ", e);
        } catch (ClassNotFoundException e) {
            logger.error("Error getting hsqldb driver", e);
        }
    }


}
