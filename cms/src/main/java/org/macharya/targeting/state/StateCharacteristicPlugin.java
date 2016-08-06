package org.macharya.targeting.state;


import com.onehippo.cms7.targeting.frontend.plugin.BuiltinCharacteristicPlugin;
import org.apache.wicket.Component;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.wicketstuff.js.ext.util.ExtClass;


@ExtClass("Hippo.Targeting.StateCharacteristicPlugin")
public class StateCharacteristicPlugin extends BuiltinCharacteristicPlugin {


    private static final JavaScriptResourceReference DOCTYPE_JS =
            new JavaScriptResourceReference(StateCharacteristicPlugin.class,
                    "StateCharacteristicPlugin.js");

    public StateCharacteristicPlugin(IPluginContext context, IPluginConfig config) {
        super(context, config);

    }

    @Override
    public void renderHead(final Component component, IHeaderResponse response) {
        super.renderHead(component, response);
        response.render(JavaScriptHeaderItem.forReference(DOCTYPE_JS));
    }

    protected ResourceReference getIcon() {
        return new PackageResourceReference(StateCharacteristicPlugin.class, "StateCharacteristicPlugin.png");
    }
}
