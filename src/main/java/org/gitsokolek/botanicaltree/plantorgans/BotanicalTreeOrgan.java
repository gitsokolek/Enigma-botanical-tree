package org.gitsokolek.botanicaltree.plantorgans;

import org.gitsokolek.basicgenerictree.interfaces.MetaData;
import org.gitsokolek.basicgenerictree.LastNode;
import org.gitsokolek.basicgenerictree.Node;

import java.util.ArrayDeque;
import java.util.Deque;

public class BotanicalTreeOrgan extends Node<BotanicalTreeOrganData>
{

	public BotanicalTreeOrgan(BotanicalTreeOrganData metaData)
	{
		super(metaData);
	}



	public void grow()
	{
		printGrowth(this, 0);

		Deque<Frame> stack = new ArrayDeque<>();


		for (LastNode<? extends MetaData> ch : childrenView())
		{
			if (ch instanceof Node<?> n)
			{
				stack.push(new Frame(n, 1));
			}
			else
			{
				printGrowth(ch, 1);
			}
		}


		while (!stack.isEmpty())
		{
			Frame f = stack.pop();

			printGrowth(f.node, f.depth);

			for (LastNode<? extends MetaData> ch : f.node.childrenView())
			{
				if (ch instanceof Node<?> n)
				{
					stack.push(new Frame(n, f.depth + 1));
				}
				else
				{
					printGrowth(ch, f.depth + 1);
				}
			}
		}
	}



	private static final class Frame
	{
		final Node<?> node;
		final int     depth;



		Frame(Node<?> node, int depth)
		{
			this.node  = node;
			this.depth = depth;
		}
	}



	private static void printGrowth(LastNode<? extends MetaData> ln, int depth)
	{
		var meta = ln.getMetaData();
		if (meta instanceof BotanicalTreeOrganData data)
		{
			System.out.println(" ".repeat(depth) + "Growing " + data.getName() + " of type " + data.getPlantOrgan());
		}
	}
}