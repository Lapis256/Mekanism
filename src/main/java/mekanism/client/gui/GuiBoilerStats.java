package mekanism.client.gui;

import java.util.Collections;
import mekanism.client.gui.element.GuiGraph;
import mekanism.client.gui.element.GuiHeatInfo;
import mekanism.client.gui.element.tab.GuiBoilerTab;
import mekanism.client.gui.element.tab.GuiBoilerTab.BoilerTab;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.boiler.SynchronizedBoilerData;
import mekanism.common.inventory.container.ContainerNull;
import mekanism.common.tile.TileEntityBoilerCasing;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import mekanism.common.util.UnitDisplayUtils;
import mekanism.common.util.UnitDisplayUtils.TemperatureUnit;
import mekanism.common.util.text.TextComponentUtil;
import mekanism.common.util.text.Translation;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiBoilerStats extends GuiMekanismTile<TileEntityBoilerCasing> {

    private final GuiGraph boilGraph;
    private final GuiGraph maxGraph;

    public GuiBoilerStats(PlayerInventory inventory, TileEntityBoilerCasing tile) {
        super(tile, new ContainerNull(inventory.player, tile));
        ResourceLocation resource = getGuiLocation();
        addGuiElement(new GuiBoilerTab(this, tileEntity, BoilerTab.MAIN, resource));
        addGuiElement(new GuiHeatInfo(() -> {
            TemperatureUnit unit = TemperatureUnit.values()[MekanismConfig.current().general.tempUnit.val().ordinal()];
            String environment = UnitDisplayUtils.getDisplayShort(tileEntity.getLastEnvironmentLoss() * unit.intervalSize, false, unit);
            return Collections.singletonList(TextComponentUtil.build(Translation.of("mekanism.gui.dissipated"), ": " + environment + "/t"));
        }, this, resource));
        addGuiElement(boilGraph = new GuiGraph(this, resource, 8, 83, 160, 36, data ->
              TextComponentUtil.build(Translation.of("mekanism.gui.boilRate"), ": " + data + " mB/t")));
        addGuiElement(maxGraph = new GuiGraph(this, resource, 8, 122, 160, 36, data ->
              TextComponentUtil.build(Translation.of("mekanism.gui.maxBoil"), ": " + data + " mB/t")));
        maxGraph.enableFixedScale((int) ((tileEntity.getSuperheatingElements() * MekanismConfig.current().general.superheatingHeatTransfer.val()) /
                                         SynchronizedBoilerData.getHeatEnthalpy()));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        drawCenteredText(TextComponentUtil.build(Translation.of("mekanism.gui.boilerStats")), 0, xSize, 6, 0x404040);
        drawString(TextComponentUtil.build(Translation.of("mekanism.gui.maxWater"), ": " + tileEntity.clientWaterCapacity + " mB"), 8, 26, 0x404040);
        drawString(TextComponentUtil.build(Translation.of("mekanism.gui.maxSteam"), ": " + tileEntity.clientSteamCapacity + " mB"), 8, 35, 0x404040);
        drawString(TextComponentUtil.build(Translation.of("mekanism.gui.heatTransfer")), 8, 49, 0x797979);
        drawString(TextComponentUtil.build(Translation.of("mekanism.gui.superheaters"), ": " + tileEntity.getSuperheatingElements()), 14, 58, 0x404040);
        int boilCapacity = (int) (tileEntity.getSuperheatingElements() * MekanismConfig.current().general.superheatingHeatTransfer.val() / SynchronizedBoilerData.getHeatEnthalpy());
        drawString(TextComponentUtil.build(Translation.of("mekanism.gui.boilCapacity"), ": " + boilCapacity + " mB/t"), 8, 72, 0x404040);
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    public void tick() {
        super.tick();
        boilGraph.addData(tileEntity.getLastBoilRate());
        maxGraph.addData(tileEntity.getLastMaxBoil());
    }

    @Override
    protected ResourceLocation getGuiLocation() {
        return MekanismUtils.getResource(ResourceType.GUI, "GuiBoilerStats.png");
    }
}