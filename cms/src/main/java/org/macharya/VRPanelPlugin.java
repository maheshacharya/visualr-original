package org.macharya;


import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.hippoecm.frontend.plugins.standards.panelperspective.PanelPlugin;

public abstract class VRPanelPlugin extends PanelPlugin {

    public static final String VR_PANEL_SERVICE_ID = "vr.panel";

    public VRPanelPlugin(final IPluginContext context, final IPluginConfig config) {
        super(context, config);
    }

    public String getPanelServiceId() {
        return VR_PANEL_SERVICE_ID;
    }

}
