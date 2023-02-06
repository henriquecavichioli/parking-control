package com.api.parkingcontrol.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.parkingcontrol.dtos.ParkingSpotDto;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.services.ParkingSpotService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking-spot")
@RestController
public class ParkingSpotController {

	@Autowired
	ParkingSpotService psService;

	@PostMapping
	public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDto psDto) {

		if (psService.existsByLicensePlateCar(psDto.getLicensePlateCar())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: License Plate Car is already in use!");
		}

		if (psService.existsByParkingSpotNumber(psDto.getParkingSpotNumber())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot is already in use!");
		}

		if (psService.existsByApartmentAndBlock(psDto.getApartment(), psDto.getBlock())) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Conflict: Parking Spot already registered for this apartment/block!");
		}

		var psModel = new ParkingSpotModel(); // novidade jdk 9+ criar um objeto do tipo desejado com var
		BeanUtils.copyProperties(psDto, psModel);
		psModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
		return ResponseEntity.status(HttpStatus.CREATED).body(psService.save(psModel));
	}

	@GetMapping
	public ResponseEntity<Page<ParkingSpotModel>> getAllParkingSpots(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable  pageable) {
		return ResponseEntity.status(HttpStatus.OK).body(psService.findAll(pageable));

	}
	
	@GetMapping("{id}")
	public ResponseEntity<Object> getOneParkingSpots(@PathVariable(value = "id")UUID id) {
		Optional<ParkingSpotModel> parkingSpotModelOptional = psService.findById(id);
		if (!parkingSpotModelOptional.isPresent() ) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
		}
		return ResponseEntity.status(HttpStatus.OK).body(parkingSpotModelOptional.get());

	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<Object> deleteParkingSpots(@PathVariable(value = "id")UUID id) {
		Optional<ParkingSpotModel> parkingSpotModelOptional = psService.findById(id);
		if (!parkingSpotModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
		}
		psService.delete(parkingSpotModelOptional.get());
		return ResponseEntity.status(HttpStatus.OK).body("Parking Spot deleted sucessfully.");

	}
	
	@PutMapping("{id}")
	public ResponseEntity<Object> updateParkingSpots(@PathVariable(value = "id")UUID id, 
													@RequestBody @Valid ParkingSpotDto psDto) {
		Optional<ParkingSpotModel> parkingSpotModelOptional = psService.findById(id);
		if (!parkingSpotModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
		}
		
		var psModel = new ParkingSpotModel();
		BeanUtils.copyProperties(psDto, psModel);
		psModel.setId(parkingSpotModelOptional.get().getId());
		psModel.setRegistrationDate(parkingSpotModelOptional.get().getRegistrationDate());
		psService.save(psModel);
		return ResponseEntity.status(HttpStatus.OK).body("Parking Spot updated sucessfully.");
		
		/*var psModel = parkingSpotModelOptional.get();
		psModel.setParkingSpotNumber(psDto.getParkingSpotNumber());
		psModel.setLicensePlateCar(psDto.getLicensePlateCar());
		psModel.setBrandCar(psDto.getBrandCar());
		psModel.setModelCar(psDto.getModelCar());
		psModel.setColorCar(psDto.getColorCar());
		psModel.setResponsibleName(psDto.getResponsibleName());
		psModel.setApartment(psDto.getApartment());
		psModel.setBlock(psDto.getBlock());
		psService.save(psModel);
		return ResponseEntity.status(HttpStatus.OK).body("Parking Spot updated sucessfully.");*/
		
	}
	
}