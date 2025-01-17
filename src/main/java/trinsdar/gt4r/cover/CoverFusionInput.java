package trinsdar.gt4r.cover;

import muramasa.antimatter.cover.CoverInput;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import trinsdar.gt4r.Ref;

public class CoverFusionInput extends CoverInput {
    @Override
    public String getId() {
        return "fusion_input";
    }

    @Override
    public String getDomain() {
        return Ref.ID;
    }

    @Override
    public ResourceLocation getModel(Direction dir, Direction facing) {
        return getBasicModel();
    }
}
