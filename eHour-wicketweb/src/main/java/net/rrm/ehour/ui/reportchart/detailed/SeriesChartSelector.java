package net.rrm.ehour.ui.reportchart.detailed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.ui.panel.report.ReportConfig;
import net.rrm.ehour.ui.panel.report.TreeReportColumn;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

/**
 * Series selector for trend charts
 * @author Thies
 *
 */
public class SeriesChartSelector extends Panel
{
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param id
	 * @param config
	 * @param targetImage
	 */
	public SeriesChartSelector(String id, ReportConfig config, final AbstractTrendChartImage<?> targetImage)
	{
		super(id);
		
		List<Serializable> columns = new ArrayList<Serializable>();
		
		for (TreeReportColumn column : config.getReportColumns())
		{
			if (column.isChartSeriesColumn())
			{
				columns.add(column);
			}
		}
		
		final IModel model = new Model();
		
		final DropDownChoice columnSelection = new DropDownChoice("serieChartSelector", model, columns, new TreeReportColumnRenderer());

		columnSelection.add(new AjaxFormComponentUpdatingBehavior("onchange")
		{
			private static final long serialVersionUID = 507045565542332885L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				System.out.println(columnSelection.getInput());
			}
		});	
		
		add(columnSelection);
	}

	/**
	 * 
	 * @author Thies
	 *
	 */
	private class TreeReportColumnRenderer implements IChoiceRenderer
	{
		private static final long serialVersionUID = 1L;

		public Object getDisplayValue(Object object)
		{
			TreeReportColumn col = (TreeReportColumn)object;
			
			return (new ResourceModel(col.getColumnHeaderResourceKey())).getObject();
		}

		public String getIdValue(Object object, int index)
		{
			return Integer.toString(index);
		}
		
	}
}