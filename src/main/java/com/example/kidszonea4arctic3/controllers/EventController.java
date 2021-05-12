package com.example.kidszonea4arctic3.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.ocpsoft.rewrite.el.ELBeanName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.kidszonea4arctic3.models.Category;
import com.example.kidszonea4arctic3.models.ChildCareCenter;
import com.example.kidszonea4arctic3.models.Event;
import com.example.kidszonea4arctic3.models.Rate;
import com.example.kidszonea4arctic3.services.ChildCareCenterService;
import com.example.kidszonea4arctic3.services.EventService;
import com.example.kidszonea4arctic3.services.RateService;
import com.example.kiszonea4arctic3.utils.FileUploadUtil;

@Controller(value = "eventController")
@ELBeanName(value = "eventController")
public class EventController {
	@Autowired
	EventService es;
	@Autowired
	ChildCareCenterService js;

	@Autowired
	RateService rs;
	
	private Event e = new Event();

	private List<Event> list;
	private Category category;
	private boolean showForm = false;
	private boolean showStat = false;
	private Category searchCat;
	private float education;
	private float competition;
	private float divertissant;
	private float autre;
	
	
	
	public void showStatistics(){
		int n = es.retrieveAllEvents(Long.parseLong("1")).size();
		float  edu = es.searchByCategory(Category.EDUCATIF).size() ;
		float  comp = es.searchByCategory(Category.COMPETITION).size() ;
		float  div = es.searchByCategory(Category.DIVERTISSANT).size() ;
		float  autr = es.searchByCategory(Category.AUTRE).size() ;
	    education =  edu/n*100;
	    competition = comp/n*100;
	    divertissant = div/n*100;
	    autre = autr/n*100;
	    System.out.println(education + "\n"+ competition+ "\n" + divertissant +"\n" + autre);
	    showStatMeth();
	}
	
	public EventController(EventService es) {
		list = es.retrieveAllEvents(Long.parseLong("1"));
		for (Event e : list) {
			float rate = calculerRates(e);
			if (Float.isNaN(rate))
				rate = 0;
			e.setTotalRates(rate);
		}
		
	}
	
	
	public void showStatMeth(){
		showStat = true;
	}
	
	public boolean isShowStat() {
		return showStat;
	}

	public void setShowStat(boolean showStat) {
		this.showStat = showStat;
	}

	public float getEducation() {
		return education;
	}




	public void setEducation(float education) {
		this.education = education;
	}




	public float getCompetition() {
		return competition;
	}




	public void setCompetition(float competition) {
		this.competition = competition;
	}




	public float getDivertissant() {
		return divertissant;
	}




	public void setDivertissant(float divertissant) {
		this.divertissant = divertissant;
	}




	public float getAutre() {
		return autre;
	}




	public void setAutre(float autre) {
		this.autre = autre;
	}




	public float calculerRates(Event e) {
		Set<Rate> setR = new HashSet<Rate>();
		setR.addAll(e.getRates());
		float s = 0;
		for (Rate r : setR) {
			s += r.getValue();
		}
		return s / setR.size();
	}

	public List<Event> getList() {
		
		return list;
	}

	public void setList(List<Event> list) {
		
		this.list = list;
	}

