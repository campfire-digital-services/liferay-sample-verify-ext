/**
* Copyright (C) 2013-present Permeance Technologies
*
* This program is free software: you can redistribute it and/or modify it under the terms of the
* GNU General Public License as published by the Free Software Foundation, either version 3 of the
* License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
* even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* General Public License for more details.
*
* You should have received a copy of the GNU General Public License along with this program. If
* not, see <http://www.gnu.org/licenses/>.
*/

package au.com.permeance.liferay.portal.verify;

import com.liferay.portal.verify.VerifyAsset;
import com.liferay.portal.verify.VerifyAuditedModel;
import com.liferay.portal.verify.VerifyBlogs;
import com.liferay.portal.verify.VerifyBookmarks;
import com.liferay.portal.verify.VerifyCalendar;
import com.liferay.portal.verify.VerifyDB2;
import com.liferay.portal.verify.VerifyDynamicDataMapping;
import com.liferay.portal.verify.VerifyGroup;
import com.liferay.portal.verify.VerifyGroupId;
import com.liferay.portal.verify.VerifyJournal;
import com.liferay.portal.verify.VerifyLayout;
import com.liferay.portal.verify.VerifyMessageBoards;
import com.liferay.portal.verify.VerifyMySQL;
import com.liferay.portal.verify.VerifyOracle;
import com.liferay.portal.verify.VerifyOrganization;
import com.liferay.portal.verify.VerifyPermission;
import com.liferay.portal.verify.VerifyPortalPreferences;
import com.liferay.portal.verify.VerifyPortletPreferences;
import com.liferay.portal.verify.VerifyProperties;
import com.liferay.portal.verify.VerifyRegion;
import com.liferay.portal.verify.VerifyResourcePermissions;
import com.liferay.portal.verify.VerifyRole;
import com.liferay.portal.verify.VerifySQLServer;
import com.liferay.portal.verify.VerifyUUID;
import com.liferay.portal.verify.VerifyUser;
import com.liferay.portal.verify.VerifyWiki;
import com.liferay.portal.verify.VerifyWorkflow;


/**
 * Custom Verify Process Suite.
 *
 * @see com.liferay.portal.verify.VerifyProcess
 * @see com.liferay.portal.verify.VerifyDocumentLibrary
 * @see https://issues.liferay.com/browse/LPS-35280
 * @see https://issues.liferay.com/browse/LPS-37869
 * 
 * @author Tim Telcik <tim.telcik@permeance.com.au>
 */
public class CustomVerifyProcessSuite extends com.liferay.portal.verify.VerifyProcess {

	@Override
	protected void doVerify() throws Exception {
		verify(new VerifyProperties());

		verify(new VerifyDB2());
		verify(new VerifyMySQL());
		verify(new VerifyOracle());
		verify(new VerifySQLServer());

		verify(new VerifyUUID());

		verify(new VerifyPermission());
		verify(new VerifyGroup());
		verify(new VerifyRole());

		verify(new VerifyAsset());
		verify(new VerifyAuditedModel());
		verify(new VerifyBlogs());
		verify(new VerifyBookmarks());
		verify(new VerifyCalendar());

		// LPS-35280 / LPS-37869
		// verify(new VerifyDocumentLibrary());
		verify(new CustomVerifyDocumentLibrary());		

		verify(new VerifyDynamicDataMapping());
		verify(new VerifyGroupId());
		verify(new VerifyJournal());
		verify(new VerifyLayout());
		verify(new VerifyMessageBoards());
		verify(new VerifyOrganization());
		verify(new VerifyPortalPreferences());
		verify(new VerifyPortletPreferences());
		verify(new VerifyRegion());
		verify(new VerifyResourcePermissions());
		verify(new VerifyUser());
		verify(new VerifyWiki());
		verify(new VerifyWorkflow());

		// VerifyBlogsTrackbacks looks at every blog comment to see if it is a
		// trackback and verifies that the source URL is a valid URL.

		//verify(new VerifyBlogsTrackbacks());
	}

}