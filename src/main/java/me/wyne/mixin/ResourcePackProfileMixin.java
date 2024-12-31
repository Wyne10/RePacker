package me.wyne.mixin;

import net.minecraft.resource.ResourcePackInfo;
import net.minecraft.resource.ResourcePackPosition;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ResourcePackProfile.class)
abstract class ResourcePackProfileMixin {

    @Shadow
    private ResourcePackInfo info;

    @Inject(at = @At("HEAD"), method = "isRequired", cancellable = true)
    protected void ignoreRequired(CallbackInfoReturnable<Boolean> callbackInfo) {
        if (info.source() != ResourcePackSource.BUILTIN)
            callbackInfo.setReturnValue(false);
    }

    @Inject(at = @At("HEAD"), method = "isPinned", cancellable = true)
    protected void ignorePinned(CallbackInfoReturnable<Boolean> callbackInfo) {
        if (info.source() != ResourcePackSource.BUILTIN)
            callbackInfo.setReturnValue(false);
    }

    @Inject(at = @At("HEAD"), method = "getPosition", cancellable = true)
    protected void ignorePosition(CallbackInfoReturnable<ResourcePackPosition> callbackInfo) {
        if (info.source() != ResourcePackSource.BUILTIN)
            callbackInfo.setReturnValue(new ResourcePackPosition(false, ResourcePackProfile.InsertionPosition.TOP, false));
    }

}
