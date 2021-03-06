/*
 * WorldEdit, a Minecraft world manipulation toolkit
 * Copyright (C) sk89q <http://www.sk89q.com>
 * Copyright (C) WorldEdit team and contributors
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.sk89q.worldedit.command.tool;

import com.boydti.fawe.config.BBC;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extension.platform.Platform;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.block.BaseBlock;

/**
 * A tool that can place (or remove) blocks at a distance.
 */
public class LongRangeBuildTool extends BrushTool implements DoubleActionTraceTool {

    private Pattern primary;
    private Pattern secondary;

    public LongRangeBuildTool(Pattern secondary, Pattern primary) {
        super("worldedit.tool.lrbuild");
        this.primary = primary;
        this.secondary = secondary;
    }

    @Override
    public boolean canUse(Actor player) {
        return player.hasPermission("worldedit.tool.lrbuild");
    }

    @Override
    public boolean actSecondary(Platform server, LocalConfiguration config, Player player, LocalSession session) {
        Location pos = getTargetFace(player);
        if (pos == null) return false;
        EditSession eS = session.createEditSession(player);
        try {
//            eS.disableBuffering();
            BlockVector3 blockPoint = pos.toBlockPoint();
            BaseBlock applied = secondary.apply(blockPoint);
            if (applied.getBlockType().getMaterial().isAir()) {
                eS.setBlock(blockPoint, secondary);
            } else {
                eS.setBlock(pos.subtract(pos.getDirection()).toBlockPoint(), secondary);
            }
            return true;
        } catch (MaxChangedBlocksException e) {
            // one block? eat it
        }
        return false;
    }

    @Override
    public boolean actPrimary(Platform server, LocalConfiguration config, Player player, LocalSession session) {
        Location pos = getTargetFace(player);
        if (pos == null) return false;
        EditSession eS = session.createEditSession(player);

        try {
//            eS.disableBuffering();
            BlockVector3 blockPoint = pos.toBlockPoint();
            BaseBlock applied = primary.apply(blockPoint);
            if (applied.getBlockType().getMaterial().isAir()) {
                eS.setBlock(blockPoint, primary);
            } else {
                eS.setBlock(pos.subtract(pos.getDirection()).toBlockPoint(), primary);
            }
            return true;
        } catch (MaxChangedBlocksException e) {
            // one block? eat it
        }
        return true;
    }

    public Location getTargetFace(Player player) {
        Location target = player.getBlockTraceFace(getRange(), true);

        if (target == null) {
            BBC.NO_BLOCK.send(player);
            return null;
        }

        return target;
    }

}