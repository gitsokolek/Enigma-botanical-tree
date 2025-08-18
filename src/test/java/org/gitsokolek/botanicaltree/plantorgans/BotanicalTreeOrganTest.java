package org.gitsokolek.botanicaltree.plantorgans;

import org.gitsokolek.basicgenerictree.LastNode;
import org.gitsokolek.botanicaltree.plantorgans.interfaces.PlantOrgans;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class BotanicalTreeOrganTest {

	private static BotanicalTreeOrganData meta(String name, PlantOrgans kind) {
		return new BotanicalTreeOrganData(name, kind) {};
	}

	@Test
	void grow_printsHierarchy_andIndentMatchesDepth() throws Exception {
		var trunk = new BotanicalTreeOrgan(meta("TRUNK[d0]", PlantOrgans.SHOOT.TRUNK));
		var b1    = new BotanicalTreeOrgan(meta("BRANCH[T.1][d1]", PlantOrgans.SHOOT.BRANCH));
		var leaf  = new BotanicalTreeLastOrgan(meta("LEAF[T.1.1][d1]", PlantOrgans.LEAF.BROAD_LEAF));

		assertTrue(trunk.attach(b1));
		assertTrue(b1.attach(leaf));

		String out = capture(trunk);
		List<String> lines = Arrays.stream(out.split("\\R")).filter(s -> !s.isBlank()).toList();
		assertTrue(lines.stream().anyMatch(s -> s.contains("TRUNK[d0]")), "Root line should be printed");
		lines.forEach(BotanicalTreeOrganTest::assertIndentMatchesDepthTag);
	}

	@Test
	void childrenView_containsAttachedChildren() {
		var trunk = new BotanicalTreeOrgan(meta("TRUNK[d0]", PlantOrgans.SHOOT.TRUNK));
		var b     = new BotanicalTreeOrgan(meta("BRANCH[T.1][d1]", PlantOrgans.SHOOT.BRANCH));
		var l     = new BotanicalTreeLastOrgan(meta("LEAF[T.1.1][d1]", PlantOrgans.LEAF.ACICULAR_LEAF));
		assertTrue(trunk.attach(b));
		assertTrue(trunk.attach(l));
		List<? extends LastNode> view = trunk.childrenView();
		assertEquals(2, view.size(), "childrenView should expose both children");
	}

	private static String capture(BotanicalTreeOrgan root) throws Exception {
		PrintStream original = System.out;
		String out;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
			 PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8)) {
			System.setOut(ps);
			root.grow();
			ps.flush();
			out = baos.toString(StandardCharsets.UTF_8);
		} finally {
			System.setOut(original);
		}
		return out;
	}

	private static final Pattern DEPTH = Pattern.compile("\\[d(\\d+)]");

	private static void assertIndentMatchesDepthTag(String line) {
		int spaces = 0;
		while (spaces < line.length() && line.charAt(spaces) == ' ') spaces++;
		var m = DEPTH.matcher(line);
		if (m.find()) {
			int depthTag = Integer.parseInt(m.group(1));
			int expected = depthTag + (line.contains("LEAF[") ? 1 : 0);
			final int indent = spaces;
			assertEquals(expected, indent,
						 () -> "Indent (" + indent + ") != expected (" + expected + ") for line: " + line);
		} else {
			fail("Line lacks [dX] tag: " + line);
		}
	}
}
