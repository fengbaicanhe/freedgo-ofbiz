/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package org.ofbiz.product.catalog;

import javolution.util.FastList;
import org.ofbiz.base.util.*;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.product.category.CategoryWorker;
import org.ofbiz.product.store.ProductStoreWorker;
import org.ofbiz.webapp.website.WebSiteWorker;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * CatalogWorker - Worker class for catalog related functionality
 */
public class CatalogWorker {

  public static final String module = org.ofbiz.product.catalog.CatalogWorker.class.getName();

  private CatalogWorker() {
  }

  public static String getWebSiteId(ServletRequest request) {
    return WebSiteWorker.getWebSiteId(request);
  }

  public static GenericValue getWebSite(ServletRequest request) {
    return WebSiteWorker.getWebSite(request);
  }

  public static List<String> getAllCatalogIds(ServletRequest request) {
    List<String> catalogIds = FastList.newInstance();
    List<GenericValue> catalogs = null;
    Delegator delegator = (Delegator) request.getAttribute("delegator");
    try {
      catalogs = delegator.findList("ProdCatalog", null, null, UtilMisc.toList("catalogName"), null, false);
    } catch (GenericEntityException e) {
      Debug.logError(e, "Error looking up all catalogs", module);
    }
    if (catalogs != null) {
      for (GenericValue c : catalogs) {
        catalogIds.add(c.getString("prodCatalogId"));
      }
    }
    return catalogIds;
  }

  public static List<GenericValue> getStoreCatalogs(ServletRequest request) {
    String productStoreId = ProductStoreWorker.getProductStoreId(request);
    Delegator delegator = (Delegator) request.getAttribute("delegator");
    return getStoreCatalogs(delegator, productStoreId);
  }

  /**
   * @param siteId
   * @param delegator
   * @return
   */
  public static List<GenericValue> getStoreCatalogsBySiteId(String siteId, Delegator delegator) {
    String productStoreId = ProductStoreWorker.getProductStoreIdBySiteId(siteId, delegator);
    return getStoreCatalogs(delegator, productStoreId);
  }

  /**
   * @param request
   * @param showInTop Y:在首页栏目显示 N:不在 ,其他 所有
   * @return
   */
  public static List<GenericValue> getShopCatalogs(ServletRequest request, String showInTop) {
    String productStoreId = ProductStoreWorker.getProductStoreId(request);
    Delegator delegator = (Delegator) request.getAttribute("delegator");
    return getShopCatalogs(delegator, productStoreId, showInTop);
  }

  public static List<GenericValue> getShopCatalogs(Delegator delegator, String productStoreId, String showInTop) {
    try {
      if ("Y".equalsIgnoreCase(showInTop)) {
        return EntityUtil.filterByDate(delegator.findByAndCache("ProdCatalogStore", UtilMisc.toMap("productStoreId", productStoreId, "showInTop", "Y", "showInShop", "Y"), UtilMisc.toList("sequenceNum", "prodCatalogId")), true);

      }
      if ("N".equalsIgnoreCase(showInTop)) {
        return EntityUtil.filterByDate(delegator.findByAndCache("ProdCatalogStore", UtilMisc.toMap("productStoreId", productStoreId, "showInTop", "N", "showInShop", "Y"), UtilMisc.toList("sequenceNum", "prodCatalogId")), true);

      } else {
        return EntityUtil.filterByDate(delegator.findByAndCache("ProdCatalogStore", UtilMisc.toMap("productStoreId", productStoreId, "showInShop", "Y"), UtilMisc.toList("sequenceNum", "prodCatalogId")), true);
      }
    } catch (GenericEntityException e) {
      Debug.logError(e, "Error looking up store catalogs for store with id " + productStoreId, module);
    }
    return null;
  }

  public static List<GenericValue> getStoreCatalogs(Delegator delegator, String productStoreId) {
    try {
      return EntityUtil.filterByDate(delegator.findByAndCache("ProductStoreCatalog", UtilMisc.toMap("productStoreId", productStoreId), UtilMisc.toList("sequenceNum", "prodCatalogId")), true);
    } catch (GenericEntityException e) {
      Debug.logError(e, "Error looking up store catalogs for store with id " + productStoreId, module);
    }
    return null;
  }

 

 
  public static List<GenericValue> getProdCatalogCategories(ServletRequest request, String prodCatalogId, String prodCatalogCategoryTypeId) {
    Delegator delegator = (Delegator) request.getAttribute("delegator");
    List<GenericValue> catalogCategories = getProdCatalogCategories(delegator, prodCatalogId, prodCatalogCategoryTypeId);
    return catalogCategories;
  }

