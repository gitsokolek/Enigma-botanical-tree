package org.gitsokolek.botanicaltree.plantorgans;

import org.gitsokolek.basicgenerictree.LastNode;
import org.gitsokolek.botanicaltree.plantorgans.interfaces.Growable;

public class BotanicalTreeLastOrgan extends LastNode<BotanicalTreeOrganData> implements Growable {

	public BotanicalTreeLastOrgan(BotanicalTreeOrganData metaData) {
		super(metaData);
	}

	@Override
	public void grow() {
		System.out.println("Growing " + getMetaData().getName() + " of type " + getMetaData().getPlantOrgan());
	}
}
