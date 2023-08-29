import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<String[]> logs = new ArrayList<>();
        logs.add(new String[]{"11371", "TRUCK", "2023-08-29T15:00:00.000Z", "ENTRANCE" });
        logs.add(new String[]{"11353", "TRUCK", "2023-08-29T15:00:00.000Z", "ENTRANCE" });
        logs.add(new String[]{"11353", "TRUCK", "2023-08-01T23:00:00.000Z", "EXIT" });
        logs.add(new String[]{"11355", "TRUCK", "2023-08-01T20:00:00.000Z", "ENTRANCE" });
        logs.add(new String[]{"11355", "TRUCK", "2023-08-01T23:20:00.000Z", "EXIT" });
        logs.add(new String[]{"86453", "CAR", "2020-01-01T08:35:55.000Z", "ENTRANCE" });
        logs.add(new String[]{"86453", "CAR", "2020-01-01T12:14:03.000Z", "EXIT" });
        logs.add(new String[]{"648702", "MOTORCYCLE", "2020-01-01T16:03:07.000Z", "ENTRANCE" });
        logs.add(new String[]{"648702", "MOTORCYCLE", "2020-01-01T18:17:13.000Z", "EXIT" });

        int result = numberOfVehicles(logs);
        System.out.println("Number of vehicles that spent more than x hours: " + result);

        int nightVehicles = numberVehiclesAtNight(logs);
        System.out.println("Number of vehicles that spent the night: " + nightVehicles);

        Map<String, Integer> vehicleCounts = numberOfVehiclesCurrently(logs);
        System.out.println("Number of vehicles currently in the parking lot:");
        for (Map.Entry<String, Integer> entry : vehicleCounts.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        int busiestHour = busiestHour(logs);
        System.out.println("Busiest hour in the parking lot: " + busiestHour + ":00 - " + (busiestHour + 1) + ":00");

    }

    /*
    Read the log data, calculates the duration of each vehicle's stay, and compares it with the corresponding time
    limits to determine how many vehicles have spent more time than allowed in the parking lot.
     */
    public static int numberOfVehicles(List<String[]> logs) {
        // Vehicle type and corresponding time limit in hours
        Map<String, Integer> timeLimits = new HashMap<>();
        timeLimits.put("TRUCK", 3);
        timeLimits.put("MOTORCYCLE", 1);
        timeLimits.put("CAR", 2);

        // Vehicle ID to entrance timestamp mapping
        Map<String, Date> entranceMap = new HashMap<>();

        int count = 0;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        for (String[] log : logs) {
            String vehicleId = log[0];
            String vehicleType = log[1];
            String timestampStr = log[2];
            String action = log[3];

            try {
                Date timestamp = dateFormat.parse(timestampStr);

                if (action.equals("ENTRANCE")) {
                    entranceMap.put(vehicleId, timestamp);
                } else if (action.equals("EXIT")) {
                    if (entranceMap.containsKey(vehicleId)) {
                        Date entranceTime = entranceMap.get(vehicleId);
                        int timeLimit = timeLimits.get(vehicleType);

                        long durationInMillis = timestamp.getTime() - entranceTime.getTime();
                        int durationInHours = (int) (durationInMillis / (60 * 60 * 1000));

                        if (durationInHours > timeLimit) {
                            count++;
                        }

                        entranceMap.remove(vehicleId);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return count;
    }

    /*
    Iterates through the log entries, parses the timestamps, and checks whether the timestamp indicates that the
    vehicle's entrance or exit occurred during the night hours (from 11:00 PM to 6:00 AM).
     */
    public static int numberVehiclesAtNight(List<String[]> logs) {
        int nightStartHour = 23;
        int nightEndHour = 6;

        int count = 0;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        for (String[] log : logs) {
            String timestampStr = log[2];

            try {
                Date timestamp = dateFormat.parse(timestampStr);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(timestamp);

                int hour = calendar.get(Calendar.HOUR_OF_DAY);

                if (hour >= nightStartHour || hour < nightEndHour) {
                    count++;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return count;
    }


    /*
     Maintains a map where the keys are vehicle types ("TRUCK", "MOTORCYCLE", "CAR") and the values represent the
     current count of each vehicle type in the parking lot. The function iterates through the log entries, updates the
     counts based on the action (entrance or exit), and returns the map with the current vehicle counts.
     */
    public static Map<String, Integer> numberOfVehiclesCurrently(List<String[]> logs) {
        Map<String, Integer> vehicleCountMap = new HashMap<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        for (String[] log : logs) {
            String vehicleType = log[1];
            String timestampStr = log[2];
            String action = log[3];

            try {
                Date timestamp = dateFormat.parse(timestampStr);

                if (action.equals("ENTRANCE")) {
                    vehicleCountMap.put(vehicleType, vehicleCountMap.getOrDefault(vehicleType, 0) + 1);
                } else if (action.equals("EXIT")) {
                    vehicleCountMap.put(vehicleType, vehicleCountMap.getOrDefault(vehicleType, 0) - 1);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return vehicleCountMap;
    }

    /*
    Tracks the count of vehicles entering the parking lot for each hour of the day.
    It iterates through the log entries, parses the timestamps, and increments the appropriate hourly count whenever an entrance occurs.
    After processing all logs, the function finds the hour with the highest entrance count and returns that as the busiest hour.
    */
    public static int busiestHour(List<String[]> logs) {
        int[] hourlyCounts = new int[24]; // Array to store counts for each hour

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        for (String[] log : logs) {
            String timestampStr = log[2];

            try {
                Date timestamp = dateFormat.parse(timestampStr);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(timestamp);

                int hour = calendar.get(Calendar.HOUR_OF_DAY);

                if (log[3].equals("ENTRANCE")) {
                    hourlyCounts[hour]++;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        int busiestHour = 0;
        int maxCount = 0;

        for (int i = 0; i < hourlyCounts.length; i++) {
            if (hourlyCounts[i] > maxCount) {
                maxCount = hourlyCounts[i];
                busiestHour = i;
            }
        }

        return busiestHour;
    }
}