  public static List<GenericValue> getProdCatalogCategories(Delegator delegator, String prodCatalogId, String prodCatalogCategoryTypeId) {
    try {
      List<GenericValue> prodCatalogCategories = EntityUtil.filterByDate(delegator.findByAndCache("ProdCatalogCategory",
         UtilMisc.toMap("prodCatalogId", prodCatalogId),
         UtilMisc.toList("sequenceNum", "productCategoryId")), true);

      if (UtilValidate.isNotEmpty(prodCatalogCategoryTypeId) && prodCatalogCategories != null) {
        prodCatalogCategories = EntityUtil.filterByAnd(prodCatalogCategories,
           UtilMisc.toMap("prodCatalogCategoryTypeId", prodCatalogCategoryTypeId));
      }
      return prodCatalogCategories;
    } catch (GenericEntityException e) {
      Debug.logError(e, "Error looking up ProdCatalogCategories for prodCatalog with id " + prodCatalogId, module);
    }
    return null;
  }

  /**
   * 获取当前目录下的分类(通过类型)
   */

  public static List<GenericValue> getCategoriesByProdCatalogCategoryType(ServletRequest request, String prodCatalogId, String prodCatalogCategoryTypeId) {
    Delegator delegator = (Delegator) request.getAttribute("delegator");
    return getProdCatalogCategories(delegator, prodCatalogId, prodCatalogCategoryTypeId);
  }

  /**
   * 获取当前目录下的分类(通过类型获取唯一)
   */

  public static GenericValue getCategoryByProdCatalogCategoryType(ServletRequest request, String prodCatalogId, String prodCatalogCategoryTypeId) {
    Delegator delegator = (Delegator) request.getAttribute("delegator");
    List<GenericValue> prodCatalogCategories = getProdCatalogCategories(delegator, prodCatalogId, prodCatalogCategoryTypeId);
    return EntityUtil.getFirst(prodCatalogCategories);
  }


  /**
   * Retrieves the current prodCatalogId.  First it will attempt to find it from a special
   * request parameter or session attribute named CURRENT_CATALOG_ID.  Failing that, it will
   * get the first catalog from the database as specified in getCatalogIdsAvailable().
   * If this behavior is undesired, give the user a selectable list of catalogs.
   */
  public static String getCurrentCatalogId(ServletRequest request) {
    HttpSession session = ((HttpServletRequest) request).getSession();
    Map<String, Object> requestParameters = UtilHttp.getParameterMap((HttpServletRequest) request);
    String prodCatalogId = null;
    boolean fromSession = false;

    // first see if a new catalog was specified as a parameter
    prodCatalogId = (String) requestParameters.get("CURRENT_CATALOG_ID");
    // if no parameter, try from session
    if (prodCatalogId == null) {
      prodCatalogId = (String) session.getAttribute("CURRENT_CATALOG_ID");
      if (prodCatalogId != null) {
          fromSession = true;
      }
    }
    // get it from the database
    if (prodCatalogId == null) {
      List<String> catalogIds = getCatalogIdsAvailable(request);
      if (UtilValidate.isNotEmpty(catalogIds)) {
          prodCatalogId = catalogIds.get(0);
      }
    }

    if (!fromSession) {
      if (Debug.verboseOn()) {
          Debug.logVerbose("[CatalogWorker.getCurrentCatalogId] Setting new catalog name: " + prodCatalogId, module);
      }
      session.setAttribute("CURRENT_CATALOG_ID", prodCatalogId);
      CategoryWorker.setTrail(request, FastList.<String>newInstance());
    }
    return prodCatalogId;
  }

  public static List<String> getCatalogIdsAvailable(ServletRequest request) {
    List<GenericValue> storeCatalogs = getStoreCatalogs(request);
    return getCatalogIdsAvailable(storeCatalogs);
  }

  public static List<String> getCatalogIdsAvailable(Delegator delegator, String productStoreId, String partyId) {
    List<GenericValue> storeCatalogs = getStoreCatalogs(delegator, productStoreId);
     return getCatalogIdsAvailable(storeCatalogs);
  }

