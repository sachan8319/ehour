/**
 * Created on Sep 27, 2007
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

package net.rrm.ehour.ui.panel.report.type;

import net.rrm.ehour.report.reports.ReportDataAggregate;
import net.rrm.ehour.ui.panel.report.ReportType;
import net.rrm.ehour.ui.report.aggregate.AggregateReport;
import net.rrm.ehour.ui.reportchart.aggregate.CustomerHoursAggregateChartImage;
import net.rrm.ehour.ui.reportchart.aggregate.CustomerTurnoverAggregateImage;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.Model;

/**
 * Customer report panel
 **/

public class CustomerReportPanel extends ReportPanel
{
	private static final long serialVersionUID = 8422287988040603274L;

	public CustomerReportPanel(String id, AggregateReport reportData, ReportDataAggregate data)
	{
		super(id, reportData, data, ReportType.AGGREGATE_CUSTOMER);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.panel.report.type.ReportPanel#addCharts(net.rrm.ehour.report.reports.ReportDataAggregate, org.apache.wicket.markup.html.WebMarkupContainer)
	 */
	@Override
	protected void addCharts(ReportDataAggregate data, WebMarkupContainer parent)
	{
		Model dataModel = new Model(data);

		// hours per customer
		CustomerHoursAggregateChartImage customerHoursChart = new CustomerHoursAggregateChartImage("hoursChart", dataModel, chartWidth, chartHeight);
		parent.add(customerHoursChart);

		CustomerTurnoverAggregateImage customerTurnoverChart = new CustomerTurnoverAggregateImage("turnoverChart", dataModel, chartWidth, chartHeight);
		parent.add(customerTurnoverChart);
	}

}