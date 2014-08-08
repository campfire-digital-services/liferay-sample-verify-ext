/**
* Copyright (C) 2014 Permeance Technologies
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

import au.com.permeance.liferay.portal.documentlibrary.util.DLUtilExt;
import au.com.permeance.liferay.portal.util.PropsValuesExt;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.model.DLFileVersion;
import com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil;
import com.liferay.portlet.documentlibrary.service.DLFileVersionLocalServiceUtil;
import com.liferay.portlet.documentlibrary.service.persistence.DLFileEntryUtil;
import com.liferay.portlet.documentlibrary.store.DLStoreUtil;
import com.liferay.portlet.documentlibrary.util.DLUtil;

import java.io.InputStream;
import java.util.Date;
import java.util.List;


/**
 * Custom Verify Document Library.
 * 
 * This Verify Process implementation extends the default VerifyDocumentLibrary
 * process as follows:
 * o Remove empty file entries (i.e. file entries without a binary asset)
 *   See https://issues.liferay.com/browse/LPS-35280
 * o Normalize DL file names
 *   See https://issues.liferay.com/browse/LPS-37869
 * 
 * @author Tim Telcik <tim.telcik@permeance.com.au>
 * 
 * @see com.liferay.portal.verify.VerifyDocumentLibrary
 * @see https://issues.liferay.com/browse/LPS-35280
 * @see https://issues.liferay.com/browse/LPS-37869
 */
public class CustomVerifyDocumentLibrary extends com.liferay.portal.verify.VerifyDocumentLibrary {
	
	private static Log _log = LogFactoryUtil.getLog(CustomVerifyDocumentLibrary.class);

	
	public CustomVerifyDocumentLibrary() {
		super();
	}
	

	// LPS-35280	
	/*
	@Override
	protected void doVerify() throws Exception {
		
		// LPS-35280
		removeEmptyDLFileEntries();
		
		checkMisversionedDLFileEntries();

		checkDLFileEntryType();
		
		checkMimeTypes();
		
		removeOrphanedDLFileEntries();
		
		// LPS-37869
		if (PropsValuesExt.VERIFY_DL_FILE_NAME_NORMALIZATION_ENABLED) {
			normalizeDLFileEntryNames();
		}
		
		updateAssets();
	}
	*/

	// VerifyDocumentLibrary
	/*
	@Override
	protected void doVerify() throws Exception {

		checkDLFileEntryType();
		checkMimeTypes();
		checkTitles();
		removeOrphanedDLFileEntries();
		updateFileEntryAssets();
		updateFolderAssets();
		verifyTree();
	}
	*/
	