  public static List<String> getCatalogIdsAvailable( List<GenericValue> storeCatalogs) {
    List<String> categoryIds = FastList.newInstance();
    List<GenericValue> allCatalogLinks = FastList.newInstance();
    if (storeCatalogs != null) {
        allCatalogLinks.addAll(storeCatalogs);
    }

    if (allCatalogLinks.size() > 0) {
      for (GenericValue catalogLink : allCatalogLinks) {
        categoryIds.add(catalogLink.getString("prodCatalogId"));
      }
    }
    return categoryIds;
  }

  public static String getCatalogName(ServletRequest request) {
    return getCatalogName(request, getCurrentCatalogId(request));
  }

  public static String getCatalogName(ServletRequest request, String prodCatalogId) {
    if (prodCatalogId == null || prodCatalogId.length() <= 0) {
        return null;
    }
    Delegator delegator = (Delegator) request.getAttribute("delegator");

    try {
      GenericValue prodCatalog = delegator.findByPrimaryKeyCache("ProdCatalog", UtilMisc.toMap("prodCatalogId", prodCatalogId));

      if (prodCatalog != null) {
        return prodCatalog.getString("catalogName");
      }
    } catch (GenericEntityException e) {
      Debug.logError(e, "Error looking up name for prodCatalog with id " + prodCatalogId, module);
    }

    return null;
  }

  public static String getContentPathPrefix(ServletRequest request) {
    GenericValue prodCatalog = getProdCatalog(request, getCurrentCatalogId(request));

    if (prodCatalog == null) {
        return "";
    }
    String contentPathPrefix = prodCatalog.getString("contentPathPrefix");

    return StringUtil.cleanUpPathPrefix(contentPathPrefix);
  }

  public static String getTemplatePathPrefix(ServletRequest request) {
    GenericValue prodCatalog = getProdCatalog(request, getCurrentCatalogId(request));

    if (prodCatalog == null) {
        return "";
    }
    String templatePathPrefix = prodCatalog.getString("templatePathPrefix");

    return StringUtil.cleanUpPathPrefix(templatePathPrefix);
  }

  public static GenericValue getProdCatalog(ServletRequest request) {
    return getProdCatalog(request, getCurrentCatalogId(request));
  }

  public static GenericValue getProdCatalog(ServletRequest request, String prodCatalogId) {
    if (prodCatalogId == null || prodCatalogId.length() <= 0) {
        return null;
    }
    Delegator delegator = (Delegator) request.getAttribute("delegator");

    try {
      return delegator.findByPrimaryKeyCache("ProdCatalog", UtilMisc.toMap("prodCatalogId", prodCatalogId));
    } catch (GenericEntityException e) {
      Debug.logError(e, "Error looking up name for prodCatalog with id " + prodCatalogId, module);
      return null;
    }
  }

  public static String getCatalogTopCategoryId(ServletRequest request) {
    return getCatalogTopCategoryId(request, getCurrentCatalogId(request));
  }

  public static String getCatalogTopCategoryId(ServletRequest request, String prodCatalogId) {
    if (prodCatalogId == null || prodCatalogId.length() <= 0) {
        return null;
    }
    List<GenericValue> prodCatalogCategories = getProdCatalogCategories(request, prodCatalogId, "PCCT_BROWSE_ROOT");
    if (UtilValidate.isNotEmpty(prodCatalogCategories)) {
      GenericValue prodCatalogCategory = EntityUtil.getFirst(prodCatalogCategories);
      return prodCatalogCategory.getString("productCategoryId");
    } else {
      return null;
    }
  }

  public static String getCatalogSearchCategoryId(ServletRequest request) {
    return getCatalogSearchCategoryId(request, getCurrentCatalogId(request));
  }

  public static String getCatalogSearchCategoryId(ServletRequest request, String prodCatalogId) {
    return getCatalogSearchCategoryId((Delegator) request.getAttribute("delegator"), prodCatalogId);
  }

  public static String getCatalogSearchCategoryId(Delegator delegator, String prodCatalogId) {
    if (prodCatalogId == null || prodCatalogId.length() <= 0) {
        return null;
    }

    List<GenericValue> prodCatalogCategories = getProdCatalogCategories(delegator, prodCatalogId, "PCCT_SEARCH");
    if (UtilValidate.isNotEmpty(prodCatalogCategories)) {
      GenericValue prodCatalogCategory = EntityUtil.getFirst(prodCatalogCategories);
      return prodCatalogCategory.getString("productCategoryId");
    } else {
      return null;
    }
  }

