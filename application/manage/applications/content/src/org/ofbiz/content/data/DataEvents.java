/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package org.ofbiz.content.data;

import org.ofbiz.base.util.*;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.LocalDispatcher;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Locale;
import java.util.Map;

/**
 * DataEvents Class
 */
public class DataEvents {

    public static final String module = DataEvents.class.getName();
    public static final String err_resource = "ContentErrorUiLabels";

    public static String uploadImage(HttpServletRequest request, HttpServletResponse response) {
        return DataResourceWorker.uploadAndStoreImage(request, "dataResourceId", "imageData");
    }
    /**
     * 查看图片 Add By AlexYao
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws GenericEntityException
     */
    public static String getImage1(HttpServletRequest request, HttpServletResponse response) throws IOException, GenericEntityException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String contentId = request.getParameter("contentId");
        if (UtilValidate.isEmpty(contentId)) {
            return "error";
        }
        GenericValue content = delegator.findByPrimaryKey("Content", UtilMisc.toMap("contentId", contentId));
        if (UtilValidate.isEmpty(content)) {
            return "error";
        }
        GenericValue dataResource = delegator.findByPrimaryKey("DataResource", UtilMisc.toMap("dataResourceId", content.get("dataResourceId")));
        if (UtilValidate.isEmpty(dataResource)) {
            return "error";
        }
        String objectInfo = dataResource.getString("objectInfo").replace("/images/webapp", "");
        if (!objectInfo.contains("/images")) {
            objectInfo = "/images" + objectInfo;
        }
        String dataName = objectInfo.substring(objectInfo.lastIndexOf("/"));
        // get the mime type
        String mimeType = DataResourceWorker.getMimeType(dataResource);
        
        String prefix = System.getProperty("ofbiz.home");
        
