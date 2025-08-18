package org.gitsokolek.basicgenerictree;

import org.gitsokolek.botanicaltree.plantorgans.BotanicalTreeLastOrgan;
import org.gitsokolek.botanicaltree.plantorgans.BotanicalTreeOrgan;
import org.gitsokolek.botanicaltree.plantorgans.BotanicalTreeOrganData;
import org.gitsokolek.botanicaltree.plantorgans.interfaces.PlantOrgans;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LastNodeTest {

	private static BotanicalTreeOrganData meta(String name, PlantOrgans kind) {
		return new BotanicalTreeOrganData(name, kind) {};
	}

	@Test
	void getMetaData_returnsSameInstance() {
		var data = meta("X", PlantOrgans.SHOOT.BRANCH);
		var leaf = new BotanicalTreeLastOrgan(data);
		assertSame(data, leaf.getMetaData(), "getMetaData should return the same instance");
	}

	@Test
	void getParent_isNull_thenUpdatedByAttach() {
		var root = new BotanicalTreeOrgan(meta("root", PlantOrgans.SHOOT.BRANCH));
		var child = new BotanicalTreeLastOrgan(meta("child", PlantOrgans.LEAF.BROAD_LEAF));
		assertNull(child.getParent(), "Parent should be null before attachment");
		assertTrue(root.attach(child), "attach should succeed");
		assertSame(root, child.getParent(), "Parent should be set by attach");
	}
}
