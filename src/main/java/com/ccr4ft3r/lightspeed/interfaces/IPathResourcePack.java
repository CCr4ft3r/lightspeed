package com.ccr4ft3r.lightspeed.interfaces;

import net.minecraft.server.packs.PackResources;
import net.minecraftforge.forgespi.locating.IModFile;

public interface IPathResourcePack extends PackResources, IPackResources {
    void setModFile(IModFile modFile);

}