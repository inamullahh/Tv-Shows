package com.inam.tvshows.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.inam.tvshows.models.TvShow;
import com.inam.tvshows.repositories.TvShowRepository;

@Service
public class TvShowService {
	
	private final TvShowRepository nameRepo;
	
	// Constructor
	public TvShowService(TvShowRepository nameRepo) {
		super();
		this.nameRepo = nameRepo;
	}
	
	
	// List all the show names
	public List<TvShow> allNames(){
		return nameRepo.findAll();
	}
	
	// Find one show by Id
	public TvShow findOne(Long Id) {
		Optional<TvShow> optionalName = nameRepo.findById(Id);
		if(optionalName.isPresent()) {
			return optionalName.get();
		}else {
			return null;
		}
	}
	
	// Create a new show
	public TvShow create(TvShow name) {
		return nameRepo.save(name);
	}
	
	// Update an existing show
	public TvShow update(TvShow name) {
		return nameRepo.save(name);
	}
	
	// Delete a show 
	public void delete(Long id) {
		nameRepo.deleteById(id);
	}


}
