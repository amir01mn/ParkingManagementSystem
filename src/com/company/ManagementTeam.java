package com.company;

public interface ManagementTeam {

	boolean addParkingLot(ParkingLotManager plManager,String location);
	void enableLot(ParkingLotManager plManager,int parkingID);
	void disableLot(ParkingLotManager plManager,int parkingID);
	void enableSpot(ParkingLotManager plManager,int sptID);
	void disableSpot(ParkingLotManager plManager,int sptID);
	void alertAvailableSpace(int sptID);
	void alertOverstay(int sptID);
	void alertMaintenance(int sptID);
	void alertIllegalParking(int sptID);
	boolean isMaintenanceRequiredSpot(int sptID);
	boolean isMaintenanceRequiredLot(int parkingID);

	String validateYorkU(String email);
}
