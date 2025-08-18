package org.gitsokolek.botanicaltree;

import org.gitsokolek.botanicaltree.plantorgans.BotanicalTreeOrgan;
import org.gitsokolek.botanicaltree.plantorgans.interfaces.PlantOrgans;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class TreeFactoryTest {

	@Test
	void createRandomTree_sameSeed_isDeterministic() {
		long seed = 42L;
		BotanicalTreeOrgan t1 = TreeFactory.createRandomTree(4, 3, 3, PlantOrgans.LEAF.BROAD_LEAF, seed);
		BotanicalTreeOrgan t2 = TreeFactory.createRandomTree(4, 3, 3, PlantOrgans.LEAF.BROAD_LEAF, seed);
		String out1 = String.join("\n", captureGrow(t1));
		String out2 = String.join("\n", captureGrow(t2));
		assertEquals(out1, out2, "Same seed should produce identical grow() output");
	}

	@Test
	void createRandomTree_broadLeaf_hasOnlyBroadLeaves() {
		BotanicalTreeOrgan t = TreeFactory.createRandomTree(4, 3, 3, PlantOrgans.LEAF.BROAD_LEAF);
		List<String> out = captureGrow(t);
		boolean anyBroad = out.stream().anyMatch(l -> l.contains("of type BROAD_LEAF"));
		if (anyBroad) {
			assertFalse(out.stream().anyMatch(l -> l.contains("of type ACICULAR_LEAF")),
						"Tree with BROAD_LEAF must not contain ACICULAR_LEAF");
		}
	}

	@Test
	void createRandomTree_conifer_hasOnlyAcicularLeaves() {
		BotanicalTreeOrgan t = TreeFactory.createRandomTree(4, 3, 3, PlantOrgans.LEAF.ACICULAR_LEAF);
		List<String> out = captureGrow(t);
		boolean anyAcicular = out.stream().anyMatch(l -> l.contains("of type ACICULAR_LEAF"));
		if (anyAcicular) {
			assertFalse(out.stream().anyMatch(l -> l.contains("of type BROAD_LEAF")),
						"Tree with ACICULAR_LEAF must not contain BROAD_LEAF");
		}
	}

	@Test
	void createRandomTree_indentMatchesDepthTag_andHasTrunk() {
		BotanicalTreeOrgan t = TreeFactory.createRandomTree(4, 3, 3, PlantOrgans.LEAF.BROAD_LEAF, 123L);
		List<String> out = captureGrow(t);
		assertTrue(out.stream().anyMatch(l -> l.contains("TRUNK[d0]")), "TRUNK[d0] should be present");
		out.forEach(TreeFactoryTest::assertIndentMatchesDepthTag);
	}

	@Test
	void createRandomTree_invalidArgs_throw() {
		assertThrows(IllegalArgumentException.class,
					 () -> TreeFactory.createRandomTree(0, 1, 1, PlantOrgans.LEAF.BROAD_LEAF));
		assertThrows(IllegalArgumentException.class,
					 () -> TreeFactory.createRandomTree(1, -1, 1, PlantOrgans.LEAF.BROAD_LEAF));
		assertThrows(IllegalArgumentException.class,
					 () -> TreeFactory.createRandomTree(1, 1, -1, PlantOrgans.LEAF.BROAD_LEAF));
	}

	private static List<String> captureGrow(BotanicalTreeOrgan root) {
		PrintStream original = System.out;
		String all;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8)) {
			System.setOut(ps);
			root.grow();
			ps.flush();
			all = baos.toString(StandardCharsets.UTF_8);
		} finally {
			System.setOut(original);
		}
		String[] lines = all.split("\\R");
		List<String> result = new ArrayList<>();
		for (String l : lines) if (!l.isBlank()) result.add(l);
		return result;
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
