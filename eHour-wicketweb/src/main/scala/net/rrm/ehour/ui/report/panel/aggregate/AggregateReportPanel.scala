package net.rrm.ehour.ui.report.panel.aggregate

import net.rrm.ehour.report.reports.ReportData
import net.rrm.ehour.ui.common.report.ReportConfig
import net.rrm.ehour.ui.report.panel.TreeReportDataPanel
import org.apache.wicket.markup.html.{WebComponent, WebMarkupContainer}
import net.rrm.ehour.ui.report.{TreeReportData, TreeReportModel}
import org.apache.wicket.model.Model
import net.rrm.ehour.ui.chart.HighChartContainer
import net.rrm.ehour.config.EhourConfig
import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import org.apache.wicket.markup.html.panel.Panel
import net.rrm.ehour.ui.report.excel.ProjectReportExcel


abstract class AggregateReportPanel(id: String, reportModel: TreeReportModel, reportConfig: ReportConfig, generateChart: (String, ReportData, EhourConfig) => String) extends AbstractBasePanel[TreeReportModel](id) {
  setDefaultModel(reportModel)
  setOutputMarkupId(true)

  protected override def onBeforeRender() {
    val greyBorder: WebMarkupContainer = new WebMarkupContainer("frame")
    addOrReplace(greyBorder)

    val reportModel: TreeReportModel = getDefaultModel.asInstanceOf[TreeReportModel]
    greyBorder.add(new TreeReportDataPanel("reportTable", reportModel, reportConfig, ProjectReportExcel.getId))

    val reportData: ReportData = reportModel.getReportData
    greyBorder.add(addCharts(reportData, greyBorder))

    super.onBeforeRender()
  }

  private def addCharts(data: ReportData, parent: WebMarkupContainer): Panel = {
    val rawData: ReportData = (data.asInstanceOf[TreeReportData]).getRawReportData

    new HighChartContainer("chart", new Model(rawData), generateChart)
  }
}