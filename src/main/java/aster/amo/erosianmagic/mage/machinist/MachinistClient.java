package aster.amo.erosianmagic.mage.machinist;

import aster.amo.erosianmagic.client.ErosianMagicClient;
import com.flansmod.common.actions.EActionResult;

import java.util.Objects;

public class MachinistClient {
    public static EActionResult shouldCancelShoot() {
        if (!Objects.equals(ErosianMagicClient.CLASS, "Machinist")){
            return EActionResult.Wait;
        } else {
            return EActionResult.CanProcess;
        }
    }
}
