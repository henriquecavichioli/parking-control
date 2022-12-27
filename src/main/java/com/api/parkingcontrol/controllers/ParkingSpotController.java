package com.api.parkingcontrol.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
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
	public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDto psDto){
		
		if(psService.existsByLicensePlateCar(psDto.getLicensePlateCar())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: License Plate Car is already in use!");
		}
		
		if(psService.existsByParkingSpotNumber(psDto.getParkingSpotNumber())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot is already in use!");
		}
		
		if(psService.existsByApartmentAndBlock(psDto.getApartment(), psDto.getBlock())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot already registered for this apartment/block!");
		}
		
		var psModel = new ParkingSpotModel(); // novidade jdk 9+ criar um objeto do tipo desejado com var
		BeanUtils.copyProperties(psDto, psModel);
		psModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
		return ResponseEntity.status(HttpStatus.CREATED).body(psService.save(psModel));
	}
}

/*

{

	"parkingSpotNumber": "2058",
	"licensePlateCar": "RRS8562",
	"brandCar": "audi",
	"modelCar": "q5",
	"colorCar": "black",
	"responsibleName": "Carlos Daniel",
	"apartment": "205",
	"block": "B",
}

 */
