package me.wyne.mixin;

import com.google.common.io.Files;
import me.wyne.RePacker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.server.ServerResourcePackManager;
import net.minecraft.util.Downloader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

@Mixin(ServerResourcePackManager.class)
abstract class ServerResourcePackManagerMixin {

    private final File resourcePackDirectory = new File(MinecraftClient.getInstance().runDirectory, "repacker");

    @Inject(
            method = "onDownload",
            at = @At("TAIL"),
            cancellable = true
    )
    private void onDownloadMixin(Collection<ServerResourcePackManager.PackEntry> packs, Downloader.DownloadResult result, CallbackInfo ci) {
        for(ServerResourcePackManager.PackEntry packEntry : packs) {
            if (!packEntry.isDiscarded()) {
                Path path = (Path)result.downloaded().get(packEntry.id);
                File packFile = path.toFile();
                File repackedFile = new File(resourcePackDirectory, packFile.getName() + ".zip");
                try {
                    if (!repackedFile.exists()) {
                        repackedFile.getParentFile().mkdirs();
                        repackedFile.createNewFile();
                    }
                    Files.copy(packFile, repackedFile);
                } catch (IOException e) {
                    RePacker.LOGGER.error("An exception occurred trying to repack resource pack");
                    RePacker.LOGGER.error(e.getMessage());
                }
            }
        }
    }

}
