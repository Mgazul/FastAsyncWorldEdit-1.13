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

package com.sk89q.worldedit.function.pattern;

import com.sk89q.minecraft.util.commands.Link;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.command.UtilityCommands;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.NullExtent;
import com.sk89q.worldedit.internal.expression.runtime.Return;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockStateHolder;
import com.sk89q.worldedit.world.block.BlockState;

/**
 * Returns a {@link BlockStateHolder} for a given position.
 */
@Link(clazz = UtilityCommands.class, value = "patterns")
public interface Pattern{

//    @Override
//    default BaseBlock next(BlockVector3 position) {
//        return new BaseBlock(apply(position));
//    }
//
//    @Override
//    default BaseBlock next(int x, int y, int z) {
//        return new BaseBlock(apply(BlockVector3.at(x, y, z)));
//    }

    /**
     * Return a {@link BlockStateHolder} for the given position.
     *
     * @param position the position
     * @return a block
     */
    BaseBlock apply(BlockVector3 position);

    default boolean apply(Extent extent, BlockVector3 get, BlockVector3 set) throws WorldEditException {
        return extent.setBlock(set, apply(get));
    }
}
