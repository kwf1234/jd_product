package jd.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jd.dao.ProductDao;
import jd.pojo.ProductModel;
import jd.utils.ResultModel;
@Repository
public class ProductDaoImpl implements ProductDao {

	@Autowired
	private SolrServer solrServer;
	
	@Override
	public ResultModel queryProduct(SolrQuery query) throws Exception {
		ResultModel resultModel = new ResultModel();
		//执行查询
		QueryResponse queryResponse = solrServer.query(query);
		SolrDocumentList documentList = queryResponse.getResults();
		//将总记录数放入到结果类中
		resultModel.setRecordCount(documentList.getNumFound());
		//新建product的集合
		List<ProductModel> productList=new ArrayList<ProductModel>();
		for (SolrDocument solrDocument : documentList) {
			//新建一个productModel对象
			ProductModel productModel = new ProductModel();
			String pid=(String)solrDocument.get("id");
			//设置商品名称
			String name="";
			Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
			List<String> list = highlighting.get(solrDocument.get("id")).get("product_name");
			if(list!=null && list.size()>0){
				name=list.get(0);
			}else{
				name=(String)solrDocument.get("product_name");
			}
			String catalog_name=(String)solrDocument.get("product_catalog_name");
			float price=(float)solrDocument.get("product_price");
			String description=(String)solrDocument.get("product_description");
			String picture=(String)solrDocument.get("product_picture");
			//将属性赋值给productModel
			productModel.setCatalog_name(catalog_name);
			productModel.setDescription(description);
			productModel.setName(name);
			productModel.setPicture(picture);
			productModel.setPid(pid);
			productModel.setPrice(price);
			//将productModel添加到集合中
			productList.add(productModel);
		}
		resultModel.setProductList(productList);
		return resultModel;
	}

}
