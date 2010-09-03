/*
 * Created on Apr 12, 2007 by Zach Bailey
 *
 * THE PROGRAM IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL, BUT WITHOUT ANY WARRANTY. IT IS PROVIDED "AS IS" 
 * WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES 
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE ENTIRE RISK AS TO THE QUALITY AND PERFORMANCE OF THE 
 * PROGRAM IS WITH YOU. SHOULD THE PROGRAM PROVE DEFECTIVE, YOU ASSUME THE COST OF ALL NECESSARY SERVICING, REPAIR OR 
 * CORRECTION.
 * 
 * IN NO EVENT UNLESS REQUIRED BY APPLICABLE LAW THE AUTHOR WILL BE LIABLE TO YOU FOR DAMAGES, INCLUDING ANY GENERAL, SPECIAL, 
 * INCIDENTAL OR CONSEQUENTIAL DAMAGES ARISING OUT OF THE USE OR INABILITY TO USE THE PROGRAM (INCLUDING BUT NOT LIMITED TO LOSS 
 * OF DATA OR DATA BEING RENDERED INACCURATE OR LOSSES SUSTAINED BY YOU OR THIRD PARTIES OR A FAILURE OF THE PROGRAM TO OPERATE 
 * WITH ANY OTHER PROGRAMS), EVEN IF THE AUTHOR HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * Please feel free to distribute this code in any way, with or without this notice.
 */
package com.cms.webservices;

import com.hannonhill.www.ws.ns.AssetOperationService.AssetFactory;
import com.hannonhill.www.ws.ns.AssetOperationService.BaseAsset;
import com.hannonhill.www.ws.ns.AssetOperationService.Connector;
import com.hannonhill.www.ws.ns.AssetOperationService.ConnectorContentTypeLink;
import com.hannonhill.www.ws.ns.AssetOperationService.ContaineredAsset;
import com.hannonhill.www.ws.ns.AssetOperationService.DublinAwareAsset;
import com.hannonhill.www.ws.ns.AssetOperationService.ExpiringAsset;
import com.hannonhill.www.ws.ns.AssetOperationService.FeedBlock;
import com.hannonhill.www.ws.ns.AssetOperationService.File;
import com.hannonhill.www.ws.ns.AssetOperationService.Folder;
import com.hannonhill.www.ws.ns.AssetOperationService.FolderContainedAsset;
import com.hannonhill.www.ws.ns.AssetOperationService.IndexBlock;
import com.hannonhill.www.ws.ns.AssetOperationService.IndexBlockType;
import com.hannonhill.www.ws.ns.AssetOperationService.MetadataSet;
import com.hannonhill.www.ws.ns.AssetOperationService.Page;
import com.hannonhill.www.ws.ns.AssetOperationService.PageConfiguration;
import com.hannonhill.www.ws.ns.AssetOperationService.PageConfigurationSet;
import com.hannonhill.www.ws.ns.AssetOperationService.PageConfigurationSetContainer;
import com.hannonhill.www.ws.ns.AssetOperationService.PageRegion;
import com.hannonhill.www.ws.ns.AssetOperationService.PublishableAsset;
import com.hannonhill.www.ws.ns.AssetOperationService.Reference;
import com.hannonhill.www.ws.ns.AssetOperationService.Role;
import com.hannonhill.www.ws.ns.AssetOperationService.RoleAssignment;
import com.hannonhill.www.ws.ns.AssetOperationService.ScriptFormat;
import com.hannonhill.www.ws.ns.AssetOperationService.Site;
import com.hannonhill.www.ws.ns.AssetOperationService.StructuredData;
import com.hannonhill.www.ws.ns.AssetOperationService.StructuredDataAssetType;
import com.hannonhill.www.ws.ns.AssetOperationService.StructuredDataNode;
import com.hannonhill.www.ws.ns.AssetOperationService.StructuredDataType;
import com.hannonhill.www.ws.ns.AssetOperationService.Symlink;
import com.hannonhill.www.ws.ns.AssetOperationService.Template;
import com.hannonhill.www.ws.ns.AssetOperationService.TextBlock;
import com.hannonhill.www.ws.ns.AssetOperationService.TransportContainer;
import com.hannonhill.www.ws.ns.AssetOperationService.TwitterConnector;
import com.hannonhill.www.ws.ns.AssetOperationService.User;
import com.hannonhill.www.ws.ns.AssetOperationService.WordPressConnector;
import com.hannonhill.www.ws.ns.AssetOperationService.XhtmlBlock;
import com.hannonhill.www.ws.ns.AssetOperationService.XmlBlock;
import com.hannonhill.www.ws.ns.AssetOperationService.XsltFormat;

