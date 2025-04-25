package com.company;

import java.io.*;
import java.util.*;
import java.nio.file.Paths;

public class Manager implements ManagementTeam {

    private static final String PARKINGLOT_CSV = "data/Parking_Lot_Database.csv";
    private static final String PARKINGSPOT_CSV = "data/Parking_Spaces_Database.csv";


    private String name;
    private String email;
    private String password;

    public Manager() {

        this.name = "";
        this.email = "";
        this.password = "";
    }

    public Manager(String name, String email, String password) {

        this.name = name;
        this.email = email;
        this.password = password;

    }

    @Override
    public boolean addParkingLot(ParkingLotManager plManager, String location) {

        if (plManager == null) {
            return false;
        }

        System.out.println("Adding parking lot at " + location);

        try {
            int newID = plManager.getNextParkingLotID();
            ParkingLot newLot = new ParkingLot(newID, location);
            plManager.addParkingLot(newLot);

            int newSpotID = plManager.getNextParkingSpotID();
            for (int i = 0; i < 100; i++) {
                ParkingSpace newSpot = new ParkingSpace(newSpotID + i, newID, newSpotID + i);
                plManager.addParkingSpace(newSpot);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void enableLot(ParkingLotManager plManager, int lotID) {

        if (plManager == null) {
            return;
        }

        try {
            ParkingLot lot = plManager.findLotByID(lotID);
            if (lot != null) {
                plManager.updateLotStatusInCSV(lot, "enabled");
                plManager.updateSpacesStatusinCSV(lot, "available");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disableLot(ParkingLotManager plManager, int lotID) {

        if (plManager == null) {
            return;
        }

        try {
            ParkingLot lot = plManager.findLotByID(lotID);
            if (lot != null) {
                plManager.updateLotStatusInCSV(lot, "disabled");
                plManager.updateSpacesStatusinCSV(lot, "disabled");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void enableSpot(ParkingLotManager plManager, int spotID) {

        if (plManager == null) {
            return;
        }

        try {
            ParkingSpace spot = plManager.findSpaceByID(spotID);
            if (spot != null) {
                plManager.updateSpaceStatusinCSV(spot, "available");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disableSpot(ParkingLotManager plManager, int spotID) {

        if (plManager == null) {
            return;
        }

        try {
            ParkingSpace spot = plManager.findSpaceByID(spotID);
            if (spot != null) {
                plManager.updateSpaceStatusinCSV(spot, "disabled");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void alertAvailableSpace(int sptID) {

        System.out.println("Alert: Available space at spot ID: " + sptID);
    }

    @Override
    public void alertOverstay(int sptID) {

        System.out.println("Alert: Overstay at spot ID: " + sptID);
    }

    @Override
    public void alertMaintenance(int sptID) {

        System.out.println("Alert: Maintenance required at spot ID: " + sptID);
    }


    @Override
    public void alertIllegalParking(int sptID) {

        System.out.println("Alert: Illegal parking at spot ID: " + sptID);
    }

    @Override
    public boolean isMaintenanceRequiredLot(int parkingID) {

        List<String> updatedRows = new ArrayList<>();
        boolean need_maintenance = false;
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(PARKINGLOT_CSV))) {
            String header = br.readLine();
            updatedRows.add(header);

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length >= 3) { // Adjusted for maintenance column being at index 3 or 4 depending how you formatted it
                    for (int i = 0; i < data.length; i++) {
                        data[i] = data[i].trim();
                    }

                    int id = Integer.parseInt(data[0]);

                    if (id == parkingID) {
                        found = true;
                        need_maintenance = Boolean.parseBoolean(data[3]); // assuming column 3 is "maintenance?"

                        if (need_maintenance) {
                            data[2] = "disabled"; // status column ("enabled"/"disabled") is at index 2
                        } else {
                            data[2] = "enabled";
                        }
                    }

                    updatedRows.add(String.join(",", data));
                }
            }
        }
        catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        if (found) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(PARKINGLOT_CSV))) {
                for (String row : updatedRows) {
                    pw.println(row);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Parking lot " + parkingID + " updated. Maintenance = " + need_maintenance);
        }
        else {
            System.out.println("No parking lot found with ID: " + parkingID);
        }

        return need_maintenance;
    }

    @Override
    public boolean isMaintenanceRequiredSpot(int sptID) {

        List<String> updatedRows = new ArrayList<>();
        boolean need_maintenance = false;
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(PARKINGSPOT_CSV))) {

            String header = br.readLine();
            updatedRows.add(header);
            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                if (data.length >= 4) {

                    for (int i = 0; i < data.length; i++)
                        data[i] = data[i].trim();


                    int id = Integer.parseInt(data[0]);

                    if (id == sptID) {

                        found = true;
                        need_maintenance = Boolean.parseBoolean(data[4]);
                        data[2] = need_maintenance ? "Disabled" : "Available";
                    }

                    updatedRows.add(String.join(",", data));
                }
            }

            try (PrintWriter pw = new PrintWriter(new FileWriter(PARKINGSPOT_CSV))) {

                for (String row : updatedRows)
                    pw.println(row);

            }

            if (found)
                System.out.println("Parking spot " + sptID + " was updated. Maintenance = " + need_maintenance);

            else
                System.out.println("No parking spot found with ID: " + sptID);


        }
        catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return need_maintenance;
    }


    @Override
    public String validateYorkU(String email) {

        if (email == null || email.trim().isEmpty()) {
            return "Visitor";
        }

        String YORKU_CSV = Paths.get(System.getProperty("user.dir"), "data", "YU_Database.csv").toString();

        try (BufferedReader br = new BufferedReader(new FileReader(YORKU_CSV))) {

            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                if (data.length >= 4) {
                    String entryEmail = data[3].trim();
                    String user_type = data[0].trim();

                    if (entryEmail.equalsIgnoreCase(email.trim()))
                        return user_type;

                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return "Visitor";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
