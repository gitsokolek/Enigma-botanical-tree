package org.gitsokolek.botanicaltree.plantorgans;

import org.gitsokolek.botanicaltree.plantorgans.interfaces.PlantOrgans;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class BotanicalTreeLastOrganTest {

	@Test
	void grow_printsExpectedLine() throws Exception {
		var data = new BotanicalTreeOrganData("Leaf-1", PlantOrgans.LEAF.BROAD_LEAF) {};
		var leaf = new BotanicalTreeLastOrgan(data);

		PrintStream originalOut = System.out;
		String out;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
			 PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8)) {
			System.setOut(ps);
			leaf.grow();
			ps.flush();
			out = baos.toString(StandardCharsets.UTF_8);
		} finally {
			System.setOut(originalOut);
		}

		assertTrue(out.contains("Growing Leaf-1"), "Output should contain object name");
		assertTrue(out.contains("BROAD_LEAF") || out.contains("ACICULAR_LEAF"), "Output should contain organ type");
	}
}
