package org.gitsokolek.botanicaltree.plantorgans;

import org.gitsokolek.basicgenerictree.interfaces.MetaData;
import org.gitsokolek.botanicaltree.plantorgans.interfaces.PlantOrgans;

public abstract class BotanicalTreeOrganData implements MetaData
{
	private final PlantOrgans plantOrgan;
	private final String name ;


	protected BotanicalTreeOrganData(String name, PlantOrgans plantOrgan)
	{
		this.plantOrgan = plantOrgan;

		this.name = name;
	}

	public String getName()
	{
		return name;
	}


	public PlantOrgans getPlantOrgan()
	{
		return plantOrgan;
	}

}
