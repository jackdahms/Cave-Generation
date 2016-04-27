package com.jackdahms;

public class Ruleset {
	
	String name;
	float fillDensity;
	int smoothingIterations;
	Generatable mapper;
	
	public Ruleset(String name, float fillDensity, int smoothingIterations, Generatable mapper) {
		this.name = name;
		this.fillDensity = fillDensity;
		this.smoothingIterations = smoothingIterations;
		this.mapper = mapper;
	}

}
