package mekanism.client.model;

import mekanism.common.Mekanism;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ExistingFileHelper;

public class MekanismBlockModelProvider extends BaseBlockModelProvider {

    public MekanismBlockModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Mekanism.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

    }
}