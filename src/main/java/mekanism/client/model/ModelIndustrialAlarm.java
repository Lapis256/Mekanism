package mekanism.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import javax.annotation.Nonnull;
import mekanism.client.render.MekanismRenderType;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.Mekanism;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

public class ModelIndustrialAlarm extends Model {

    private static final ResourceLocation TEXTURE = Mekanism.rl("render/industrial_alarm.png");
    private static final ResourceLocation TEXTURE_ACTIVE = Mekanism.rl("render/industrial_alarm_active.png");
    private final RenderType RENDER_TYPE = MekanismRenderType.mekStandard(TEXTURE);
    private final RenderType RENDER_TYPE_ACTIVE = MekanismRenderType.mekStandard(TEXTURE_ACTIVE);

    ModelRenderer base;
    ModelRenderer bulb;
    ModelRenderer light_box;
    ModelRenderer aura;

    public ModelIndustrialAlarm() {
        super(RenderType::getEntitySolid);
        textureWidth = 64;
        textureHeight = 64;

        base = new ModelRenderer(this, 0, 9);
        base.addBox(-3F, 0F, -3F, 6, 1, 6);
        base.setRotationPoint(0F, 0F, 0F);
        base.setTextureSize(64, 64);
        setRotationAngle(base, 0F, 0F, 0F);
        bulb = new ModelRenderer(this, 16, 0);
        bulb.addBox(-1F, 1F, -1F, 2, 3, 2);
        bulb.setRotationPoint(0F, 0F, 0F);
        bulb.setTextureSize(64, 64);
        setRotationAngle(bulb, 0F, 0F, 0F);
        light_box = new ModelRenderer(this, 0, 0);
        light_box.addBox(-2F, 1F, -2F, 4, 4, 4);
        light_box.setRotationPoint(0F, 0F, 0F);
        light_box.setTextureSize(64, 64);
        setRotationAngle(light_box, 0F, 0F, 0F);
        aura = new ModelRenderer(this, 0, 16);
        aura.addBox(-6F, 2F, -1F, 12, 1, 2);
        aura.setRotationPoint(0F, 0F, 0F);
        aura.setTextureSize(64, 64);
        setRotationAngle(aura, 0F, 0F, 0F);
    }

    public void render(@Nonnull MatrixStack matrix, @Nonnull IRenderTypeBuffer renderer, int light, int overlayLight, boolean active, float rotation, boolean renderBase) {
        render(matrix, renderer.getBuffer(active ? RENDER_TYPE_ACTIVE : RENDER_TYPE), light, overlayLight, 1, 1, 1, 1, active, rotation, renderBase);
    }

    @Override
    public void render(@Nonnull MatrixStack matrix, @Nonnull IVertexBuilder vertexBuilder, int light, int overlayLight, float red, float green, float blue,
          float alpha) {
        render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha, false, 0, false);
    }

    public void render(@Nonnull MatrixStack matrix, @Nonnull IVertexBuilder vertexBuilder, int light, int overlayLight, float red, float green, float blue, float alpha,
          boolean active, float rotation, boolean renderBase) {
        if (renderBase) {
            base.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        }
        if (active) {
            setRotationAngle(aura, 0, (float) Math.toRadians(rotation), 0);
            setRotationAngle(bulb, 0, (float) Math.toRadians(rotation), 0);
        } else {
            setRotationAngle(aura, 0, 0, 0);
            setRotationAngle(bulb, 0, 0, 0);
        }
        float test = 0.3F + (Math.abs(((rotation * 2) % 360) - 180F) / 180F) * 0.7F;
        bulb.render(matrix, vertexBuilder, active ? MekanismRenderer.FULL_LIGHT : light, overlayLight, red, green, blue, test);
        light_box.render(matrix, vertexBuilder, active ? MekanismRenderer.FULL_LIGHT : light, overlayLight, red, green, blue, alpha);
        aura.render(matrix, vertexBuilder, MekanismRenderer.FULL_LIGHT, overlayLight, red, green, blue, test);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