        String sep = "";
        if (objectInfo.indexOf("/") != 0 && prefix.lastIndexOf("/") != (prefix.length() - 1)) {
            sep = "/";
        }
        File file = FileUtil.getFile(prefix + sep + "/images/webapp" + objectInfo);
        if (!file.exists()) {
            throw new FileNotFoundException("No file found: " + (prefix + sep + objectInfo));
        }
        String url = objectInfo;
        response.sendRedirect(url);

        
        return "success";
    }
    
    /** Streams any binary content data to the browser */
    public static String getImage(HttpServletRequest request, HttpServletResponse response) {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        HttpSession session = request.getSession();

        response.setContentType("image/jpeg");
        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
        String userAgent = request.getHeader("User-Agent");
        
        Map<String, Object> httpParams = UtilHttp.getParameterMap(request);
        String contentId = (String) httpParams.get("contentId");
        if (UtilValidate.isEmpty(contentId)) {
            String errorMsg = "Required parameter contentId not found!";
            Debug.logError(errorMsg, module);
            request.setAttribute("_ERROR_MESSAGE_", errorMsg);
            return "error";
        }
        
        // get the permission service required for streaming data; default is always the genericContentPermission
        String permissionService = UtilProperties.getPropertyValue("content.properties", "stream.permission.service", "genericContentPermission");
        
        // get the content record
        GenericValue content;
        try {
            content = delegator.findByPrimaryKey("Content", UtilMisc.toMap("contentId", contentId));
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            request.setAttribute("_ERROR_MESSAGE_", e.getMessage());
            return "error";
        }
        
        // make sure content exists
        if (content == null) {
            String errorMsg = "No content found for Content ID: " + contentId;
            Debug.logError(errorMsg, module);
            request.setAttribute("_ERROR_MESSAGE_", errorMsg);
            return "error";
        }
        
        // make sure there is a DataResource for this content
        String dataResourceId = content.getString("dataResourceId");
        if (UtilValidate.isEmpty(dataResourceId)) {
            String errorMsg = "No Data Resource found for Content ID: " + contentId;
            Debug.logError(errorMsg, module);
            request.setAttribute("_ERROR_MESSAGE_", errorMsg);
            return "error";
        }
        
        // get the data resource
        GenericValue dataResource;
        try {
            dataResource = delegator.findByPrimaryKey("DataResource", UtilMisc.toMap("dataResourceId", dataResourceId));
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            request.setAttribute("_ERROR_MESSAGE_", e.getMessage());
            return "error";
        }
        
        // make sure the data resource exists
        if (dataResource == null) {
            String errorMsg = "No Data Resource found for ID: " + dataResourceId;
            Debug.logError(errorMsg, module);
            request.setAttribute("_ERROR_MESSAGE_", errorMsg);
            return "error";
        }
        
        // see if data resource is public or not
        String isPublic = dataResource.getString("isPublic");
        if (UtilValidate.isEmpty(isPublic)) {
            isPublic = "N";
        }
        
        // not public check security
        if (!"Y".equalsIgnoreCase(isPublic)) {
            // do security check
            Map<String, Object> permSvcCtx = UtilMisc.toMap("userLogin", userLogin, "mainAction", "VIEW", "contentId", contentId);
            Map<String, Object> permSvcResp;
            /*add by changsy 暂时不对图片权限做判断 2016.01.27*/
            /*try {
                permSvcResp = dispatcher.runSync(permissionService, permSvcCtx);
            } catch (GenericServiceException e) {
                Debug.logError(e, module);
                request.setAttribute("_ERROR_MESSAGE_", e.getMessage());
                return "error";
            }
            if (ServiceUtil.isError(permSvcResp)) {
                String errorMsg = ServiceUtil.getErrorMessage(permSvcResp);
                Debug.logError(errorMsg, module);
                request.setAttribute("_ERROR_MESSAGE_", errorMsg);
                return "error";
            }

            // no service errors; now check the actual response
            Boolean hasPermission = (Boolean) permSvcResp.get("hasPermission");
            if (!hasPermission.booleanValue()) {
                String errorMsg = (String) permSvcResp.get("failMessage");
                Debug.logError(errorMsg, module);
                request.setAttribute("_ERROR_MESSAGE_", errorMsg);
                return "error";
            }*/
        }
        
        // get objects needed for data processing
        String contextRoot = (String) request.getAttribute("_CONTEXT_ROOT_");
        String webSiteId = (String) session.getAttribute("webSiteId");
        String dataName = dataResource.getString("dataResourceName");
        Locale locale = UtilHttp.getLocale(request);
        
        // get the mime type
        String mimeType = DataResourceWorker.getMimeType(dataResource);
        
        // hack for IE and mime types
        if (userAgent.indexOf("MSIE") > -1) {
            Debug.logInfo("Found MSIE changing mime type from - " + mimeType, module);
            mimeType = "application/octet-stream";
        }
        
        // for local resources; use HTTPS if we are requested via HTTPS
        String https = "false";
        String protocol = request.getProtocol();
        if ("https".equalsIgnoreCase(protocol)) {
            https = "true";
        }
        
        // get the data resource stream and conent length
        Map<String, Object> resourceData;
        try {
            resourceData = DataResourceWorker.getDataResourceStream(dataResource, https, webSiteId, locale, contextRoot, false);
        } catch (IOException e) {
            Debug.logError(e, "Error getting DataResource stream", module);
            request.setAttribute("_ERROR_MESSAGE_", e.getMessage());
            return "error";
        } catch (GeneralException e) {
            Debug.logError(e, "Error getting DataResource stream", module);
            request.setAttribute("_ERROR_MESSAGE_", e.getMessage());
            return "error";
        }
        
        // get the stream data
        InputStream stream = null;
        Long length = null;
        
        if (resourceData != null) {
            stream = (InputStream) resourceData.get("stream");
            length = (Long) resourceData.get("length");
        }
        Debug.logInfo("Got resource data stream: " + length + " bytes", module);
        
        // stream the content to the browser
        if (stream != null && length != null) {
            try {
                UtilHttp.streamContentToBrowser2(response, stream, length.intValue(), mimeType, dataName);
            } catch (IOException e) {
                Debug.logError(e, "Unable to write content to browser", module);
                request.setAttribute("_ERROR_MESSAGE_", e.getMessage());
                return "error";
            }
        } else {
            String errorMsg = "No data is available.";
            Debug.logError(errorMsg, module);
            request.setAttribute("_ERROR_MESSAGE_", errorMsg);
            return "error";
        }
        
        return "success";
    }
    /** Streams any binary content data to the browser */
    public static String serveObjectData(HttpServletRequest request, HttpServletResponse response) {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        HttpSession session = request.getSession();

        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
        String userAgent = request.getHeader("User-Agent");

        Map<String, Object> httpParams = UtilHttp.getParameterMap(request);
        String contentId = (String) httpParams.get("contentId");
        if (UtilValidate.isEmpty(contentId)) {
            String errorMsg = "Required parameter contentId not found!";
            Debug.logError(errorMsg, module);
            request.setAttribute("_ERROR_MESSAGE_", errorMsg);
            return "error";
        }

        // get the permission service required for streaming data; default is always the genericContentPermission
        String permissionService = UtilProperties.getPropertyValue("content.properties", "stream.permission.service", "genericContentPermission");

        // get the content record
        GenericValue content;
        try {
            content = delegator.findByPrimaryKey("Content", UtilMisc.toMap("contentId", contentId));
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            request.setAttribute("_ERROR_MESSAGE_", e.getMessage());
            return "error";
        }

        // make sure content exists
        if (content == null) {
            String errorMsg = "No content found for Content ID: " + contentId;
            Debug.logError(errorMsg, module);
            request.setAttribute("_ERROR_MESSAGE_", errorMsg);
            return "error";
        }

        // make sure there is a DataResource for this content
        String dataResourceId = content.getString("dataResourceId");
        if (UtilValidate.isEmpty(dataResourceId)) {
            String errorMsg = "No Data Resource found for Content ID: " + contentId;
            Debug.logError(errorMsg, module);
            request.setAttribute("_ERROR_MESSAGE_", errorMsg);
            return "error";
        }

        // get the data resource
        GenericValue dataResource;
        try {
            dataResource = delegator.findByPrimaryKey("DataResource", UtilMisc.toMap("dataResourceId", dataResourceId));
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            request.setAttribute("_ERROR_MESSAGE_", e.getMessage());
            return "error";
        }

        // make sure the data resource exists
        if (dataResource == null) {
            String errorMsg = "No Data Resource found for ID: " + dataResourceId;
            Debug.logError(errorMsg, module);
            request.setAttribute("_ERROR_MESSAGE_", errorMsg);
            return "error";
        }

        // see if data resource is public or not
        String isPublic = dataResource.getString("isPublic");
        if (UtilValidate.isEmpty(isPublic)) {
            isPublic = "N";
        }

        // not public check security
        if (!"Y".equalsIgnoreCase(isPublic)) {
            // do security check
            Map<String, Object> permSvcCtx = UtilMisc.toMap("userLogin", userLogin, "mainAction", "VIEW", "contentId", contentId);
            Map<String, Object> permSvcResp;
            /*add by changsy 暂时不对图片权限做判断 2016.01.27*/
            /*try {
                permSvcResp = dispatcher.runSync(permissionService, permSvcCtx);
            } catch (GenericServiceException e) {
                Debug.logError(e, module);
                request.setAttribute("_ERROR_MESSAGE_", e.getMessage());
                return "error";
            }
            if (ServiceUtil.isError(permSvcResp)) {
                String errorMsg = ServiceUtil.getErrorMessage(permSvcResp);
                Debug.logError(errorMsg, module);
                request.setAttribute("_ERROR_MESSAGE_", errorMsg);
                return "error";
            }

            // no service errors; now check the actual response
            Boolean hasPermission = (Boolean) permSvcResp.get("hasPermission");
            if (!hasPermission.booleanValue()) {
                String errorMsg = (String) permSvcResp.get("failMessage");
                Debug.logError(errorMsg, module);
                request.setAttribute("_ERROR_MESSAGE_", errorMsg);
                return "error";
            }*/
        }

        // get objects needed for data processing
        String contextRoot = (String) request.getAttribute("_CONTEXT_ROOT_");
        String webSiteId = (String) session.getAttribute("webSiteId");
        String dataName = dataResource.getString("dataResourceName");
        Locale locale = UtilHttp.getLocale(request);

        // get the mime type
        String mimeType = DataResourceWorker.getMimeType(dataResource);

        // hack for IE and mime types
        if (userAgent.indexOf("MSIE") > -1) {
            Debug.logInfo("Found MSIE changing mime type from - " + mimeType, module);
            mimeType = "application/octet-stream";
        }

        // for local resources; use HTTPS if we are requested via HTTPS
        String https = "false";
        String protocol = request.getProtocol();
        if ("https".equalsIgnoreCase(protocol)) {
            https = "true";
        }

        // get the data resource stream and conent length
        Map<String, Object> resourceData;
        try {
            resourceData = DataResourceWorker.getDataResourceStream(dataResource, https, webSiteId, locale, contextRoot, false);
        } catch (IOException e) {
            Debug.logError(e, "Error getting DataResource stream", module);
            request.setAttribute("_ERROR_MESSAGE_", e.getMessage());
            return "error";
        } catch (GeneralException e) {
            Debug.logError(e, "Error getting DataResource stream", module);
            request.setAttribute("_ERROR_MESSAGE_", e.getMessage());
            return "error";
        }

        // get the stream data
        InputStream stream = null;
        Long length = null;

        if (resourceData != null) {
            stream = (InputStream) resourceData.get("stream");
            length = (Long) resourceData.get("length");
        }
        Debug.logInfo("Got resource data stream: " + length + " bytes", module);

        // stream the content to the browser
        if (stream != null && length != null) {
            try {
                UtilHttp.streamContentToBrowser(response, stream, length.intValue(), mimeType, dataName);
            } catch (IOException e) {
                Debug.logError(e, "Unable to write content to browser", module);
                request.setAttribute("_ERROR_MESSAGE_", e.getMessage());
                return "error";
            }
        } else {
            String errorMsg = "No data is available.";
            Debug.logError(errorMsg, module);
            request.setAttribute("_ERROR_MESSAGE_", errorMsg);
            return "error";
        }

        return "success";
    }

    /** Streams ImageDataResource data to the output. */
    // TODO: remove this method in favor of serveObjectData
    public static String serveImage(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        ServletContext application = session.getServletContext();

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, Object> parameters = UtilHttp.getParameterMap(request);

        Debug.logInfo("Img UserAgent - " + request.getHeader("User-Agent"), module);

        String dataResourceId = (String) parameters.get("imgId");
        if (UtilValidate.isEmpty(dataResourceId)) {
            String errorMsg = "Error getting image record from db: " + " dataResourceId is empty";
            Debug.logError(errorMsg, module);
            request.setAttribute("_ERROR_MESSAGE_", errorMsg);
            return "error";
        }

        try {
            GenericValue dataResource = delegator.findByPrimaryKeyCache("DataResource", UtilMisc.toMap("dataResourceId", dataResourceId));
            if (!"Y".equals(dataResource.getString("isPublic"))) {
                // now require login...
                GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
                if (userLogin == null) {
                    String errorMsg = "You must be logged in to download the Data Resource with ID [" + dataResourceId + "]";
                    Debug.logError(errorMsg, module);
                    request.setAttribute("_ERROR_MESSAGE_", errorMsg);
                    return "error";
                }

                // make sure the logged in user can download this content; otherwise is a pretty big security hole for DataResource records...
                // TODO: should we restrict the roleTypeId?
                //add by changsy  此处不需要严格检查只有party自身的内容才可以看到 at 2015.12.28
                /*List<GenericValue> contentAndRoleList = delegator.findByAnd("ContentAndRole",
                        UtilMisc.toMap("partyId", userLogin.get("partyId"), "dataResourceId", dataResourceId));
                if (contentAndRoleList.size() == 0) {
                    String errorMsg = "You do not have permission to download the Data Resource with ID [" + dataResourceId + "], ie you are not associated with it.";
                    Debug.logError(errorMsg, module);
                    request.setAttribute("_ERROR_MESSAGE_", errorMsg);
                    return "error";
                }*/
            }

            String mimeType = DataResourceWorker.getMimeType(dataResource);

            // hack for IE and mime types
            String userAgent = request.getHeader("User-Agent");
            if (userAgent.indexOf("MSIE") > -1) {
                Debug.logInfo("Found MSIE changing mime type from - " + mimeType, module);
                mimeType = "application/octet-stream";
            }

            if (mimeType != null) {
                response.setContentType(mimeType);
            }
            OutputStream os = response.getOutputStream();
            DataResourceWorker.streamDataResource(os, delegator, dataResourceId, "", application.getInitParameter("webSiteId"), UtilHttp.getLocale(request), application.getRealPath("/"));
            os.flush();
        } catch (GenericEntityException e) {
            String errMsg = "Error downloading digital product content: " + e.toString();
            Debug.logError(e, errMsg, module);
            request.setAttribute("_ERROR_MESSAGE_", errMsg);
            return "error";
        } catch (GeneralException e) {
            String errMsg = "Error downloading digital product content: " + e.toString();
            Debug.logError(e, errMsg, module);
            request.setAttribute("_ERROR_MESSAGE_", errMsg);
            return "error";
        } catch (IOException e) {
            String errMsg = "Error downloading digital product content: " + e.toString();
            Debug.logError(e, errMsg, module);
            request.setAttribute("_ERROR_MESSAGE_", errMsg);
            return "error";
        }

        return "success";
    }


    /** Dual create and edit event.
     *  Needed to make permission criteria available to services.
     */
    public static String persistDataResource(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = null;
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
        Map<String, Object> paramMap = UtilHttp.getParameterMap(request);
        String dataResourceId = (String)paramMap.get("dataResourceId");
        GenericValue dataResource = delegator.makeValue("DataResource");
        dataResource.setPKFields(paramMap);
        dataResource.setNonPKFields(paramMap);
        Map<String, Object> serviceInMap = UtilMisc.makeMapWritable(dataResource);
        serviceInMap.put("userLogin", userLogin);
        String mode = (String)paramMap.get("mode");
        Locale locale = UtilHttp.getLocale(request);

        if (mode != null && "UPDATE".equals(mode)) {
            try {
                result = dispatcher.runSync("updateDataResource", serviceInMap);
            } catch (GenericServiceException e) {
                String errMsg = UtilProperties.getMessage(DataEvents.err_resource, "dataEvents.error_call_update_service", locale);
                String errorMsg = "Error calling the updateDataResource service." + e.toString();
                Debug.logError(e, errorMsg, module);
                request.setAttribute("_ERROR_MESSAGE_", errMsg + e.toString());
                return "error";
            }
        } else {
            mode = "CREATE";
            try {
                result = dispatcher.runSync("createDataResource", serviceInMap);
            } catch (GenericServiceException e) {
                String errMsg = UtilProperties.getMessage(DataEvents.err_resource, "dataEvents.error_call_create_service", locale);
                String errorMsg = "Error calling the createDataResource service." + e.toString();
                Debug.logError(e, errorMsg, module);
                request.setAttribute("_ERROR_MESSAGE_", errMsg + e.toString());
                return "error";
            }
            dataResourceId = (String)result.get("dataResourceId");
            dataResource.set("dataResourceId", dataResourceId);
        }

        String returnStr = "success";
        if ("CREATE".equals(mode)) {
            // Set up return message to guide selection of follow on view
            request.setAttribute("dataResourceId", result.get("dataResourceId"));
            String dataResourceTypeId = (String)serviceInMap.get("dataResourceTypeId");
            if (dataResourceTypeId != null) {
                 if ("ELECTRONIC_TEXT".equals(dataResourceTypeId)
                     || "IMAGE_OBJECT".equals(dataResourceTypeId)) {
                    returnStr = dataResourceTypeId;
                 }
            }
        }

        return returnStr;
    }
}
