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

package com.sk89q.worldedit.blocks;

import static com.google.common.base.Preconditions.checkNotNull;

import com.sk89q.jnbt.CompoundTag;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockStateHolder;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.math.BlockVector3;

/**
 * A implementation of a lazy block for {@link Extent#getLazyBlock(Vector)}
 * that takes the block's ID and metadata, but will defer loading of NBT
 * data until time of access.
 *
 * <p>NBT data is later loaded using a call to {@link Extent#getBlock(Vector)}
 * with a stored {@link Extent} and location.</p>
 *
 * <p>All mutators on this object will throw an
 * {@link UnsupportedOperationException}.</p>
 */
public class LazyBlock extends BaseBlock {

    private final Extent extent;
    private final BlockVector3 position;
    private boolean loaded = false;

    /**
     * Create a new lazy block.
     *
     * @param type the block type
     * @param extent the extent to later load the full block data from
     * @param position the position to later load the full block data from
     */
    public LazyBlock(BlockType type, Extent extent, BlockVector3 position) {
        super(type);
        checkNotNull(extent);
        checkNotNull(position);
        this.extent = extent;
        this.position = position;
    }

    /**
     * Create a new lazy block.
     *
     * @param state the block state
     * @param extent the extent to later load the full block data from
     * @param position the position to later load the full block data from
     */
    public LazyBlock(BlockState state, Extent extent, BlockVector3 position) {
        super(state);
        checkNotNull(extent);
        checkNotNull(position);
        this.extent = extent;
        this.position = position;
    }

    @Override
    public CompoundTag getNbtData() {
        if (!loaded) {
            BaseBlock loadedBlock = extent.getFullBlock(position);
            this.nbtData = loadedBlock.getNbtData();
            loaded = true;
        }
        return super.getNbtData();
    }

    @Override
    public void setNbtData(CompoundTag nbtData) {
        throw new UnsupportedOperationException("This object is immutable");
    }

}
