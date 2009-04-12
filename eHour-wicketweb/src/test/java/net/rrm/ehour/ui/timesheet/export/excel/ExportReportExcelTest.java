/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * eHour is sponsored by TE-CON  - http://www.te-con.nl/
 */

package net.rrm.ehour.ui.timesheet.export.excel;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.rrm.ehour.config.dao.BinaryConfigurationDAO;
import net.rrm.ehour.config.service.ConfigurationServiceImpl;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.report.service.DetailedReportService;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;
import net.rrm.ehour.ui.report.panel.ReportTestUtil;
import net.rrm.ehour.ui.report.trend.PrintReport;
import net.rrm.ehour.util.DateUtil;

import org.junit.Before;
import org.junit.Test;

/**
 * Created on Apr 10, 2009, 2:05:13 PM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public class ExportReportExcelTest extends AbstractSpringWebAppTester
{
	private PrintReport report;
	private ConfigurationServiceImpl configService;
	private DetailedReportService detailedReportService;
	
	private ReportCriteria criteria;

	@Before
	public void setUp() throws Exception
	{
		super.setUp();

		detailedReportService = createMock(DetailedReportService.class);
		getMockContext().putBean("detailedReportService", detailedReportService);
		
		configService = new ConfigurationServiceImpl();
		getMockContext().putBean("configurationService", configService);
		
		BinaryConfigurationDAO binConfigfDao = createMock(BinaryConfigurationDAO.class);
		configService.setBinConfigDAO(binConfigfDao);
		
				
		criteria = ReportTestUtil.getReportCriteria();
		report = new PrintReport(criteria);
	}
	
	/**
	 * Test method for {@link net.rrm.ehour.ui.timesheet.export.excel.ExportReportExcel#getExcelData(java.lang.String)}.
	 * @throws IOException 
	 */
	@Test
	public void produceForEmptyMonth() throws IOException
	{
		List<FlatReportElement>	elements = new ArrayList<FlatReportElement>();
		
		ReportData data = new ReportData(elements, DateUtil.getDateRangeForMonth(new Date()));
		
		expect(detailedReportService.getDetailedReportData(criteria))
			.andReturn(data);
		
		replay(detailedReportService);
		byte[] excelData = new ExportReportExcel().getExcelData(report);
		assertTrue(excelData.length > 0);
//		writeByteData(excelData);
		
		verify(detailedReportService);
	}

	/**
	 * @param excelData
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void writeByteData(byte[] excelData) throws FileNotFoundException, IOException
	{
		File outfile = new File("/home/thies/test.xls");
		FileOutputStream fos = new FileOutputStream(outfile);
		fos.write(excelData);
		fos.close();
	}
}