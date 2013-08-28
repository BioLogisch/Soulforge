package customore.generator;

import java.util.Random;

import net.minecraft.src.MathHelper;
import net.minecraft.src.StructureBoundingBox;
import net.minecraft.src.World;
import customore.generator.tools.CODistributionSettingMap;
import customore.generator.tools.CODistributionSettingMap.DistributionSetting;
import customore.generator.tools.CONoiseGenerator;
import customore.generator.tools.COPDist;
import customore.generator.tools.COPDist.Type;
import customore.generator.tools.COTransform;
import customore.util.COLogger;



public class COMapGenCloud extends COMapGenOreDistribution
{
    @DistributionSetting(
            name = "CloudRadius",
            info = "Cloud horizontal radius, in meters"
    )
    public final COPDist clRadius = new COPDist(25.0F, 10.0F);
    @DistributionSetting(
            name = "CloudThickness",
            info = "Cloud thickness (vertical radius), in meters"
    )
    public final COPDist clThickness = new COPDist(14.0F, 6.0F);
    @DistributionSetting(
            name = "CloudSizeNoise",
            info = "Noise level added to cloud radius and thickness"
    )
    public final COPDist clSizeNoise = new COPDist(0.2F, 0.0F);
    @DistributionSetting(
            name = "CloudHeight",
            info = "Height of cloud, in meters"
    )
    public final COPDist clHeight;
    @DistributionSetting(
            name = "CloudInclination",
            info = "Cloud angle from horizontal plane, in radians"
    )
    public final COPDist clInclination;
    @DistributionSetting(
            name = "OreRadiusMult",
            info = "Radius multiplier for individual ore blocks"
    )
    public final COPDist orRadiusMult;
    @DistributionSetting(
            name = "OreDensity",
            info = "Density multiplier for individual ore blocks"
    )
    public final COPDist orDensity;
    @DistributionSetting(
            name = "OreVolumeNoiseCutoff",
            info = "Minimum threshold for density noise on individual ore blocks"
    )
    public final COPDist orVolumeNoiseCutoff;
    protected static final CODistributionSettingMap _cloudSettingsMap = new CODistributionSettingMap(COMapGenCloud.class);

    public COMapGenCloud(int distributionID, boolean canGenerate)
    {
        super(_cloudSettingsMap, distributionID, canGenerate);
        this.clHeight = new COPDist(32.0F, 16.0F, Type.normal);
        this.clInclination = new COPDist(0.0F, 0.35F);
        this.orRadiusMult = new COPDist(1.0F, 0.1F);
        this.orDensity = new COPDist(0.1F, 0.0F);
        this.orVolumeNoiseCutoff = new COPDist(0.5F, 0.0F);
        this.name = "Cloud_" + distributionID;
        this.frequency.set(0.001F, 0.0F, Type.uniform);
    }

    public boolean validate() throws IllegalStateException
    {
        float r = Math.max(this.clRadius.getMax(), this.clThickness.getMax());
        r *= 1.0F + this.clSizeNoise.getMax() * 2.0F;
        r *= this.orRadiusMult.getMax();
        super.range = (int)(r + 15.9999F) / 16;
        return super.validate();
    }

    public Component generateStructure(StructureGroup structureGroup, Random random)
    {
    	COLogger.log.fine("Gen Cloud");
        float clX = (random.nextFloat() + (float)structureGroup.chunkX) * 16.0F;
        float clY = this.clHeight.getValue(random);
        float clZ = (random.nextFloat() + (float)structureGroup.chunkZ) * 16.0F;

        if (!structureGroup.canPlaceComponentAt(0, clX, clY, clZ, random))
        {
            return null;
        }
        else
        {
            COTransform clMat = new COTransform();
            clMat.translate(clX, clY, clZ);
            clMat.rotateZInto(0.0F, 1.0F, 0.0F);
            clMat.rotateZ(random.nextFloat() * ((float)Math.PI * 2F));
            clMat.rotateY(this.clInclination.getValue(random));
            clMat.scale(this.clRadius.getValue(random), this.clRadius.getValue(random), this.clThickness.getValue(random));
            DiffuseCloudComponent cloud = new DiffuseCloudComponent(this, structureGroup, clMat, random);
            structureGroup.addComponent(cloud, (Component)null);
            return cloud;
        }
    }
    
    private class DiffuseCloudComponent extends Component
    {
        protected final COTransform mat;
        protected final COTransform invMat;
        protected final CONoiseGenerator noiseGen;
        protected final float sizeNoiseMagnitude;
        protected final int noiseLevels;

