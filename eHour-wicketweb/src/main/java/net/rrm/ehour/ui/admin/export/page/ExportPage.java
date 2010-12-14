package net.rrm.ehour.ui.admin.export.page;

import net.rrm.ehour.export.service.ParseSession;
import net.rrm.ehour.ui.admin.AbstractAdminPage;
import net.rrm.ehour.ui.admin.export.ExportAjaxEventType;
import net.rrm.ehour.ui.admin.export.panel.ImportPanel;
import net.rrm.ehour.ui.admin.export.panel.ValidateImportPanel;
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.PayloadAjaxEvent;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.AjaxLazyLoadPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.target.basic.RedirectRequestTarget;

/**
 * User: thies
 * Date: Nov 12, 2010
 * Time: 10:54:38 PM
 */
public class ExportPage extends AbstractAdminPage<Void>
{
    private static final String ID_PARSE_STATUS = "parseStatus";
    private static final String ID_RESTORE_BORDER = "restoreBorder";

    private static final long serialVersionUID = 821234996218723175L;
    private Form<Void> form;

    public ExportPage()
    {
        super(new ResourceModel("admin.export.title"), "admin.export.help.header", "admin.export.help.body");

        GreyRoundedBorder frame = new GreyRoundedBorder("frame", new ResourceModel("audit.report.title"));
        add(frame);

        GreyBlueRoundedBorder backupBorder = new GreyBlueRoundedBorder("backupBorder");
        frame.add(backupBorder);

        backupBorder.add(new Link<Void>("exportLink")
        {
            @Override
            public void onClick()
            {
                ResourceReference exportReference = new ResourceReference(ExportDatabase.ID_EXPORT_DB);

                exportReference.bind(getApplication());
                CharSequence url = getRequestCycle().urlFor(exportReference);

                getRequestCycle().setRequestTarget(new RedirectRequestTarget(url.toString()));
            }
        });

        GreyBlueRoundedBorder restoreBorder = new GreyBlueRoundedBorder(ID_RESTORE_BORDER);
        frame.add(restoreBorder);
        form = addUploadForm("form");

        restoreBorder.add(form);

        final WebMarkupContainer parseStatus = new WebMarkupContainer(ID_PARSE_STATUS);
        parseStatus.setOutputMarkupId(true);
        form.add(parseStatus);

    }

    private Form<Void> addUploadForm(String id)
    {
        Form<Void> form = new Form<Void>(id);
        form.setMultiPart(true);

        add(form);

        final FileUploadField file = new FileUploadField("file");
        form.add(file);


        // create the ajax button used to submit the form
        form.add(new AjaxSubmitLink("ajaxSubmit")
        {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form)
            {
                Component replacementPanel;

                String errorMessage;

                if ((errorMessage = isValidUpload(file)) == null)
                {
                    byte[] bytes = file.getFileUpload().getBytes();
                    final String xmlData = new String(bytes);

                    replacementPanel = new AjaxLazyLoadPanel(ID_PARSE_STATUS)
                    {
                        @Override
                        public Component getLazyLoadComponent(String markupId)
                        {
                            return new ValidateImportPanel(markupId, xmlData);
                        }
                    };
                } else
                {
                    replacementPanel = new Label(ID_PARSE_STATUS, "Invalid file uploaded: " + errorMessage);
                }

                replaceStatusPanel(replacementPanel, target);
            }
        });

        return form;
    }

    private String isValidUpload(FileUploadField field)
    {
        String errorMessage = null;

        if (field.getFileUpload() != null)
        {
            FileUpload upload = field.getFileUpload();

            if (upload.getContentType() == null || !upload.getContentType().toLowerCase().contains("text"))
            {
                errorMessage = "Invalid content type";
            } else if (StringUtils.isBlank(upload.getClientFileName()))
            {
                errorMessage = "Empty file";
            } else if (upload.getBytes() == null || upload.getBytes().length == 0 || upload.getSize() == 0)
            {
                errorMessage = "Empty file";
            } else
            {
                errorMessage = null;
            }
        } else
        {
            errorMessage = "Empty file";
        }

        return errorMessage;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean ajaxEventReceived
            (AjaxEvent
                     ajaxEvent)
    {
        boolean continueWithPropagating = true;

        if (ajaxEvent.getEventType() == ExportAjaxEventType.VALIDATED)
        {
            PayloadAjaxEvent<ParseSession> event = (PayloadAjaxEvent<ParseSession>) ajaxEvent;
            final ParseSession session = event.getPayload();

            Component replacement = new AjaxLazyLoadPanel(ID_PARSE_STATUS)
            {
                @Override
                public Component getLazyLoadComponent(String markupId)
                {
                    return new ImportPanel(markupId, session);
                }

                public Component getLoadingComponent(final String markupId)
                {
                    return new Label(markupId, "Importing...<br /><img alt=\"Loading...\" src=\"" +
                            RequestCycle.get().urlFor(AbstractDefaultAjaxBehavior.INDICATOR) + "\"/>").setEscapeModelStrings(false);
                }
            };

            AjaxRequestTarget target = event.getTarget();

            replaceStatusPanel(replacement, target);
            continueWithPropagating = false;
        }

        return continueWithPropagating;
    }

    private void replaceStatusPanel(Component replacement, AjaxRequestTarget target)
    {
        replacement.setOutputMarkupId(true);
        form.addOrReplace(replacement);
        target.addComponent(replacement);
    }
}
