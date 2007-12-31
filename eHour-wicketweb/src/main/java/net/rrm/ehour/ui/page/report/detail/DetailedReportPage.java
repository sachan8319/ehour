/**
 * Created on Dec 29, 2007
 * Created by Thies Edeling
 * Created by Thies Edeling
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.page.report.detail;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.report.criteria.AvailableCriteria;
import net.rrm.ehour.report.criteria.DetailedAvailableCriteria;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.service.ReportService;
import net.rrm.ehour.ui.model.KeyResourceModel;
import net.rrm.ehour.ui.page.report.BaseReportPage;
import net.rrm.ehour.ui.panel.contexthelp.ContextualHelpPanel;
import net.rrm.ehour.ui.panel.nav.report.ReportNavPanel;
import net.rrm.ehour.ui.panel.report.criteria.ReportCriteriaBackingBean;
import net.rrm.ehour.ui.panel.report.criteria.ReportTabbedPanel;
import net.rrm.ehour.ui.panel.report.criteria.detailed.DetailedReportCriteriaPanel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Detailed report 
 **/

@AuthorizeInstantiation("ROLE_REPORT")
public class DetailedReportPage extends BaseReportPage
{
	private static final long serialVersionUID = 187757929348342350L;
	
	@SpringBean
	private ReportService		reportService;
	private ReportTabbedPanel	tabPanel;

	/**
	 * 
	 */
	public DetailedReportPage()
	{
		super(new ResourceModel("report.title"));
		
		add(new ReportNavPanel("reportNav"));
		add(new ContextualHelpPanel("contextHelp"));
		
		final ReportCriteria reportCriteria = getReportCriteria(false);
		final IModel model = new CompoundPropertyModel(new ReportCriteriaBackingBean(reportCriteria));
		setModel(model);		
		
		List<AbstractTab> tabList = new ArrayList<AbstractTab>();
		
		tabList.add(new AbstractTab(new KeyResourceModel("criteria.title"))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String panelId)
			{
				return new DetailedReportCriteriaPanel(panelId, getModel());
			}
		});
		
		tabPanel = new ReportTabbedPanel("reportContainer", tabList);
		add(tabPanel);		
	}

	/**
	 * Get report data
	 * @param reportCriteria
	 * @return
	 */
	protected ReportData getReportData(ReportCriteria reportCriteria)
	{
		logger.debug("Getting report data");
		ReportData data = reportService.createAggregateReportData(reportCriteria);
		
//		reportService.getReportData(project, dateRange)
		
		return data;
	}		
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.page.BasePage#ajaxRequestReceived(org.apache.wicket.ajax.AjaxRequestTarget, int, java.lang.Object)
	 */
	@Override
	public void ajaxRequestReceived(AjaxRequestTarget target, int type, Object params)
	{
		target.addComponent(tabPanel);
	}	
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.page.report.BaseReportPage#getAvailableCriteria()
	 */
	@Override
	public AvailableCriteria getAvailableCriteria()
	{
		return new DetailedAvailableCriteria();
	}
}