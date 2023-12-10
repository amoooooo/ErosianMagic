package aster.amo.erosianmagic.util;

import com.mojang.blaze3d.platform.Window;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.awt.*;

public class SpaceHelper {
    public static Vector3f worldToScreenSpace(Vec3 pos, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        Camera camera = mc.gameRenderer.getMainCamera();
        Vec3 cameraPosition = camera.getPosition();
        Vector3f position = new Vector3f(
                (float) (cameraPosition.x - pos.x),
                (float) (cameraPosition.y - pos.y),
                (float) (cameraPosition.z - pos.z)
        );
        Quaternionf cameraRotation = camera.rotation();
        cameraRotation.conjugate();
        //cameraRotation = restrictAxis(new Vec3(1, 1, 0), cameraRotation);
        cameraRotation.transform(position);

        // Account for view bobbing
        if (mc.options.bobView().get() && mc.getCameraEntity() instanceof Player) {
            Player player = (Player) mc.getCameraEntity();
            float playerStep = player.walkDist - player.walkDistO;
            float stepSize = -(player.walkDist + playerStep * partialTicks);
            float viewBob = Mth.lerp(partialTicks, player.oBob, player.bob);
            Quaternionf bobXRotation =
                    (Axis.XP.rotationDegrees(Math.abs(Mth.cos((float) (stepSize * Math.PI)) - 0.2f) * viewBob * 5f));
            Quaternionf bobZRotation = Axis.ZP.rotationDegrees(Mth.sin((float) (stepSize * Math.PI)) * viewBob * 3f);
            bobXRotation.conjugate();
            bobZRotation.conjugate();
            bobXRotation.transform(position);
            bobZRotation.transform(position);
            position.add(
                    Mth.sin((float) (stepSize * Math.PI)) * viewBob * 0.5f,
                    Math.abs(Mth.cos((float) (stepSize * Math.PI)) * viewBob),
                    0f
            );
        }
        Window window = mc.getWindow();
        float screenSize = (float) (window.getGuiScaledHeight() / 2f / position.z() / Math.tan(
                        Math.toRadians(
                                mc.gameRenderer.getFov(
                                        camera,
                                        partialTicks,
                                        true
                                ) / 2f
                        )
                ));
        position.mul(-screenSize, -screenSize, 1f);
        position.add(window.getGuiScaledWidth() / 2f, window.getGuiScaledWidth() / 2f, 0f);
        return position;
    }
}
