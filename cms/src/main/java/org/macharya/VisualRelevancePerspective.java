package org.macharya;

import javax.jcr.*;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;

import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupElement;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.hippoecm.frontend.plugins.standards.perspective.Perspective;
import org.hippoecm.repository.HippoRepository;
import org.hippoecm.repository.HippoRepositoryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class VisualRelevancePerspective extends Perspective {

    private static final ResourceReference PERSPECTIVE_CSS =
            new CssResourceReference(VisualRelevancePerspective.class, "VisualRelevancePerspective.css");
    private static final ResourceReference OL_JS = new JavaScriptResourceReference(VisualRelevancePerspective.class, "ol.js");
    private static final ResourceReference OL_CSS = new CssResourceReference(VisualRelevancePerspective.class, "ol.css");
    private static final ResourceReference MA_JS = new JavaScriptResourceReference(VisualRelevancePerspective.class, "ma.js");
    private static final ResourceReference NG_JS = new JavaScriptResourceReference(VisualRelevancePerspective.class, "angular.js");


    private static HippoRepository repo;
    private static Logger logger = LoggerFactory.getLogger(VisualRelevancePerspective.class);


    public VisualRelevancePerspective(IPluginContext context, IPluginConfig config) {
        super(context, config);
        /*HippoRepository repo = null;
        try {
            repo = HippoRepositoryFactory.getHippoRepository("//vm");
        } catch (RepositoryException e) {
            e.printStackTrace();
        }*/
        setOutputMarkupId(true);

        final Model m = new Model("");
        TextField text = new TextField("textfield", m, String.class);
        this.add(text);
        // Form form = new Form("formx");

        Form<?> form = new Form<Void>("formx") {

            @Override
            protected void onSubmit() {


            }

        };
        form.add(text);

        this.add(form);

        add(new Label("perspective-heading", "Geo Targeting"));
        form.add(new Button("submit", Model.of("Submit")) {

            private static final long serialVersionUID = 1L;

            @Override
            public void onEvent(IEvent<?> event) {
                super.onEvent(event);

            }


        });


    }

    @Override
    protected void onActivated() {
        super.onActivated();

    }

    @Override
    public void renderHead(final IHeaderResponse response) {
        response.render(CssHeaderItem.forReference(PERSPECTIVE_CSS));
        response.render(CssHeaderItem.forReference(OL_CSS));
        response.render(JavaScriptHeaderItem.forReference(NG_JS));
        response.render(JavaScriptHeaderItem.forReference(OL_JS));
        response.render(JavaScriptHeaderItem.forReference(MA_JS));

    }

}