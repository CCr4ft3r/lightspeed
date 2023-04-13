package com.ccr4ft3r.lightspeed.events;

import com.ccr4ft3r.lightspeed.ModConstants;
import com.ccr4ft3r.lightspeed.cache.GlobalCache;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.internal.BrandingControl;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = ModConstants.MOD_ID, value = Dist.CLIENT)
public class TitleScreenInjector {

    private static boolean launchComplete = false;

    @SuppressWarnings({"InstantiationOfUtilityClass", "unchecked"})
    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.InitScreenEvent event) {
        if (!(event.getScreen() instanceof TitleScreen) || launchComplete)
            return;
        launchComplete = true;
        try {
            long secondsToStart = ManagementFactory.getRuntimeMXBean().getUptime() / 1000;
            LogUtils.getLogger().info("Lightspeed: Launch took {}s", secondsToStart);
            BrandingControl brandingControl = new BrandingControl();

            Field f = BrandingControl.class.getDeclaredField("brandings");
            f.setAccessible(true);
            Method computeBranding = BrandingControl.class.getDeclaredMethod("computeBranding");
            computeBranding.setAccessible(true);
            computeBranding.invoke(null);

            List<String> brandings = new ArrayList<>((List<String>) f.get(brandingControl));
            if (brandings.size() > 1) {
                List<String> newBrandings = new ArrayList<>(brandings);
                f.set(brandingControl, newBrandings);
                newBrandings.add("Lightspeed: Launch took " + secondsToStart + "s");
            }
        } catch (NoSuchFieldException | NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException e) {
            LogUtils.getLogger().error("Cannot add launch time to title screen", e);
        }
        GlobalCache.EXECUTOR.execute(GlobalCache::disablePersistAndClear);
        GlobalCache.EXECUTOR.shutdown();
    }
}