  public static String getCatalogViewAllowCategoryId(Delegator delegator, String prodCatalogId) {
    if (prodCatalogId == null || prodCatalogId.length() <= 0) {
        return null;
    }

    List<GenericValue> prodCatalogCategories = getProdCatalogCategories(delegator, prodCatalogId, "PCCT_VIEW_ALLW");
    if (UtilValidate.isNotEmpty(prodCatalogCategories)) {
      GenericValue prodCatalogCategory = EntityUtil.getFirst(prodCatalogCategories);
      return prodCatalogCategory.getString("productCategoryId");
    } else {
      return null;
    }
  }

  public static String getCatalogPurchaseAllowCategoryId(Delegator delegator, String prodCatalogId) {
    if (prodCatalogId == null || prodCatalogId.length() <= 0) {
        return null;
    }

    List<GenericValue> prodCatalogCategories = getProdCatalogCategories(delegator, prodCatalogId, "PCCT_PURCH_ALLW");
    if (UtilValidate.isNotEmpty(prodCatalogCategories)) {
      GenericValue prodCatalogCategory = EntityUtil.getFirst(prodCatalogCategories);
      return prodCatalogCategory.getString("productCategoryId");
    } else {
      return null;
    }
  }

  public static String getCatalogCategoryIdByTypeId(Delegator delegator, String prodCatalogId, String prodCatalogCategoryTypeId) {
    if (prodCatalogId == null || prodCatalogId.length() <= 0) {
        return null;
    }

    List<GenericValue> prodCatalogCategories = getProdCatalogCategories(delegator, prodCatalogId, prodCatalogCategoryTypeId);

    if (UtilValidate.isNotEmpty(prodCatalogCategories)) {
      GenericValue prodCatalogCategory = EntityUtil.getFirst(prodCatalogCategories);

      return prodCatalogCategory.getString("productCategoryId");
    } else {
      return null;
    }
  }

  public static String getCatalogPromotionsCategoryId(ServletRequest request) {
    return getCatalogPromotionsCategoryId(request, getCurrentCatalogId(request));
  }

  public static String getCatalogPromotionsCategoryId(ServletRequest request, String prodCatalogId) {
    if (prodCatalogId == null || prodCatalogId.length() <= 0) {
        return null;
    }

    List<GenericValue> prodCatalogCategories = getProdCatalogCategories(request, prodCatalogId, "PCCT_PROMOTIONS");

    if (UtilValidate.isNotEmpty(prodCatalogCategories)) {
      GenericValue prodCatalogCategory = EntityUtil.getFirst(prodCatalogCategories);

      return prodCatalogCategory.getString("productCategoryId");
    } else {
      return null;
    }
  }


  public static boolean getCatalogQuickaddUse(ServletRequest request) {
    return getCatalogQuickaddUse(request, getCurrentCatalogId(request));
  }

  public static boolean getCatalogQuickaddUse(ServletRequest request, String prodCatalogId) {
    if (prodCatalogId == null || prodCatalogId.length() <= 0) {
        return false;
    }
    Delegator delegator = (Delegator) request.getAttribute("delegator");

    try {
      GenericValue prodCatalog = delegator.findByPrimaryKeyCache("ProdCatalog", UtilMisc.toMap("prodCatalogId", prodCatalogId));

      if (prodCatalog != null) {
        return "Y".equals(prodCatalog.getString("useQuickAdd"));
      }
    } catch (GenericEntityException e) {
      Debug.logError(e, "Error looking up name for prodCatalog with id " + prodCatalogId, module);
    }
    return false;
  }

  public static String getCatalogQuickaddCategoryPrimary(ServletRequest request) {
    return getCatalogQuickaddCategoryPrimary(request, getCurrentCatalogId(request));
  }

  public static String getCatalogQuickaddCategoryPrimary(ServletRequest request, String prodCatalogId) {
    if (prodCatalogId == null || prodCatalogId.length() <= 0) {
        return null;
    }

    List<GenericValue> prodCatalogCategories = getProdCatalogCategories(request, prodCatalogId, "PCCT_QUICK_ADD");

    if (UtilValidate.isNotEmpty(prodCatalogCategories)) {
      GenericValue prodCatalogCategory = EntityUtil.getFirst(prodCatalogCategories);

      return prodCatalogCategory.getString("productCategoryId");
    } else {
      return null;
    }
  }

