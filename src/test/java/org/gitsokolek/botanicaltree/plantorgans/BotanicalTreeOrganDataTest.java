package org.gitsokolek.botanicaltree.plantorgans;

import org.gitsokolek.botanicaltree.plantorgans.interfaces.PlantOrgans;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BotanicalTreeOrganDataTest {

	@Test
	void getters_returnValues() {
		String name = "X";
		var kind = PlantOrgans.SHOOT.BRANCH;
		var data = new BotanicalTreeOrganData(name, kind) {};
		assertEquals(name, data.getName(), "getName should return the given name");
		assertEquals(kind, data.getPlantOrgan(), "getPlantOrgan should return the given organ");
	}
}
