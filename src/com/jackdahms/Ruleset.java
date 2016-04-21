package com.jackdahms;

import java.util.List;

public class Ruleset {
	
	String name;
	float fillDensity;
	Generatable mapper;
	
	public Ruleset(String name, float fillDensity, Generatable mapper) {
		this.name = name;
		this.fillDensity = fillDensity;
		this.mapper = mapper;
	}

}