/**
 * Useful utility functions for Web Services implementations.
 *
 * @author Zach Bailey
 * @since 0.1
 */
public final class WebservicesUtils
{
    /**
     * Nulls out base asset's values
     * 
     * @param baseAsset
     */
    private static final void nullAssetValues(BaseAsset baseAsset)
    {
        //Never, ever send an entity type. This will lead to an error.
        baseAsset.setEntityType(null);
    }

    /**
     * Nulls out folder contained asset's values
     * 
     * @param folderContained
     */
    private static final void nullFolderContainedValues(FolderContainedAsset folderContained)
    {
        nullAssetValues(folderContained);
        //Null out the various relationship paths in favor of the ids 
        if (folderContained.getParentFolderId() != null)
            folderContained.setParentFolderPath(null);
        if (folderContained.getId() != null)
            folderContained.setPath(null);
        if (folderContained.getSiteId() != null)
            folderContained.setSiteName(null);
    }

    /**
     * Nulls out publishable asset's values
     * 
     * @param publishable
     */
    private static final void nullPublishableValues(PublishableAsset publishable)
    {
        nullExpiringValues(publishable);
        if (publishable.getExpirationFolderId() != null)
            publishable.setExpirationFolderPath(null);
    }

    /**
     * Nulls out dublin aware values
     * 
     * @param dublinAware
     */
    private static final void nullDublinAwareValues(DublinAwareAsset dublinAware)
    {
        nullFolderContainedValues(dublinAware);
        if (dublinAware.getMetadataSetId() != null)
            dublinAware.setMetadataSetPath(null);
    }

    /**
     * Nulls out expiring values
     * 
     * @param expiring
     */
    private static final void nullExpiringValues(ExpiringAsset expiring)
    {
        nullDublinAwareValues(expiring);
        if (expiring.getExpirationFolderId() != null)
            expiring.setExpirationFolderPath(null);
    }

    /**
     * Because of a limitation with Apache Axis, the data received when doing
     * a read on an asset is not able to be directly sent back to the server
     * as-is because the server actually sends more data than necessary. This
     * function will go through a page asset and null out the unnecessary items,
     * making the asset able to be sent back to the server.
     * 
     * In essence, the server sends both the id and path information for a relationship,
     * however the server will only accept either the id or the path, but not both. This
     * method nulls out the applicable relationship paths in favor of the ids. 
     * 
     * @param folder the folder whose data will be intelligently nulled out to ensure
     *      it can be sent back to the server.
     */
    public static final void nullFolderValues(Folder folder)
    {
        nullPublishableValues(folder);
        folder.setChildren(null);
    }

    /**
     * Because of a limitation with Apache Axis, the data received when doing
     * a read on an asset is not able to be directly sent back to the server
     * as-is because the server actually sends more data than necessary. This
     * function will go through a page asset and null out the unnecessary items,
     * making the asset able to be sent back to the server.
     * 
     * In essence, the server sends both the id and path information for a relationship,
     * however the server will only accept either the id or the path, but not both. This
     * method nulls out the applicable relationship paths in favor of the ids. 
     * 
     * @param page the page whose data will be intelligently nulled out to ensure
     *      it can be sent back to the server.
     */
    public static final void nullPageValues(Page page)
    {
        nullPublishableValues(page);

        // If the page has a content type, null out the configuration set
        if ((page.getContentTypeId() != null) || (page.getContentTypePath() != null))
        {
            page.setConfigurationSetId(null);
            page.setConfigurationSetPath(null);
            if (page.getContentTypeId() != null)
                page.setContentTypePath(null);
        }
        else
        {
            if (page.getConfigurationSetId() != null)
                page.setConfigurationSetPath(null);
        }

        //If the page has structured data, null out the structured data
        //relationships as well
        StructuredData sData = page.getStructuredData();
        if (sData != null)
        {
            if (sData.getDefinitionId() != null)
                sData.setDefinitionPath(null);
            StructuredDataNode[] nodes = sData.getStructuredDataNodes();

            if (nodes != null)
            {
                nullStructuredData(nodes);
            }
        }

        nullPageConfigurationValues(page.getPageConfigurations());
    }

    private static void nullPageConfigurationValues(PageConfiguration[] configs)
    {
        //Null out all the page configuration relationship information
        if (configs != null)
        {
            for (int i = 0; i < configs.length; i++)
            {
                if (configs[i].getFormatId() != null)
                    configs[i].setFormatPath(null);
                if (configs[i].getTemplateId() != null)
                    configs[i].setTemplatePath(null);
                configs[i].setEntityType(null);

                // fix page regions
                nullPageRegionValues(configs[i].getPageRegions());
            }
        }
    }

