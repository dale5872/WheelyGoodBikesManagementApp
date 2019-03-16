import DatabaseConnector.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class generateRentalTestData {

    public static void main(String[] args) {
        int[] accounts = {79, 80, 81, 82, 83, 84, 85, 86 ,87};
        int[] locations = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 23};
        int[] bikes = {1, 2, 3, 4, 5, 6, 7};
        double[] prices = {3.9, 5.2, 5.9, 9.3, 4.5, 7.2, 5.9};
        String[] statuses = {"Paid", "Unpaid"};

        for(int i = 0; i < 200; i++) {

            /** Pick rental details **/
            int account = accounts[new Random().nextInt(accounts.length - 1)];
            int location = locations[new Random().nextInt(locations.length - 1)];
            int bike = bikes[new Random().nextInt(bikes.length - 1)];
            double price = prices[bike];
            String status = statuses[new Random().nextInt(statuses.length - 1)];


            /** Start Date **/
            int days = new Random().nextInt(100) + 1;
            int hours = new Random().nextInt(24);
            int mins = new Random().nextInt(60);
            int secs = new Random().nextInt(60);

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // Save current LocalDateTime into a variable
            LocalDateTime localDateTime = LocalDateTime.now();

            // Format LocalDateTime into a String variable and print
            String formattedLocalDateTime = localDateTime.format(dateTimeFormatter);

            // Add randomAmountOfDays to LocalDateTime variable we defined earlier and store it into a new variable
            LocalDateTime date = localDateTime.minusDays(days);
            date = date.minusHours(hours);
            date = date.minusMinutes(mins);
            date = date.minusSeconds(secs);

            // Format new LocalDateTime variable into a String variable and print
            String formattedStartDateTime = date.format(dateTimeFormatter);


            /** End Date **/
            days = new Random().nextInt(2);
            hours = new Random().nextInt(24);
            mins = new Random().nextInt(60);
            secs = new Random().nextInt(60);

            // Add randomAmountOfDays to LocalDateTime variable we defined earlier and store it into a new variable
            date = date.plusDays(days);
            date = date.plusHours(hours);
            date = date.plusMinutes(mins);
            date = date.plusSeconds(secs);

            // Format new LocalDateTime variable into a String variable and print
            String formattedEndDateTime = date.format(dateTimeFormatter);

            price = ((price * 24) * days) + (price * hours) + ((price / 60) * mins);

            /** Get bike id **/
            Query q = new Query("read", "fetchBikes", "location_id=" + location + "&bike_type=" + bike + "&search=");
            Results res = q.executeQuery();
            int bikeID = Integer.parseInt((String) res.getElement(new Random().nextInt(res.getRows() - 1), "bikeID"));


            String params = "bike_id=" + bikeID + "&user_id=" + account + "&location=" + location + "&price=" + price + "&start_time=" + formattedStartDateTime + "&return_time=" + formattedEndDateTime + "&status=" + status;
            Query q2 = new Query("create", "addBikeRental", params);
            if (!q2.insertQuery()) {
                System.err.println("Error inserting into database\n");
            } else {
                System.out.println("Inserted");
                System.out.println("Start Date: " + formattedStartDateTime);
                System.out.println("End Date: " + formattedEndDateTime);
                System.out.println("Bike " + bike + " with bikeID " + bikeID + " at location " + location + " by account " + account + " costing " + price + " with status " + status);
            }
        }
    }
}
