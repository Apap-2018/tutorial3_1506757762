package com.apap.tutorial3.controller;

import com.apap.tutorial3.model.PilotModel;
import com.apap.tutorial3.service.PilotService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PilotController {
	@Autowired
	private PilotService pilotService;

	@RequestMapping("/pilot/add")
	public String add(@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "licenseNumber", required = true) String licenseNumber,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "flyHour", required = true) int flyHour) {
		PilotModel pilot = new PilotModel(id, licenseNumber, name, flyHour);
		pilotService.addPilot(pilot);
		return "add";
	}

	@RequestMapping("/pilot/view")
	public String view(@RequestParam("licenseNumber") String licenseNumber, Model model) {

		PilotModel archive = pilotService.getPilotDetailByLicenseNumber(licenseNumber);

		model.addAttribute("pilot", archive);
		return "view-pilot";
	}

	@RequestMapping("/pilot/viewall")
	public String viewall(Model model) {
		List<PilotModel> archive = pilotService.getPilotList();

		model.addAttribute("listPilot", archive);
		return "viewall-pilot";
	}

	@RequestMapping(value = { "/pilot/view/license-number", "/pilot/view/license-number/{number}"})
	public String viewLicense(@PathVariable Optional<String> number, @PathVariable Optional<Integer> update, Model model) {
		if (number.isPresent()) {
			if (((pilotService.getPilotDetailByLicenseNumber(number.get())) != null)) {
				PilotModel archive = pilotService.getPilotDetailByLicenseNumber(number.get());
				model.addAttribute("pilot", archive);
				return "view-pilot";
			}
		}
		return "notfound";
	}
	
	@RequestMapping(value = {"/pilot/view/license-number/{number}/fly-hour/", "/pilot/view/license-number/{number}/fly-hour/{update}","/pilot/view/license-number/fly-hour/{update}"})
	public String setFlyHour(@PathVariable Optional<String> number, @PathVariable Optional<Integer> update,
			Model model) {
		if (update.isPresent() && number.isPresent()) {
			PilotModel archive = pilotService.getPilotDetailByLicenseNumber(number.get());
			if(archive != null) {
				archive.setFlyHour(update.get());
				model.addAttribute("successMessage", "Edit Succeed");
				return "edit-success";
			}
		}
		return "notfound";
	}
	
	@RequestMapping(value= {"/pilot/delete/id/","/pilot/delete/id/{id}"})
	public String delete(@PathVariable Optional<String> id, Model model) {
	
	if(id.isPresent()) {
		for(int i=0 ; i< pilotService.getPilotList().size(); i++) {
			if(pilotService.getPilotList().get(i).getId().equalsIgnoreCase(id.get())) {
				pilotService.getPilotList().remove(i);
				model.addAttribute("successMessage", "Delete Sucess");
				return "edit-success";
			}
		}	
	}
	return "notfound";
	}
}
