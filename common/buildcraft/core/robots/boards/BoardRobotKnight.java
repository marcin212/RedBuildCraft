/**
 * Copyright (c) 2011-2014, SpaceToad and the BuildCraft Team
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.core.robots.boards;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

import buildcraft.api.boards.RedstoneBoardRobot;
import buildcraft.api.boards.RedstoneBoardRobotNBT;
import buildcraft.api.robots.AIRobot;
import buildcraft.api.robots.EntityRobotBase;
import buildcraft.core.inventory.filters.IStackFilter;
import buildcraft.core.robots.AIRobotAttack;
import buildcraft.core.robots.AIRobotFetchAndEquipItemStack;
import buildcraft.core.robots.AIRobotFindMob;
import buildcraft.core.robots.AIRobotGotoSleep;

public class BoardRobotKnight extends RedstoneBoardRobot {

	public BoardRobotKnight(EntityRobotBase iRobot) {
		super(iRobot);
	}

	@Override
	public RedstoneBoardRobotNBT getNBTHandler() {
		return BoardRobotKnightNBT.instance;
	}

	@Override
	public final void update() {
		if (robot.getItemInUse() == null) {
			startDelegateAI(new AIRobotFetchAndEquipItemStack(robot, new IStackFilter() {
				@Override
				public boolean matches(ItemStack stack) {
					return stack.getItem() instanceof ItemSword;
				}
			}));
		} else {
			startDelegateAI(new AIRobotFindMob(robot, 250, robot.getAreaToWork()));
		}
	}

	@Override
	public void delegateAIEnded(AIRobot ai) {
		if (ai instanceof AIRobotFetchAndEquipItemStack) {
			if (robot.getItemInUse() == null) {
				startDelegateAI(new AIRobotGotoSleep(robot));
			}
		} else if (ai instanceof AIRobotFindMob) {
			AIRobotFindMob mobAI = (AIRobotFindMob) ai;

			if (mobAI.target != null) {
				startDelegateAI(new AIRobotAttack(robot, ((AIRobotFindMob) ai).target));
			} else {
				startDelegateAI(new AIRobotGotoSleep(robot));
			}
		}
	}

}
