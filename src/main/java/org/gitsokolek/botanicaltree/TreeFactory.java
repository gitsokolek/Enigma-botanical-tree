package org.gitsokolek.botanicaltree;

import org.gitsokolek.botanicaltree.plantorgans.BotanicalTreeLastOrgan;
import org.gitsokolek.botanicaltree.plantorgans.BotanicalTreeOrgan;
import org.gitsokolek.botanicaltree.plantorgans.BotanicalTreeOrganData;
import org.gitsokolek.botanicaltree.plantorgans.interfaces.PlantOrgans;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.random.RandomGenerator;

public final class TreeFactory {

	private TreeFactory() { }

	public static BotanicalTreeOrgan createRandomTree(
			int maxDepth,
			int maxBranchesPerNode,
			int maxLeavesPerBranch,
			PlantOrgans.LEAF leafType
													 ) {
		return createRandomTree(RandomGenerator.getDefault(),
								maxDepth, maxBranchesPerNode, maxLeavesPerBranch, leafType);
	}

	public static BotanicalTreeOrgan createRandomTree(
			int maxDepth,
			int maxBranchesPerNode,
			int maxLeavesPerBranch,
			PlantOrgans.LEAF leafType,
			long seed
													 ) {
		return createRandomTree(new java.util.Random(seed),
								maxDepth, maxBranchesPerNode, maxLeavesPerBranch, leafType);
	}


	private static BotanicalTreeOrgan createRandomTree(
			RandomGenerator rng,
			int maxDepth,
			int maxBranchesPerNode,
			int maxLeavesPerBranch,
			PlantOrgans.LEAF leafType
													  ) {
		validateArgs(maxDepth, maxBranchesPerNode, maxLeavesPerBranch, leafType);

		BiFunction<String, PlantOrgans, BotanicalTreeOrganData> meta = metaFactory();
		BotanicalTreeOrgan trunk = createTrunk(meta);

		List<Entry> level = new ArrayList<>();
		level.add(new Entry(trunk, "T", 0));

		for (int depth = 1; depth <= maxDepth; depth++) {
			level = expandLevel(rng, level, depth, maxBranchesPerNode, maxLeavesPerBranch, leafType, meta);
			if (level.isEmpty()) break;
		}
		return trunk;
	}

	private static void validateArgs(int maxDepth, int maxBranchesPerNode, int maxLeavesPerBranch,
									 PlantOrgans.LEAF leafType) {
		if (maxDepth < 1) throw new IllegalArgumentException("maxDepth must be >= 1");
		if (maxBranchesPerNode < 0) throw new IllegalArgumentException("maxBranchesPerNode must be >= 0");
		if (maxLeavesPerBranch < 0) throw new IllegalArgumentException("maxLeavesPerBranch must be >= 0");
		Objects.requireNonNull(leafType, "leafType");
	}

	private static BiFunction<String, PlantOrgans, BotanicalTreeOrganData> metaFactory() {
		return (name, kind) -> new BotanicalTreeOrganData(name, kind) {};
	}

	private static BotanicalTreeOrgan createTrunk(BiFunction<String, PlantOrgans, BotanicalTreeOrganData> meta) {
		return new BotanicalTreeOrgan(meta.apply("TRUNK[d0]", PlantOrgans.SHOOT.TRUNK));
	}

	private static List<Entry> expandLevel(
			RandomGenerator rng,
			List<Entry> parents,
			int depth,
			int maxBranchesPerNode,
			int maxLeavesPerBranch,
			PlantOrgans.LEAF leafType,
			BiFunction<String, PlantOrgans, BotanicalTreeOrganData> meta
										  ) {
		List<Entry> next = new ArrayList<>();
		for (Entry parent : parents) {
			int branches = randomBranches(rng, maxBranchesPerNode);
			for (int b = 1; b <= branches; b++) {
				String path = parent.path + "." + b;

				BotanicalTreeOrgan branch = makeBranch(parent, path, depth, meta);
				int leaves = randomLeaves(rng, maxLeavesPerBranch);
				attachLeaves(branch, path, depth, leaves, leafType, meta);

				next.add(new Entry(branch, path, depth));
			}
		}
		return next;
	}

	private static int randomBranches(RandomGenerator rng, int maxBranchesPerNode) {
		return (maxBranchesPerNode == 0) ? 0 : 1 + rng.nextInt(maxBranchesPerNode);
	}

	private static int randomLeaves(RandomGenerator rng, int maxLeavesPerBranch) {
		return (maxLeavesPerBranch == 0) ? 0 : rng.nextInt(maxLeavesPerBranch + 1);
	}

	private static BotanicalTreeOrgan makeBranch(
			Entry parent, String path, int depth,
			BiFunction<String, PlantOrgans, BotanicalTreeOrganData> meta
												) {
		BotanicalTreeOrgan branch = new BotanicalTreeOrgan(
				meta.apply("BRANCH[" + path + "][d" + depth + "]", PlantOrgans.SHOOT.BRANCH)
		);
		parent.node.attach(branch);
		return branch;
	}

	private static void attachLeaves(
			BotanicalTreeOrgan branch,
			String path,
			int depth,
			int count,
			PlantOrgans.LEAF leafType,
			BiFunction<String, PlantOrgans, BotanicalTreeOrganData> meta
									) {
		for (int li = 1; li <= count; li++) {
			BotanicalTreeLastOrgan leaf = new BotanicalTreeLastOrgan(
					meta.apply("LEAF[" + path + "." + li + "][d" + depth + "]", leafType)
			);
			branch.attach(leaf);
		}
	}

	private static final class Entry {
		final BotanicalTreeOrgan node;
		final String path;
		final int depth;
		Entry(BotanicalTreeOrgan node, String path, int depth) {
			this.node = node;
			this.path = path;
			this.depth = depth;
		}
	}
}
