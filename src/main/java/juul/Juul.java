package juul;

import juul.event.Event;
import juul.event.EventKey;
import juul.event.EventTick;
import juul.font.FontUtil;
import juul.inject.LUT;
import juul.inject.ReflectionHelper;
import juul.inject.VersionMapping;
import juul.inject.WrapperManager;
import juul.inject.wrappers.MinecraftWrapper;
import juul.module.Manager;
import juul.module.Module;
import juul.ui.GUI;
import juul.util.misc.AimUtil;
import juul.util.player.InventoryUtil;

import java.util.Arrays;
import java.util.List;

public enum Juul {
    INSTANCE;

    public static final String NAME = "Juul", VERSION = "1.0";
    public boolean injected, closed;
    private final LUT lookupTable = new LUT();
    public Manager manager;
    public InventoryUtil invUtil = new InventoryUtil();
    public AimUtil aimUtil = new AimUtil();
    private final ReflectionHelper reflectionHelper = new ReflectionHelper();
    public Constants constants;
    public GUI gui;
    private WrapperManager wrapperManager;

    public List<Theme> themes = Arrays.asList(Theme.values());
    public Theme THEME;

    public void cycleTheme()
    {
        int iNewTheme = THEME.id() + 1;

        if (iNewTheme >= themes.size())
            iNewTheme = 0;

        THEME = themes.get(iNewTheme);
    }

    public void init() {
        if (injected || closed) return;

        injected = true;

        THEME = themes.get(0);

        /**
         * Must be enabled for injection
         */
        //getLookupTable().current = new VersionMapping(LUT.Version.V1_8);

        //Init Wrappers
        wrapperManager = new WrapperManager();

        FontUtil.bootstrap();
        constants = new Constants();

        if ((manager = new Manager()).setup()) {
            gui = new GUI();
        }
    }

    public void onEvent(Event<?> e) {
        if (!injected || closed) return;

        MinecraftWrapper minecraftWrapper = wrapperManager.get(MinecraftWrapper.class);

        if (e instanceof EventTick && minecraftWrapper.currentScreen() == gui)
            gui.onTick();

        for (Module module : manager.modules) {
            if (e instanceof EventKey) {
                if (module.keyBind == ((EventKey) e).getCode()) module.toggle();
            }
            if (module.isEnabled()) {
                module.onEvent(e);
            }
        }
    }

    public ReflectionHelper getReflectionHelper() { return reflectionHelper; }

    public LUT getLookupTable() { return lookupTable; }

    public WrapperManager getWrapperManager() { return wrapperManager; }

    /**
     *  Hooks
     *
     * Main Hook; Minecraft.startGame : 561
     *
     * EventKey; Minecraft.runTick : 1940 [X]
     * EventReach; PlayerControllerMP.getBlockReachDistance : 344 [X], EntityRenderer.getMouseOver : 480 [X]
     * EventUpdate; EntityPlayerSP.onLivingUpdate : 677, 872 [X]
     * EventMotion; EntityPlayerSP.func_175161_p : 155, 241 [X]
     * EventTick; Minecraft.runTick : 1727 [X]
     * EventRender; EntityRenderer.func_175068_a : 1907 [X]
     * EventHUD; GuiIngame.func_175180_a : 205 [X]
     *
     * AutoClicker; Minecraft.clickMouse : 1524, 1548 [X]
     *
     *
     * s27packetexplosion motion x, y, z made public
     * s12packetentityvelocity motion x, y, z made public
     */

}
