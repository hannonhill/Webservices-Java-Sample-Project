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

import com.hannonhill.www.ws.ns.AssetOperationService.Page;
import com.hannonhill.www.ws.ns.AssetOperationService.PageConfiguration;
import com.hannonhill.www.ws.ns.AssetOperationService.PageRegion;
import com.hannonhill.www.ws.ns.AssetOperationService.StructuredData;
import com.hannonhill.www.ws.ns.AssetOperationService.StructuredDataAssetType;
import com.hannonhill.www.ws.ns.AssetOperationService.StructuredDataNode;
import com.hannonhill.www.ws.ns.AssetOperationService.StructuredDataType;

/**
 * Useful utility functions for Web Services implementations.
 *
 * @author Zach Bailey
 * @since 0.1
 */
public final class WebservicesUtils
{
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
        //Never, ever send an entity type. This will lead to an error.
        page.setEntityType(null);
        //Null out the various relationship paths in favor of the ids 
        page.setParentFolderPath(null);
        page.setConfigurationSetPath(null);
        page.setExpirationFolderPath(null);
        page.setMetadataSetPath(null);
        //Null out the path in favor of the id
        page.setPath(null);

        //If the page has structured data, null out the structured data
        //relationships as well
        StructuredData sData = page.getStructuredData();
        if (sData != null)
        {
            sData.setDefinitionPath(null);
            StructuredDataNode[] nodes = sData.getStructuredDataNodes();

            if (nodes != null)
            {
                nullStructuredData(nodes);
            }
        }
        
        //Null out all the page configuration relationship information
        PageConfiguration[] pConf = page.getPageConfigurations();
        if (pConf != null)
        {
            for (int i = 0; i < pConf.length; i++)
            {
                pConf[i].setStylesheetPath(null);
                pConf[i].setTemplatePath(null);
                pConf[i].setEntityType(null);

                // fix page regions
                PageRegion[] pRegs = pConf[i].getPageRegions();

                if (pRegs != null)
                {
                    for (int j = 0; j < pRegs.length; j++)
                    {
                        pRegs[j].setBlockPath(null);
                        pRegs[j].setStylesheetPath(null);
                        pRegs[j].setEntityType(null);
                    }
                }
            }
        }
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
}