  public static Collection<String> getCatalogQuickaddCategories(ServletRequest request) {
    return getCatalogQuickaddCategories(request, getCurrentCatalogId(request));
  }

  public static Collection<String> getCatalogQuickaddCategories(ServletRequest request, String prodCatalogId) {
    if (prodCatalogId == null || prodCatalogId.length() <= 0) {
        return null;
    }

    Collection<String> categoryIds = FastList.newInstance();

    Collection<GenericValue> prodCatalogCategories = getProdCatalogCategories(request, prodCatalogId, "PCCT_QUICK_ADD");

    if (UtilValidate.isNotEmpty(prodCatalogCategories)) {
      for (GenericValue prodCatalogCategory : prodCatalogCategories) {
        categoryIds.add(prodCatalogCategory.getString("productCategoryId"));
      }
    }

    return categoryIds;
  }

  public static String getCatalogTopEbayCategoryId(ServletRequest request, String prodCatalogId) {
    if (prodCatalogId == null || prodCatalogId.length() <= 0) {
        return null;
    }

    List<GenericValue> prodCatalogCategories = getProdCatalogCategories(request, prodCatalogId, "PCCT_EBAY_ROOT");

    if (UtilValidate.isNotEmpty(prodCatalogCategories)) {
      GenericValue prodCatalogCategory = EntityUtil.getFirst(prodCatalogCategories);

      return prodCatalogCategory.getString("productCategoryId");
    } else {
      return null;
    }
  }

  /**
   * 获取当前的分类归属目录
   */

  public static List<GenericValue> getCatalogByCategoryAndType(ServletRequest request, String productCategoryId, String prodCatalogCategoryTypeId) {
    Delegator delegator = (Delegator) request.getAttribute("delegator");
    List<GenericValue> result = FastList.newInstance();
    try {
      List<GenericValue> prodCatalogCategories = EntityUtil.filterByDate(delegator.findByAndCache("ProdCatalogCategory",
         UtilMisc.toMap("productCategoryId", productCategoryId), UtilMisc.toList("sequenceNum", "prodCatalogId")), true);
      if (prodCatalogCategoryTypeId != null) {
        if (prodCatalogCategories != null) {
          for (int i = 0; i < prodCatalogCategories.size(); i++) {
            GenericValue prodCatalogCategory =  prodCatalogCategories.get(i);
            if (prodCatalogCategory.get("prodCatalogCategoryTypeId").equals(prodCatalogCategoryTypeId)) {
              result.add(prodCatalogCategory);
            }
          }
        }
      } else {
        result = prodCatalogCategories;
      }
    } catch (GenericEntityException e) {
      e.printStackTrace();
    }
    return result;

  }


  public static List<GenericValue> getShopCatalogsTypeCatalogCategoryTypeId(ServletRequest request, String productStoreId, String prodCatalogCategoryTypeId) {
    Delegator delegator = (Delegator) request.getAttribute("delegator");
    return getShopCatalogsTypeCatalogCategoryTypeId(delegator, productStoreId, prodCatalogCategoryTypeId);
  }

  /**
   * 根据类型获取所有的目录
   *
   * @param delegator
   * @param productStoreId
   * @param prodCatalogCategoryTypeId
   * @return
   */
  public static List<GenericValue> getShopCatalogsTypeCatalogCategoryTypeId(Delegator delegator, String productStoreId, String prodCatalogCategoryTypeId) {
    List<GenericValue> result = FastList.newInstance();
    try {
      List<GenericValue> prodCatalogCategories = EntityUtil.filterByDate(delegator.findByAndCache("ProdCatalogCategory",
         UtilMisc.toMap("prodCatalogCategoryTypeId", prodCatalogCategoryTypeId), UtilMisc.toList("sequenceNum", "prodCatalogId")), true);
      if (prodCatalogCategories != null) {
        for (int i = 0; i < prodCatalogCategories.size(); i++) {
          GenericValue prodCatalogCategory = prodCatalogCategories.get(i);
          result.add(prodCatalogCategory.getRelatedOne("ProdCatalog"));
        }
      }
    } catch (GenericEntityException e) {
      Debug.logError(e, "Error looking up store catalogs for store with id " + productStoreId, module);
    }
    return result;
  }

}
