package org.gitsokolek.botanicaltree.plantorgans.interfaces;

// Java 22
public sealed interface PlantOrgans
{

	enum LEAF implements PlantOrgans
	{ BROAD_LEAF, ACICULAR_LEAF }

	enum SHOOT implements PlantOrgans
	{ TRUNK, BRANCH }
}
