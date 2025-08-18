package org.gitsokolek.basicgenerictree;

import org.gitsokolek.botanicaltree.plantorgans.BotanicalTreeLastOrgan;
import org.gitsokolek.botanicaltree.plantorgans.BotanicalTreeOrgan;
import org.gitsokolek.botanicaltree.plantorgans.BotanicalTreeOrganData;
import org.gitsokolek.botanicaltree.plantorgans.interfaces.PlantOrgans;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

	private static BotanicalTreeOrganData meta(String name, PlantOrgans kind) {
		return new BotanicalTreeOrganData(name, kind) {};
	}

	@Test
	void childrenView_isUnmodifiable() {
		var p = new BotanicalTreeOrgan(meta("p", PlantOrgans.SHOOT.BRANCH));
		var c = new BotanicalTreeLastOrgan(meta("c", PlantOrgans.LEAF.BROAD_LEAF));
		assertTrue(p.attach(c));

		List<? extends LastNode> viewCovariant = p.childrenView();

		assertThrows(UnsupportedOperationException.class, () -> {
			@SuppressWarnings({"unchecked", "rawtypes"})
			List<LastNode> view = (List) viewCovariant; // tylko do testu, aby wywołać add(...)
			view.add(c);
		}, "childrenView must be unmodifiable");
	}


	@Test
	void attach_setsParent_andReturnsTrue() {
		var p = new BotanicalTreeOrgan(meta("p", PlantOrgans.SHOOT.BRANCH));
		var c = new BotanicalTreeLastOrgan(meta("c", PlantOrgans.LEAF.ACICULAR_LEAF));
		assertTrue(p.attach(c), "attach should return true");
		assertSame(p, c.getParent(), "attach should set parent reference");
	}

	@Test
	void attach_sameInstanceTwice_isAllowed() {
		var p = new BotanicalTreeOrgan(meta("p", PlantOrgans.SHOOT.BRANCH));
		var c = new BotanicalTreeLastOrgan(meta("c", PlantOrgans.LEAF.BROAD_LEAF));
		assertTrue(p.attach(c));
		assertTrue(p.attach(c));
		List<? extends LastNode> view = p.childrenView();
		assertEquals(2, view.size(), "Duplicate child instances should be allowed");
		assertSame(view.get(0), view.get(1), "Both entries must reference the same instance");
	}

	@Test
	void attach_selfIsRejected() {
		var n = new BotanicalTreeOrgan(meta("n", PlantOrgans.SHOOT.BRANCH));
		assertFalse(n.attach(n), "Self attachment must be rejected");
	}

	@Test
	void attach_cycleIsRejected() {
		var a = new BotanicalTreeOrgan(meta("a", PlantOrgans.SHOOT.BRANCH));
		var b = new BotanicalTreeOrgan(meta("b", PlantOrgans.SHOOT.BRANCH));
		assertTrue(a.attach(b), "a->b should succeed");
		assertFalse(b.attach(a), "b->a must be rejected to prevent a cycle");
	}

	@Test
	void attach_acceptsMixedChildren() {
		var p = new BotanicalTreeOrgan(meta("p", PlantOrgans.SHOOT.BRANCH));
		var branch = new BotanicalTreeOrgan(meta("branch", PlantOrgans.SHOOT.BRANCH));
		var leaf = new BotanicalTreeLastOrgan(meta("leaf", PlantOrgans.LEAF.ACICULAR_LEAF));
		assertTrue(p.attach(branch));
		assertTrue(p.attach(leaf));
		assertEquals(2, p.childrenView().size(), "Parent should contain mixed children");
	}
}