	@Override
	protected void doVerify() throws Exception {

		// VerifyDocumentLibrary
		/*
		checkDLFileEntryType();
		checkMimeTypes();
		checkTitles();
		removeOrphanedDLFileEntries();
		updateFileEntryAssets();
		updateFolderAssets();
		verifyTree();
		 */
		
		// LPS-35280 Check
		if (PropsValuesExt.VERIFY_DL_REMOVE_EMPTY_FILE_ENTRIES_ENABLED) {
			removeEmptyDLFileEntries();
		}

		// Default checks
		checkDLFileEntryType();
		checkMimeTypes();
		checkTitles();
		removeOrphanedDLFileEntries();
		
		// LPS-37869 Check
		if (PropsValuesExt.VERIFY_DL_FILE_NAME_NORMALIZATION_ENABLED) {
			normalizeDLFileEntryNames();
		}

		// Default checks
		updateFolderAssets();
		verifyTree();
	}
	
	
	@Override	
	protected void checkMimeTypes() throws Exception {

		List<DLFileEntry> dlFileEntries =
				DLFileEntryUtil.findByMimeType(
					ContentTypes.APPLICATION_OCTET_STREAM);
		
		if (_log.isDebugEnabled()) {
			_log.debug(
				"Processing " + dlFileEntries.size() + " file entries with " +
					ContentTypes.APPLICATION_OCTET_STREAM + " MIME type");
		}
		
		int updateCount = 0;

		for (DLFileEntry dlFileEntry : dlFileEntries) {
			
            if (!fileEntryHasFile(dlFileEntry)) {
               if (_log.isDebugEnabled()) {
            	  String fileEntryDetails = buildFileEntryDetails( dlFileEntry );
                  _log.warn("Skipping file entry " + dlFileEntry.getTitle() + " due to missing file : " + fileEntryDetails);
               }

               continue;
            }
            
			InputStream inputStream =
				DLFileEntryLocalServiceUtil.getFileAsStream(
					dlFileEntry.getUserId(), dlFileEntry.getFileEntryId(),
					dlFileEntry.getVersion(), false);

			String title = DLUtil.getTitleWithExtension(
				dlFileEntry.getTitle(), dlFileEntry.getExtension());

			String mimeType = MimeTypesUtil.getContentType(inputStream, title);

			if (mimeType.equals(ContentTypes.APPLICATION_OCTET_STREAM)) {
				continue;
			}

			dlFileEntry.setMimeType(mimeType);

			DLFileEntryLocalServiceUtil.updateDLFileEntry(dlFileEntry);

			DLFileVersion dlFileVersion = dlFileEntry.getFileVersion();

			dlFileVersion.setMimeType(mimeType);

			DLFileVersionLocalServiceUtil.updateDLFileVersion(dlFileVersion);
			
			updateCount++;
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Updated " + updateCount + " file entries with invalid MIME types");
		}
	}	
	
	
	protected String buildFileEntryDetails( DLFileEntry dlFileEntry ) {
		
		String fileEntryDetails = "";
		
		if (dlFileEntry != null) {

	        String uuid = dlFileEntry.getUuid();
	        long fileEntryId = dlFileEntry.getFileEntryId();
	        long groupId = dlFileEntry.getGroupId();
	        long companyId = dlFileEntry.getCompanyId();
	        long userId = dlFileEntry.getUserId();
	        String userName = dlFileEntry.getUserName();
	        long versionUserId = dlFileEntry.getVersionUserId();
	        String versionUserName = dlFileEntry.getVersionUserName();
	        Date createDate = dlFileEntry.getCreateDate();
	        Date modifiedDate = dlFileEntry.getModifiedDate();
	        long folderId = dlFileEntry.getFolderId();
	        String name = dlFileEntry.getName();
	        String ext = dlFileEntry.getExtension();
	        String title = dlFileEntry.getTitle();
	        String desc = dlFileEntry.getDescription();
	        String extraSettings = dlFileEntry.getExtraSettings();
	        String version = dlFileEntry.getVersion();
	        long size = dlFileEntry.getSize();
	        int readCount = dlFileEntry.getReadCount();
	        String mimeType = dlFileEntry.getMimeType();

	 	    fileEntryDetails = "(" 
	 	    		+ "uuid=" + uuid 
	 	    		+ ", id=" + fileEntryId
	 	    		+ ", groupId=\"" + groupId
	 	    		+ ", companyId=\"" + companyId
	 	    		+ ", userId=\"" + userId
	 	    		+ ", userName=\"" + userName
	 	    		+ ", versionUserId=\"" + versionUserId
	 	    		+ ", versionUserName=\"" + versionUserName	 	
	 	    		+ ", createDate=\"" + createDate
	 	    		+ ", modifiedDate=\"" + modifiedDate
	 	    		+ ", folderId=\"" + folderId
	 	    		+ ", name=\"" + name
	 	    		+ ", extension=\"" + ext 	 	    		
	 	    		+ ", title=\"" + title 
	 	    		+ ", description=\"" + desc 
	 	    		+ ", extraSettings=\"" + extraSettings 
	 	    		+ ", version=\"" + version
	 	    		+ ", size=\"" + size
	 	    		+ ", readCount=\"" + readCount
	 	    		+ ", mimeType=\"" + mimeType
	 	    		+ "\")";
		}
 	   
 	   return fileEntryDetails;
	}
	
	
	/**
	 * Remove empty DL file entries.
	 * 
     * @see https://issues.liferay.com/browse/LPS-35280
     * @see https://issues.liferay.com/browse/LPS-37869
	 */
	protected void removeEmptyDLFileEntries() throws Exception {
		
		List<DLFileEntry> dlFileEntries =
				DLFileEntryUtil.findByMimeType(
					ContentTypes.APPLICATION_OCTET_STREAM);
		

		if (_log.isDebugEnabled()) {
			_log.debug(
				"Scanning " + dlFileEntries.size() + " file entries with " +
					ContentTypes.APPLICATION_OCTET_STREAM + " MIME type for empty entries");
		}
		
		int deleteCount = 0;

		for (DLFileEntry dlFileEntry : dlFileEntries) {
			
			if (!fileEntryHasFile(dlFileEntry)) {
				try {
	               if (_log.isWarnEnabled()) {
	            	  String fileEntryDetails = buildFileEntryDetails(dlFileEntry);
	                  _log.warn("Removing file entry " + dlFileEntry.getTitle() + " due to missing file : " + fileEntryDetails);
	               }
					
	               DLFileEntryLocalServiceUtil.deleteFileEntry(dlFileEntry.getFileEntryId());
	               
	               deleteCount++;
				}
				catch (Exception e) {
					if (_log.isWarnEnabled()) {
						_log.warn("Unable to remove file entry " + dlFileEntry.getFileEntryId() + ": " + e.getMessage());
					}
				}
			}
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Removed " + deleteCount + " empty file entries");
		}
	}	
	
	
	/**
	 * Normalize DL file entry names.
	 * 
	 * @see https://issues.liferay.com/browse/LPS-37869
	 */
	protected void normalizeDLFileEntryNames() throws Exception {

		int fileEntriesCount = DLFileEntryLocalServiceUtil.getFileEntriesCount();
		
		_log.info("Scanning " + fileEntriesCount + " file entries for invalid file names");
		
		int fileNameNormalizationListPageSize = PropsValuesExt.VERIFY_DL_FILE_NAME_NORMALIZATION_MAX_LIST_PAGE_SIZE;
		
		_log.debug("fileNameNormalizationListPageSize: " + fileNameNormalizationListPageSize);
		
		int totalUpdateCount = 0;
		
		int istart = 0;
		int iend = fileNameNormalizationListPageSize;
		
		while (istart < fileEntriesCount) {
			
			_log.debug("Processing file entries " + istart + " to " + iend);

			List<DLFileEntry> dlFileEntries =
					DLFileEntryLocalServiceUtil.getFileEntries( istart, iend );

			int curUpdateCount = normalizeDLFileEntryNames( dlFileEntries );
			
			totalUpdateCount += curUpdateCount;
			
			istart = iend;

			iend += fileNameNormalizationListPageSize;

			iend = Math.min( iend, fileEntriesCount);
		}

		if (_log.isDebugEnabled()) {
			_log.info("Finished normalizing " + totalUpdateCount + " file names");
		}
	}

