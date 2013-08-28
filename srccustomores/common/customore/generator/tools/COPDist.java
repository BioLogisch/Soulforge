package customore.generator.tools;

import java.util.Random;

import customore.generator.tools.CODistributionSettingMap.Copyable;


public class COPDist implements Copyable<COPDist>
{
    public float mean;
    public float range;
    public Type type;

    public enum Type
    {
        uniform,
        normal;
    }

    public COPDist(float mean, float range, Type type)
    {
        this.set(mean, range, type);
    }

    public COPDist(float mean, float range)
    {
        this.set(mean, range, Type.uniform);
    }

    public COPDist()
    {
        this.set(0.0F, 0.0F, Type.uniform);
    }

    public void copyFrom(COPDist source)
    {
        this.mean = source.mean;
        this.range = source.range;
        this.type = source.type;
    }

    public COPDist set(float mean, float range, Type type)
    {
        this.mean = mean;
        this.range = range >= 0.0F ? range : -range;
        this.type = type;
        return this;
    }

    public float getMax()
    {
        return this.mean + this.range;
    }

    public float getMin()
    {
        return this.mean - this.range;
    }

    public float getValue(Random rand)
    {
        if (this.range == 0.0F)
        {
            return this.mean;
        }
        else
        {
            switch (this.type)
            {
                case uniform:
                    return (rand.nextFloat() * 2.0F - 1.0F) * this.range + this.mean;

                case normal:
                    float value = (float)rand.nextGaussian() / 2.5F;

                    if (value < -1.0F)
                    {
                        value = -1.0F;
                    }
                    else if (value > 1.0F)
                    {
                        value = 1.0F;
                    }

                    return value * this.range + this.mean;

                default:
                    return 0.0F;
            }
        }
    }

    public int getIntValue(Random rand)
    {
        float fval = this.getValue(rand);
        int ival = (int)fval;
        fval -= (float)ival;

        if (fval > 0.0F && fval > rand.nextFloat())
        {
            ++ival;
        }
        else if (fval < 0.0F && -fval > rand.nextFloat())
        {
            --ival;
        }

        return ival;
    }

    public String toString()
    {
        return String.format("%f +- %f %s", new Object[] {Float.valueOf(this.mean), Float.valueOf(this.range), this.type.name()});
    }
}