    /**
     * Nulls out page region values
     * 
     * @param pRegs
     */
    private static void nullPageRegionValues(PageRegion[] pRegs)
    {
        if (pRegs != null)
        {
            for (int j = 0; j < pRegs.length; j++)
            {
                if (pRegs[j].getBlockId() != null)
                    pRegs[j].setBlockPath(null);
                if (pRegs[j].getFormatId() != null)
                    pRegs[j].setFormatPath(null);
                pRegs[j].setEntityType(null);
            }
        }
    }

    /**
     * Nulls out unneeded values from a ScriptFormat.
     * 
     * @param scriptFormat
     */
    public static void nullScriptFormatValues(ScriptFormat scriptFormat)
    {
        nullFolderContainedValues(scriptFormat);
    }

    /**
     * Nulles out unneeded values from a XsltFormat.
     * @param xsltFormat
     */
    public static void nullXsltFormatValues(XsltFormat xsltFormat)
    {
        nullFolderContainedValues(xsltFormat);
    }

    /**
     * Nulls out unneeded values in an array of StructuredDataNode
     * objects
     *
     * @param sDataNodes
     */
    private static final void nullStructuredData(StructuredDataNode[] sDataNodes)
    {
        if (sDataNodes != null)
        {
            for (int k = 0; k < sDataNodes.length; k++)
            {
                if (StructuredDataType.fromString(StructuredDataType._asset) == sDataNodes[k].getType())
                {
                    sDataNodes[k].setText(null);

                    if (sDataNodes[k].getAssetType() == StructuredDataAssetType.fromString(StructuredDataAssetType._block))
                    {
                        if (sDataNodes[k].getBlockId() == null && sDataNodes[k].getBlockPath() == null)
                        {
                            sDataNodes[k].setBlockPath("");
                        }
                        else
                        {
                            sDataNodes[k].setBlockId(null);
                        }
                    }
                    else if (sDataNodes[k].getAssetType() == StructuredDataAssetType.fromString(StructuredDataAssetType._file))
                    {
                        if (sDataNodes[k].getFileId() == null && sDataNodes[k].getFilePath() == null)
                        {
                            sDataNodes[k].setFilePath("");
                        }
                        else
                        {
                            sDataNodes[k].setFileId(null);
                        }
                    }
                    else if (sDataNodes[k].getAssetType() == StructuredDataAssetType.fromString(StructuredDataAssetType._page))
                    {
                        if (sDataNodes[k].getPageId() == null && sDataNodes[k].getPagePath() == null)
                        {
                            sDataNodes[k].setPagePath("");
                        }
                        else
                        {
                            sDataNodes[k].setPageId(null);
                        }
                    }
                    else if (sDataNodes[k].getAssetType() == StructuredDataAssetType.fromString(StructuredDataAssetType._symlink))
                    {
                        if (sDataNodes[k].getSymlinkId() == null && sDataNodes[k].getSymlinkPath() == null)
                        {
                            sDataNodes[k].setSymlinkPath("");
                        }
                        else
                        {
                            sDataNodes[k].setSymlinkId(null);
                        }
                    }
                }
                else if (StructuredDataType.fromString(StructuredDataType._group) == sDataNodes[k].getType())
                {
                    StructuredDataNode[] sDataNodeArray = sDataNodes[k].getStructuredDataNodes();
                    nullStructuredData(sDataNodeArray);
                    sDataNodes[k].setText(null);
                    sDataNodes[k].setAssetType(null);
                }
                else if (StructuredDataType.fromString(StructuredDataType._text) == sDataNodes[k].getType())
                {
                    sDataNodes[k].setAssetType(null);
                    sDataNodes[k].setStructuredDataNodes(null);
                    if (sDataNodes[k].getText() == null)
                    {
                        sDataNodes[k].setText("");
                    }
                }
                else
                {
                    sDataNodes[k].setAssetType(null);
                    sDataNodes[k].setText(null);
                }
            }
        }
    }

    /**
     * Nulls out unneeded properties on File objects.
     * 
     * @param file
     */
    public static final void nullFileValues(File file)
    {
        nullPublishableValues(file);
        if ((file.getText() != null) && (!file.getText().equals("")))
            file.setData(null);
        else
            file.setText(null);
    }

    /**
     * Nulls out unneeded properties on PageConfigurationSetContainer objects. 
     * @param pcsc
     */
    public static final void nullPageConfigurationSetContainerValues(PageConfigurationSetContainer pcsc)
    {
        nullContaineredValues(pcsc);
    }

