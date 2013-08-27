package net.minecraft.src.customore.generator;


import java.util.Map;

import net.minecraft.src.World;

public interface ICOOreDistribution
{
    Map<String,Object> getDistributionSettings();

    Object getDistributionSetting(String var1);

    void setDistributionSetting(String var1, Object var2) throws IllegalArgumentException, IllegalAccessException;

    void generate(World var1, int var2, int var3);

    void populate(World var1, int var2, int var3);

    void cull();

    void clear();

    boolean validate() throws IllegalStateException;

    void inheritFrom(ICOOreDistribution var1) throws IllegalArgumentException;
    
    public static interface IDistributionFactory
    {
        ICOOreDistribution createDistribution(int var1);
    }

    public enum StandardSettings
    {
        Name,
        Seed,
        OreBlock,
        ReplaceableBlock,
        TargetBiome,
        Parent;
    }

}
