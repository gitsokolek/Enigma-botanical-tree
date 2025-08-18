package org.gitsokolek;

import org.gitsokolek.botanicaltree.TreeFactory;
import org.gitsokolek.botanicaltree.plantorgans.BotanicalTreeOrgan;
import org.gitsokolek.botanicaltree.plantorgans.interfaces.PlantOrgans;

public class Main {
	public static void main(String[] args) {
		// losowe liściaste
		BotanicalTreeOrgan broad = TreeFactory.createRandomTree(4, 3, 4, PlantOrgans.LEAF.BROAD_LEAF, 123L);
		System.out.println("=== Random broad-leaf ===");
		broad.grow();

		// losowe iglaste
		BotanicalTreeOrgan conifer = TreeFactory.createRandomTree(4, 3, 5, PlantOrgans.LEAF.ACICULAR_LEAF);
		System.out.println("\n=== Random coniferous ===");
		conifer.grow();
	}
}
