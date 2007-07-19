/**
 * Created on Jul 17, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.common;

import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.ui.EhourWebApplication;
import net.rrm.ehour.ui.page.login.SessionExpiredPage;

import org.apache.wicket.Component;
import org.apache.wicket.authorization.IUnauthorizedComponentInstantiationListener;
import org.apache.wicket.authorization.strategies.role.RoleAuthorizationStrategy;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.spring.injection.annot.test.AnnotApplicationContextMock;

/**
 * TODO 
 **/

public class TestEhourWebApplication extends EhourWebApplication
{
	private AnnotApplicationContextMock	mockContext;


	/**
	 * When not authorized, just let it pass
	 */
	@Override
	protected void setupSecurity()
	{
		getApplicationSettings().setPageExpiredErrorPage(SessionExpiredPage.class);

		getSecuritySettings().setAuthorizationStrategy(new RoleAuthorizationStrategy(this));

		getSecuritySettings().setUnauthorizedComponentInstantiationListener(new IUnauthorizedComponentInstantiationListener()
        {
            public void onUnauthorizedInstantiation(final Component component)
            {
            }
        });		
	}

	/**
	 * Override to provide our mock injector
	 */
	@Override
	protected void springInjection()
	{
		mockContext = new AnnotApplicationContextMock();
		mockContext.putBean("EhourConfig", new EhourConfigStub());		
		
		addComponentInstantiationListener(new SpringComponentInjector(this, mockContext));
	}

	public AnnotApplicationContextMock getMockContext()
	{
		return mockContext;
	}
}