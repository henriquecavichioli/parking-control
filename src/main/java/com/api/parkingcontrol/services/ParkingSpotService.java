package com.api.parkingcontrol.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	public boolean existsByLicensePlateCar(String licensePlateCar) {

		return psRepository.existsByLicensePlateCar(licensePlateCar);
	}

	public boolean existsByParkingSpotNumber(String parkingSpotNumber) {
		return psRepository.existsByParkingSpotNumber(parkingSpotNumber);
	}

	public boolean existsByApartmentAndBlock(String apartment, String block) {
		return psRepository.existsByApartmentAndBlock(apartment, block);
	}

	public Page<ParkingSpotModel> findAll(Pageable pageable) {
		return psRepository.findAll(pageable);
	}

	public Optional<ParkingSpotModel> findById(UUID id) {
		return psRepository.findById(id);
	}
	
	@Transactional
	public void delete(ParkingSpotModel parkingSpotModel) {
		psRepository.delete(parkingSpotModel);
	}

}