        public DiffuseCloudComponent(COMapGenCloud var1, StructureGroup structureGroup, COTransform transform, Random random)
        {
            super(structureGroup);
            this.noiseGen = new CONoiseGenerator(random);
            this.sizeNoiseMagnitude = Math.abs(var1.clSizeNoise.getValue(random));
            float rMax = (1.0F + this.sizeNoiseMagnitude * 2.0F) * var1.orRadiusMult.getMax();

            if (rMax < 0.0F)
            {
                rMax = 0.0F;
            }

            float[] bb = new float[] { -rMax, -rMax, -rMax, rMax, rMax, rMax};
            transform.transformBB(bb);
            super.boundingBox = new StructureBoundingBox(MathHelper.floor_float(bb[0]), MathHelper.floor_float(bb[1]), MathHelper.floor_float(bb[2]), MathHelper.floor_float(bb[3]) + 1, MathHelper.floor_float(bb[4]) + 1, MathHelper.floor_float(bb[5]) + 1);
            float maxSize = (float)Math.max(super.boundingBox.getXSize(), Math.max(super.boundingBox.getYSize(), super.boundingBox.getZSize())) * 0.2F;
            this.noiseLevels = maxSize <= 1.0F ? 0 : (int)(Math.log((double)maxSize) / Math.log(2.0D) + 0.5D);
            this.mat = transform.clone();

            if (transform.determinant() != 0.0F)
            {
                this.invMat = transform.inverse();
            }
            else
            {
                this.invMat = null;
            }
        }

        public float getNoise(float x, float y, float z)
        {
            double noise = 0.0D;

            for (int i = 0; i < this.noiseLevels; ++i)
            {
                float im = (float)(1 << i);
                noise += (double)(1.0F / im) * this.noiseGen.noise((double)(x * im), (double)(y * im), (double)(z * im));
            }

            return (float)noise;
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox bounds)
        {
            if (this.invMat == null)
            {
                return true;
            }
            else
            {
                float maxR = Math.max(orRadiusMult.getMax(), 0.0F);
                float minR = Math.max(orRadiusMult.getMin(), 0.0F);
                float maxNoisyR2 = maxR * (1.0F + this.sizeNoiseMagnitude * 2.0F);
                float minNoisyR2 = minR * (1.0F - this.sizeNoiseMagnitude * 2.0F);
                maxNoisyR2 *= maxNoisyR2;
                minNoisyR2 *= minNoisyR2;
                float[] pos = new float[3];

                for (int x = Math.max(super.boundingBox.minX, bounds.minX); x <= Math.min(super.boundingBox.maxX, bounds.maxX); ++x)
                {
                    for (int y = Math.max(super.boundingBox.minY, bounds.minY); y <= Math.min(super.boundingBox.maxY, bounds.maxY); ++y)
                    {
                        for (int z = Math.max(super.boundingBox.minZ, bounds.minZ); z <= Math.min(super.boundingBox.maxZ, bounds.maxZ); ++z)
                        {
                            pos[0] = (float)x + 0.5F;
                            pos[1] = (float)y + 0.5F;
                            pos[2] = (float)z + 0.5F;
                            this.invMat.transformVector(pos);
                            float r2 = pos[0] * pos[0] + pos[1] * pos[1] + pos[2] * pos[2];

                            if (r2 <= maxNoisyR2)
                            {
                                if (r2 > minNoisyR2)
                                {
                                    float r = MathHelper.sqrt_float(r2);
                                    float mult = 1.0F;

                                    if (r > 0.0F)
                                    {
                                        mult += this.sizeNoiseMagnitude * this.getNoise(pos[0] / r, pos[1] / r, pos[2] / r);
                                    }
                                    else
                                    {
                                        mult += this.sizeNoiseMagnitude * this.getNoise(0.0F, 0.0F, 0.0F);
                                    }

                                    if (mult <= 0.0F)
                                    {
                                        continue;
                                    }

                                    r /= mult;

                                    if (r > maxR || r > minR && r > orRadiusMult.getValue(random))
                                    {
                                        continue;
                                    }
                                }

                                if (orVolumeNoiseCutoff.getMin() <= 1.0F && (orVolumeNoiseCutoff.getMax() <= 0.0F || (this.getNoise(pos[0], pos[1], pos[2]) + 1.0F) / 2.0F >= orVolumeNoiseCutoff.getValue(random)) && orDensity.getIntValue(random) >= 1)
                                {
                                    this.attemptPlaceBlock(world, random, x, y, z, bounds);
                                }
                            }
                        }
                    }
                }

                super.addComponentParts(world, random, bounds);
                return true;
            }
        }
    }

}