	protected int normalizeDLFileEntryNames( List<DLFileEntry> dlFileEntries ) throws Exception {

		int updateCount = 0;

		if (dlFileEntries != null)  {

			if (_log.isDebugEnabled()) {
				_log.debug("Scanning " + dlFileEntries.size() + " file entries for invalid file names");
			}
			
			for (DLFileEntry dlFileEntry : dlFileEntries) {
				
				long fileEntryId = dlFileEntry.getFileEntryId();
				
				String fileName = dlFileEntry.getTitle();
				
				if (!DLUtilExt.isValidFileName(fileName)) {
					try {
		               if (_log.isWarnEnabled()) {
		            	   _log.warn("Normalizing file " + fileEntryId + " with name \"" + fileName + "\"");
		               }
		               
		               String newFileName = DLUtilExt.normalizeFileName( fileName );	               
		               
		               dlFileEntry.setTitle( newFileName );
						
		               DLFileEntryLocalServiceUtil.updateDLFileEntry( dlFileEntry );
		               
		               if (_log.isWarnEnabled()) {
		            	   _log.warn("Normalized file " + fileEntryId + " with name \"" + newFileName + "\"");
		               }
		               
		               updateCount++;
					}
					catch (Exception e) {
						if (_log.isWarnEnabled()) {
							_log.warn("Unable to update file entry " + fileEntryId + " : " + e.getMessage());
						}
					}
				}
			}
		}
		
		return updateCount;
	}	
	
	protected boolean fileEntryHasFile( DLFileEntry dlFileEntry ) throws Exception {
		
        long companyId = dlFileEntry.getCompanyId();
        long dataRepositoryId = dlFileEntry.getDataRepositoryId();
        String name = dlFileEntry.getName();
        String version = dlFileEntry.getVersion();

        boolean result = DLStoreUtil.hasFile( companyId, dataRepositoryId, name, version );
        
        return result;
	}

}
