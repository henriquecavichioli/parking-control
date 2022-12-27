package com.api.parkingcontrol.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.repositories.ParkingSpotRepository;

import jakarta.transaction.Transactional;

@Service
public class ParkingSpotService {

	@Autowired
	ParkingSpotRepository psRepository;
	
	@Transactional
	public ParkingSpotModel save(ParkingSpotModel psModel) {
		return psRepository.save(psModel);
	}

	public boolean existsByLicensePlateCar (String licensePlateCar) {
		
		return psRepository.existsByLicensePlateCar(licensePlateCar);
	}

	public boolean existsByParkingSpotNumber(String parkingSpotNumber) {
		return psRepository.existsByParkingSpotNumber(parkingSpotNumber);
	}

	public boolean existsByApartmentAndBlock(String apartment, String block) {
		return psRepository.existsByApartmentAndBlock(apartment, block);
	}


		
	}
