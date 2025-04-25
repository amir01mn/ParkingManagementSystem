package com.company;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParkingLotManager {
    private Timer timer;

    public ParkingLotManager() {
        timer = new Timer();

        try {
            File dataDir = new File(System.getProperty("user.dir"), "data");
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }

            // Create database files if they don't exist
            File lotDB = new File(getAbsolutePathForLot());
            if (!lotDB.exists()) {
                createEmptyLotDatabase(lotDB);
            }

            File spotDB = new File(getAbsolutePathForSpot());
            if (!spotDB.exists()) {
                createEmptySpotDatabase(spotDB);
            }

            File tmpFile = new File(getAbsolutePathForTmp());
            if (!tmpFile.getParentFile().exists()) {
                tmpFile.getParentFile().mkdirs();
            }

        } catch (Exception e) {
            System.err.println("Error initializing database files: " + e.getMessage());
        }
    }

    // Create empty lot database with headers
    private void createEmptyLotDatabase(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("LotID,Location,Status,Capacity,Maintenance\n");
        } catch (IOException e) {
            System.err.println("Error creating lot database: " + e.getMessage());
        }
    }

    // Create empty spot database with headers
    private void createEmptySpotDatabase(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("SpotID,LotID,Status,SensorID,Maintenance\n");
        } catch (IOException e) {
            System.err.println("Error creating spot database: " + e.getMessage());
        }
    }

    private static String getAbsolutePathForLot() {

        String currentDir = System.getProperty("user.dir");
        return Paths.get(currentDir, "data/Parking_Lot_Database.csv").toString();
    }

    private static String getAbsolutePathForSpot() {

        String currentDir = System.getProperty("user.dir");
        return Paths.get(currentDir, "data/Parking_Spaces_Database.csv").toString();
    }

    private static String getAbsolutePathForTmp() {

        String currentDir = System.getProperty("user.dir");
        return Paths.get(currentDir, "data/tmp.csv").toString();
    }

    public int getNextParkingSpotID() {

        int lastID = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(getAbsolutePathForSpot()))) {

            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                if (data.length >= 5) {
                    try {

                        int id = Integer.parseInt(data[0]);
                        if (id > lastID)
                            lastID = id;

                    }
                    catch (NumberFormatException e) {
                        System.err.println("Error data reading " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
        catch (IOException e) {
            System.err.println("getting next p id " + e.getMessage());
            e.printStackTrace();
        }
        return lastID + 1;
    }

    public int getNextParkingLotID() {

        int lastID = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(getAbsolutePathForLot()))) {

            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                if (data.length >= 4) {

                    try {
                        int id = Integer.parseInt(data[0]);
                        if (id > lastID)
                            lastID = id;

                    }
                    catch (NumberFormatException e) {
                        System.err.println("Error data reading " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
        catch (IOException e) {
            System.err.println("getting next l id " + e.getMessage());
            e.printStackTrace();
        }

        return lastID + 1;
    }

    // Load parking lots and their spaces from CSV
    public List<ParkingLot> getAllLots() {

        List<ParkingLot> lots = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(getAbsolutePathForLot()))) {

            String line=br.readLine();

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                if (data.length >= 4) {
                    try {

                        int lotID = Integer.parseInt(data[0].trim());
                        String location = data[1].trim();
                        String status = data[2].trim();
                        ParkingLot lot = new ParkingLot(lotID, location);
                        String needMaintenance = data[3].trim();
                        lot.setNeedMaintenance(needMaintenance);
                        lot.setStatus(status);
                        lots.add(lot);
                    }
                    catch (NumberFormatException e) {
                        System.err.println("Error parsing lot ID: " + e.getMessage());
                    }
                }
            }
        }
        catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }
        return lots;
    }


    // Load parking spaces for a specific parking lot from CSV
    public List<ParkingSpace> getAllSpaces() {

        List<ParkingSpace> spaces = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(getAbsolutePathForSpot()))) {

            String line=br.readLine();
            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                if (data.length >= 5) {
                    try {
                        int spotID = Integer.parseInt(data[0].trim());
                        int lotID = Integer.parseInt(data[1].trim());
                        String status = data[2].trim();
                        int sensorID = Integer.parseInt(data[3].trim());
                        ParkingSpace space = new ParkingSpace(spotID, lotID, sensorID);
                        space.setStatus(status);
                        String needMaintenance = data[4].trim();
                        space.setNeedMaintenance(needMaintenance);
                        spaces.add(space);
                    }
                    catch (NumberFormatException e) {
                        System.err.println("Error parsing spot or lot ID: " + e.getMessage());
                    }
                }
            }
        }
        catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }
        return spaces;
    }

    public void addParkingLot(ParkingLot newLot) {

        List<ParkingLot> existingLots = getAllLots(); // read all current lots
        existingLots.add(newLot);                     // add the new one

        List<String> lines = new ArrayList<>();
        lines.add("LotID,Location,Status,NeedMaintenance"); // header line for CSV

        for (ParkingLot lot : existingLots) {

            String record = lot.getLotID() + "," + lot.getLocation() + "," + lot.getStatus() + "," + lot.getNeedMaintenance();
            lines.add(record);
        }
        writeAllLinesToParkingLotCSV(lines);
    }

    public void addParkingSpace(ParkingSpace newSpot) {

        List<ParkingSpace> existingSpaces = getAllSpaces(); // read all current spaces
        existingSpaces.add(newSpot);                        // add the new one

        List<String> lines = new ArrayList<>();
        lines.add("SpotID,LotID,Status,SensorID,NeedMaintenance"); // header line for CSV

        for (ParkingSpace space : existingSpaces) {

            String record = space.getSpotID() + "," + space.getLotID() + "," + space.getStatus() + "," + space.getSensorID() + "," + space.getNeedMaintenance();
            lines.add(record);
        }

        writeAllLinesToParkingSpaceCSV(lines);
    }

    public ParkingLot findLotByID(int lotID) {

        try (BufferedReader br = new BufferedReader(new FileReader(getAbsolutePathForLot()))) {
            String line=br.readLine();

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");
                int id = Integer.parseInt(data[0]);

                if (id == lotID) {

                    ParkingLot lot = new ParkingLot(id, data[1]);
                    lot.setStatus(data[2]);
                    String needMaintenance = data[3].trim();
                    lot.setNeedMaintenance(needMaintenance);
                    return lot;
                }
            }
        }
        catch (IOException e) {
            System.err.println("Error finding lot by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public ParkingSpace findSpaceByID(int spaceID) {

        try (BufferedReader br = new BufferedReader(new FileReader(getAbsolutePathForSpot()))) {
            String line=br.readLine();

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");
                int id = Integer.parseInt(data[0]);

                if (id == spaceID) {
                    ParkingSpace space = new ParkingSpace(id, Integer.parseInt(data[1]), Integer.parseInt(data[3]));

                    space.setStatus(data[2]);
                    space.setNeedMaintenance(data[4].trim());
                    return space;
                }
            }
        }
        catch (IOException e) {
            System.err.println("Error finding space by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<ParkingSpace> getAvailableSpaces() {

        List<ParkingSpace> allSpaces = getAllSpaces();
        return allSpaces.stream().filter(space -> "Available".equalsIgnoreCase(space.getStatus())).collect(Collectors.toList());
    }

    public List<ParkingSpace> getTimeAvailableSpaces(LocalTime startTime, LocalTime endTime) {
        
        List<ParkingSpace> availableSpaces = getAvailableSpaces(); // Get all available spaces based on status
        List<Booking> activeBookings = BookingDatabaseHelper.getBookingsForTimeSlot(startTime, endTime); // Assuming implementation exists to fetch bookings

        Set<Integer> bookedSpotIDs = activeBookings.stream().map(Booking::getParkingSpace).collect(Collectors.toSet());

        return availableSpaces.stream().filter(space -> !bookedSpotIDs.contains(space.getSpotID())).collect(Collectors.toList());
    }






    // Update the status of a parking lot in the CSV file
    public void updateLotStatusInCSV(ParkingLot lot, String newStatus) {
        // Check for null lot
        if (lot == null) {
            System.err.println("Warning: Cannot update status of null parking lot");
            return;
        }
        List<ParkingLot> existingLots = getAllLots(); // read all current lots

        for (ParkingLot l : existingLots) {
            if (l.getLotID() == lot.getLotID()) {
                l.setStatus(newStatus); // update status
            }
        }

        List<String> lines = new ArrayList<>();
        lines.add("LotID,Location,Status,NeedMaintenance"); // header

        for (ParkingLot l : existingLots) {
            String record = l.getLotID() + "," + l.getLocation() + "," + l.getStatus() + "," + l.getNeedMaintenance();
            lines.add(record);
        }

        writeAllLinesToParkingLotCSV(lines);
    }


    public void updateSpacesStatusinCSV(ParkingLot lot, String status) {
        // Check for null lot
        if (lot == null) {
            System.err.println("Warning: Cannot update spaces status for null parking lot");
            return;
        }
        List<ParkingSpace> existingSpaces = getAllSpaces(); // read all current spaces

        for (ParkingSpace s : existingSpaces) {
            if (s.getLotID() == lot.getLotID()) {
                s.setStatus(status);  // update status of all spaces in that lot
            }
        }

        List<String> lines = new ArrayList<>();
        lines.add("SpotID,LotID,Status,SensorID,NeedMaintenance"); // header

        for (ParkingSpace s : existingSpaces) {
            String record = s.getSpotID() + "," + s.getLotID() + "," + s.getStatus() + "," + s.getSensorID() + "," + s.getNeedMaintenance();
            lines.add(record);
        }

        writeAllLinesToParkingSpaceCSV(lines);
    }

    public void updateSpaceStatusinCSV(ParkingSpace spot, String status) {
        // Check for null space
        if (spot == null) {
            System.err.println("Warning: Cannot update status of null parking space");
            return;
        }
        List<ParkingSpace> existingSpaces = getAllSpaces(); // read all current spaces

        for (ParkingSpace s : existingSpaces) {
            if (s.getSpotID() == spot.getSpotID()) {
                s.setStatus(status);  // update status
            }
        }

        List<String> lines = new ArrayList<>();
        lines.add("SpotID,LotID,Status,SensorID,NeedMaintenance"); // header

        for (ParkingSpace s : existingSpaces) {
            String record = s.getSpotID() + "," + s.getLotID() + "," + s.getStatus() + "," + s.getSensorID() + "," + s.getNeedMaintenance();
            lines.add(record);
        }

        writeAllLinesToParkingSpaceCSV(lines);
    }
    public void writeAllLinesToParkingSpaceCSV(List<String> lines) {
        File outputFile = new File(getAbsolutePathForSpot());
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile, false))) {  // false to overwrite the file
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing updated data to file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void writeAllLinesToParkingLotCSV(List<String> lines) {
        File outputFile = new File(getAbsolutePathForLot());
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile, false))) {  // false to overwrite the file
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing updated data to file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public List<SensorData> readSensorData(String filePath) {
        List<SensorData> sensorDataList = new ArrayList<>();
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            return lines.skip(1)  // Skip header
                    .map(line -> line.split(","))
                    .map(data -> {
                        try {
                            int sensorId = Integer.parseInt(data[0]);
                            boolean carDetected = "true".equalsIgnoreCase(data[1]);

                            // Try multiple date formats
                            LocalDateTime timestamp = null;
                            try {
                                // Try yyyy-MM-dd HH:mm:ss format
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                timestamp = LocalDateTime.parse(data[2], formatter);
                            } catch (Exception e1) {
                                try {
                                    // Try HH:mm format
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                                    timestamp = LocalDateTime.parse(data[2], formatter);
                                } catch (Exception e2) {
                                    // Default to current time if parsing fails
                                    timestamp = LocalDateTime.now();
                                }
                            }

                            return new SensorData(sensorId, carDetected, timestamp);
                        } catch (Exception e) {
                            System.err.println("Error parsing sensor data: " + e.getMessage());
                            return null;
                        }
                    })
                    .filter(data -> data != null)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Error reading sensor data file: " + e.getMessage());
            e.printStackTrace();
            return sensorDataList;
        }
    }


    public void updateParkingSpaces(List<SensorData> sensorDataList) {
        if (sensorDataList == null) {
            System.err.println("Warning: Null sensor data list passed to updateParkingSpaces");
            return;
        }

        sensorDataList.forEach(data -> {
            ParkingSpace space = findSpaceByID(data.getSpotID());
            if (space != null && space.isAvailable() != !data.isCarDetected()) {
                space.setStatus(data.isCarDetected() ? "occupied" : "available");
                updateSpaceStatusinCSV(space, space.getStatus());  // Assuming this method updates the CSV
            }
        });
    }
    public void checkCompletedBookings() {
        
        List<Booking> allBookings = BookingDatabaseHelper.readAllBookings();
        LocalTime now = LocalTime.now();

        for (Booking booking : allBookings) {
            if ("Active".equals(booking.getBookingStatus()) && 
                "Paid".equals(booking.getPaymentStatus()) && 
                now.isAfter(booking.getEnd())) {
                booking.checkout();
            }
        }
    }
    public void scheduleRegularUpdates() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                List<SensorData> sensorData = readSensorData("path/to/sensor_timestamped.csv");
                updateParkingSpaces(sensorData);
                checkCompletedBookings(); // Add automatic checkout check
            }
        }, 0, 900000);  // Schedule to run every 15 minutes (900,000 milliseconds)
    }

    public Object getTimer() {
        return timer;
    }
}