    /**
     * Nulls out unneeded properties on ContaineredAsset objects.
     * @param asset
     */
    public static final void nullContaineredValues(ContaineredAsset asset)
    {
        nullAssetValues(asset);

        if (asset.getParentContainerId() != null)
            asset.setParentContainerPath(null);
        if (asset.getSiteId() != null)
            asset.setSiteName(null);
    }

    /**
     * Nulls out unneeded properties of a TransportContainer object
     *  
     * @param asset
     */
    public static final void nullTransportContainerValues(TransportContainer asset)
    {
        nullContaineredValues(asset);

        asset.setChildren(null);
    }

    /**
     * Because of a limitation with Apache Axis, the data received when doing
     * a read on an asset is not able to be directly sent back to the server
     * as-is because the server actually sends more data than necessary. This
     * function will go through a page asset and null out the unnecessary items,
     * making the asset able to be sent back to the server.
     * 
     * In essence, the server sends both the id and path information for a relationship,
     * however the server will only accept either the id or the path, but not both. This
     * method nulls out the applicable relationship paths in favor of the ids. 
     * 
     * @param user the user whose data will be intelligently nulled out to ensure
     *      it can be sent back to the server.
     */
    public static final void nullUserValues(User user)
    {
        user.setEntityType(null);
    }

    /**
     * Nulls out unneeded properties of a FeedBlock object
     * 
     * @param feedBlock
     */
    public static final void nullFeedBlockValues(FeedBlock feedBlock)
    {
        nullExpiringValues(feedBlock);
    }

    /**
     * Nulls out unneeded properties of an IndexBlock object
     * 
     * @param indexBlock
     */
    public static final void nullIndexBlockValues(IndexBlock indexBlock)
    {
        nullExpiringValues(indexBlock);

        IndexBlockType indexBlockType = indexBlock.getIndexBlockType();
        if ((indexBlockType == null) || (indexBlockType.equals("")))
            indexBlockType = IndexBlockType.fromString("folder");

        if (indexBlockType.toString().equals("folder"))
        {
            if (indexBlock.getIndexedFolderId() == null && indexBlock.getIndexedFolderPath() == null)
                indexBlock.setIndexedFolderPath("");
            else if (indexBlock.getIndexedFolderId() != null)
                indexBlock.setIndexedFolderPath(null);
            indexBlock.setIndexedContentTypeId(null);
            indexBlock.setIndexedContentTypePath(null);
        }
        else if (indexBlockType.toString().equals("content-type"))
        {
            if (indexBlock.getIndexedContentTypeId() == null && indexBlock.getIndexedContentTypePath() == null)
                indexBlock.setIndexedContentTypePath("");
            else if (indexBlock.getIndexedContentTypeId() != null)
                indexBlock.setIndexedContentTypePath(null);
            indexBlock.setIndexedFolderId(null);
            indexBlock.setIndexedFolderPath(null);
        }
    }

    /**
     * Nulls out unneeded properties of a Template object
     * 
     * @param template
     */
    public static final void nullTemplateValues(Template template)
    {
        nullFolderContainedValues(template);

        if (template.getTargetId() != null)
            template.setTargetPath(null);

        nullPageRegionValues(template.getPageRegions());
    }

    /**
     * Nulls out unneeded properties of a Reference object
     * 
     * @param reference
     */
    public static final void nullReferenceValues(Reference reference)
    {
        nullFolderContainedValues(reference);

        if (reference.getReferencedAssetId() != null)
            reference.setReferencedAssetPath(null);
    }

    /**
     * Nulls out unneeded properties of a TextBlock object
     * 
     * @param textBlock
     */
    public static final void nullTextBlockValues(TextBlock textBlock)
    {
        nullExpiringValues(textBlock);
    }

    /**
     * Nulls out unneeded properties of an XhtmlBlock object
     * 
     * @param xhtmlBlock
     */
    public static final void nullXhtmlBlockValues(XhtmlBlock xhtmlBlock)
    {
        nullExpiringValues(xhtmlBlock);
    }

    /**
     * Nulls out unneeded properties of an XmlBlock object
     * 
     * @param xmlBlock
     */
    public static final void nullXmlBlockValues(XmlBlock xmlBlock)
    {
        nullExpiringValues(xmlBlock);
    }

    /**
     * Nulls out unneeded properties of a Symlink object
     * 
     * @param symlink
     */
    public static final void nullSymlinkValues(Symlink symlink)
    {
        nullExpiringValues(symlink);
    }