	public void removeEvent(Long id) {
		es.deleteEvent(id);
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Event getE() {
		return e;
	}

	public void setE(Event e) {
		this.e = e;
	}

	
	public void rate(Event e){
		System.out.println("event : " + e);
		Rate r = new Rate();
		System.out.println("rateValue : " +e.getRateValue());
		r.setValue(e.getRateValue());
		Set<Event> setE = new HashSet<Event>();
		setE.add(e);
		r.setEvents(setE);
		System.out.println("rate : " + r);
		rs.addRateMeth(r);
		showAll();
	}
	
	
	
	




	public void showFormMeth(){
		showForm = true;
		System.out.println("showForm : "+showForm );
	}
	public void hideFormMeth(){
		System.out.println("showForm : "+showForm );
		showForm = false;
	}
	
	
	public boolean isShowForm() {
		return showForm;
	}

	public void setShowForm(boolean showForm) {
		this.showForm = showForm;
	}

	public void addEvent() {
		ChildCareCenter cc = js.retrieveJardin((long) 1).get();
		e.setChildCareCenter(cc);
		//e.setImage("static.jpg");
		System.out.println(e);
		es.addEvent(e);
		showAll();
	}

	public void updateEvent(Event e1) {
		showFormMeth();
		e = e1;
	}

	public Category[] getCategories() {
		return Category.values();
	}

	
	public void participate(Long id){
		es.participateEvent(id);
		showAll();
	}
	
	
	
	
	public void searchEvent(){
		System.out.println("searchCat : " + searchCat);
		list = es.searchByCategory(searchCat);
	  	for (Event e : list) {
			float rate = calculerRates(e);
			if(Float.isNaN(rate))rate=0;
			e.setTotalRates(rate);
		}
	}
	
	
	
	
	public void showAll(){
		list = es.retrieveAllEvents(Long.parseLong("1"));
		for (Event e : list) {
			float rate = calculerRates(e);
			if (Float.isNaN(rate))
				rate = 0;
			e.setTotalRates(rate);
		}
	}
	
	public Category getSearchCat() {
		return searchCat;
	}



	public void setSearchCat(Category searchCat) {
		this.searchCat = searchCat;
	}

	public void sortByRate(){
		list = es.retrieveAllEventsSortedByRate(Long.parseLong("1"));
		  for (Event e : list) {
				float rate = calculerRates(e);
				if(Float.isNaN(rate))rate=0;
				e.setTotalRates(rate);
			}
		
	}

	public void resetEvent(){
		e = new Event();
	}
	
	
	// http://localhost:8082/jardinEnfantEvents/servlet/retrieveAllEvents/{jardin-id}
	@GetMapping("/retrieveAllEvents/{jardin-id}")
	@ResponseBody
	public List<Event> retrieveAllEvents(@PathVariable("jardin-id") String jardinId) {
		return es.retrieveAllEvents(Long.parseLong(jardinId));
	}

	// http://localhost:8082/jardinEnfantEvents/servlet/ajouterEvent/{jardin-id}
	@PostMapping("/ajouterEvent/{jardin-id}")
	@ResponseBody
	public void ajouterEvent(@PathVariable("jardin-id") String idJardin,
			@RequestParam("image") MultipartFile multipartFile, @RequestParam("category") String category,
			@RequestParam("date_event") String date_event, @RequestParam("description") String description,
			@RequestParam("name") String name, @RequestParam("nb_participants") int nb_participants) throws IOException

	{
		ChildCareCenter j = js.retrieveJardin(Long.parseLong(idJardin)).get();

		// e.setJardin_enfant(j);
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		Category c = Category.AUTRE;
		switch (category) {
		case "COMPETITION":
			c = Category.COMPETITION;
			break;
		case "EDUCATIF":
			c = Category.EDUCATIF;
			break;
		case "DIVERTISSANT":
			c = Category.DIVERTISSANT;
			break;
		case "AUTRE":
			c = Category.AUTRE;
			break;
		}
		Date date1 = new Date();
		try {
			date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date_event);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Event e = new Event(date1, name, description, fileName, nb_participants, c, j);
		// es.addEvent(e);
		String uploadDir = "event-photos/" + e.getName();
		FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		es.addEvent(e);

	}

	// http://localhost:8082/jardinEnfantEvents/servlet/remove-event/{event-id}
	@DeleteMapping("/remove-event/{event-id}")
	@ResponseBody
	public void removeEvent(@PathVariable("event-id") String id) {
		es.deleteEvent(Long.parseLong(id));
	}

	// http://localhost:8082/jardinEnfantEvents/servlet/modify-event/{jardin-id}
	@PutMapping("/modify-event/{jardin-id}")
	@ResponseBody
	public void modifyEvent(@RequestBody Event e, @PathVariable("jardin-id") String idJardin) {
		ChildCareCenter j = js.retrieveJardin(Long.parseLong(idJardin)).get();
		e.setJardin_enfant(j);
		es.updateEvent(e);
	}

	// http://localhost:8082/jardinEnfantEvents/servlet/participer-event/{jardin-id}
	@PutMapping("/participer-event/{event-id}")
	@ResponseBody
	public void participerEvent(@RequestBody Event e, @PathVariable("event-id") String idEvent) {
		es.participateEvent(Long.parseLong(idEvent));
	}

	// http://localhost:8082/jardinEnfantEvents/servlet/retrieve-event/{event-id}
	@GetMapping("/retrieve-event/{event-id}")
	@ResponseBody
	public Optional<Event> retrieveEvent(@PathVariable("event-id") String eventId) {
		return es.retrieveEvent(Long.parseLong(eventId));
	}

	// http://localhost:8082/jardinEnfantEvents/servlet/searchByCategory/{category}
	@GetMapping("/searchByCategory/{category}")
	@ResponseBody
	public List<Event> searchByCategory(@PathVariable("category") Category category) {
		return es.searchByCategory(category);
	}

	// http://localhost:8082/jardinEnfantEvents/servlet/retrieveAllEventsSortedByRate/{jardin-id}
	@GetMapping("/retrieveAllEventsSortedByRate/{jardin-id}")
	@ResponseBody
	public List<Event> retrieveAllEventsSortedByRate(@PathVariable("jardin-id") String jardinId) {
		return es.retrieveAllEventsSortedByRate(Long.parseLong(jardinId));
	}

}
