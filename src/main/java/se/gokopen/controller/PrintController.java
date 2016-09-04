package se.gokopen.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import se.gokopen.dao.TrackNotFoundException;
import se.gokopen.model.Config;
import se.gokopen.model.Patrol;
import se.gokopen.model.Station;
import se.gokopen.model.Track;
import se.gokopen.service.ConfigService;
import se.gokopen.service.PatrolService;
import se.gokopen.service.ScoreService;
import se.gokopen.service.StationService;
import se.gokopen.service.TrackService;

@RequestMapping("/print")
@Controller
public class PrintController { 
	
	//Skjuter in PatrolService
	@Autowired
	private PatrolService patrolService;
	@Autowired
	private TrackService trackService;
	@Autowired
	private StationService stationService;
	@Autowired
	private ScoreService scoreService;
	@Autowired
    private ConfigService configService;
	
	@InitBinder
    protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Station.class, new StationEditor(this.stationService));
		binder.registerCustomEditor(Patrol.class, new PatrolEditor(this.patrolService));
		binder.registerCustomEditor(Track.class, new TrackEditor(this.trackService));
    }
	
	@ModelAttribute("tracks")
    public List<Track> populateTracks() {
		return trackService.getAllTracks();
	}
	
	@RequestMapping(value="/start")
	public String printScoreCardsStart(HttpServletRequest request){
		return "viewscorecardstart";
	}
	
	@RequestMapping(value="/bytrack/{id}")
	public ModelAndView printScoreCardForTrack(@PathVariable String id,HttpServletRequest request){
		Track track = null;
		try {
			track = trackService.getTrackById(Integer.parseInt(id));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TrackNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.setAttribute("selectedTrack", track.getTrackName());
		List<Patrol> patrols = patrolService.getAllPatrolsByTrack(track);
		List<Station> stations = stationService.getAllStations();
		request.setAttribute("stations", stations);
		
		return new ModelAndView("printscorecardstations","patrols",patrols);
	}
	
	@RequestMapping(value="/patrolcards", method=RequestMethod.GET)
	public ModelAndView printPatrolCards(){
	    ModelAndView model = new ModelAndView();
	    Config config = configService.getCurrentConfig();
	    model.addObject("config",config);
	    List<Station> stations = stationService.getAllStations();
	    model.addObject("stations",stations);
	    List<Patrol> patrols = patrolService.getAllPatrols();
	    model.addObject("patrols",patrols);
	    model.setViewName("printpatrolcards");
	    return model;
	}
	
}