    /**
     * Nulls out unneeded properties of a PageConfigurationSet object.
     * @param pcs
     */
    public static final void nullPageConfigurationSetValues(PageConfigurationSet pcs)
    {
        nullPageConfigurationValues(pcs.getPageConfigurations());
        nullContaineredValues(pcs);
    }

    /**
     * Nulls out unneeded properties of a Site object.
     * @param site
     */
    public static final void nullSiteValues(Site site)
    {
        if (site.getCssFileId() != null && !site.getCssFileId().equals(""))
            site.setCssFilePath(null);

        if (site.getDefaultMetadataSetId() != null && !site.getDefaultMetadataSetId().equals(""))
            site.setDefaultMetadataSetPath(null);

        if (site.getSiteAssetFactoryContainerId() != null && !site.getSiteAssetFactoryContainerId().equals(""))
            site.setSiteAssetFactoryContainerPath(null);

        if (site.getSiteStartingPageId() != null && !site.getSiteStartingPageId().equals(""))
            site.setSiteStartingPagePath(null);

        if (site.getRoleAssignments() != null)
        {
            for (RoleAssignment assignment : site.getRoleAssignments())
            {
                if (assignment.getRoleId() != null && !assignment.getRoleId().equals(""))
                    assignment.setRoleName(null);
            }
        }

        site.setEntityType(null);
    }

    /**
     * Nulls out unneeded properties of a Role object
     * 
     * @param role
     */
    public static final void nullRoleValues(Role role)
    {
        nullAssetValues(role);
    }

    /**
     * Nulls out all the un-required 
     * @param ms
     */
    public static final void nullMetadataSetValues(MetadataSet ms)
    {
        nullAssetValues(ms);
        nullContaineredValues(ms);
        ms.setAuthorFieldRequired(null);
        ms.setAuthorFieldVisibility(null);
        ms.setDescriptionFieldRequired(null);
        ms.setDescriptionFieldVisibility(null);
        ms.setDisplayNameFieldRequired(null);
        ms.setDisplayNameFieldVisibility(null);
        ms.setDynamicMetadataFieldDefinitions(null);
        ms.setEndDateFieldRequired(null);
        ms.setEndDateFieldVisibility(null);
        ms.setKeywordsFieldRequired(null);
        ms.setKeywordsFieldVisibility(null);
        ms.setReviewDateFieldRequired(null);
        ms.setReviewDateFieldVisibility(null);
        ms.setStartDateFieldRequired(null);
        ms.setStartDateFieldVisibility(null);
        ms.setSummaryFieldRequired(null);
        ms.setSummaryFieldVisibility(null);
        ms.setTeaserFieldRequired(null);
        ms.setTeaserFieldVisibility(null);
        ms.setTitleFieldRequired(null);
        ms.setTitleFieldVisibility(null);
    }

    public static final void nullAssetFactoryValues(AssetFactory af)
    {
        nullAssetValues(af);
        nullContaineredValues(af);
        af.setAllowSubfolderPlacement(null);
        af.setBaseAssetId(null);
        af.setBaseAssetPath(null);
        af.setOverwrite(null);
        af.setPlacementFolderId(null);
        af.setPlacementFolderPath(null);
        af.setWorkflowDefinitionId(null);
        af.setWorkflowDefinitionPath(null);
    }

    /**
     * Fetches id of the input Site object. Returns <code>null</code>
     * if the input object is <code>null</code>
     * 
     * @param site
     * @return Returns the id of the input Site object or null 
     */
    public static String getSiteId(Site site)
    {
        if (site == null)
            return null;

        return site.getId();
    }

    private static final void nullConnectorValues(Connector c)
    {
        nullContaineredValues(c);
        ConnectorContentTypeLink[] links = c.getConnectorContentTypeLinks();
        for (ConnectorContentTypeLink link : links)
        {
            if (link.getContentTypeId() != null && !link.getContentTypeId().equals(""))
                link.setContentTypePath(null);
            if (link.getPageConfigurationId() != null && !link.getPageConfigurationId().equals(""))
                link.setPageConfigurationName(null);
        }
    }

    /**
     * Nulls out unneeded values from a TwitterConnector.
     * 
     * @param tc
     */
    public static final void nullTwitterConnectorValues(TwitterConnector tc)
    {
        nullConnectorValues(tc);
        if (tc.getDestinationId() != null && !tc.getDestinationId().equals(""))
            tc.setDestinationPath(null);
    }

    /**
     * Nulls out unneeded values from a WordPressConnector.
     * 
     * @param wpc
     */
    public static final void nullWordPressConnectorValues(WordPressConnector wpc)
    {
        nullConnectorValues(wpc);
    }
}
