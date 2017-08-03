package jd.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jd.dao.ProductDao;
import jd.service.ProductService;
import jd.utils.ResultModel;

@Service
public class ProductServiceImpl implements ProductService {
	private static final int ROWS = 60;
	@Autowired
	private ProductDao productDao;

	@Override
	public ResultModel queryProduct(String queryString, String catalog_name, String price, int sort, Integer page)
			throws Exception {
		SolrQuery query = new SolrQuery();
		//添加主查询条件
		if(queryString!=null && !"".equals(queryString)){
			query.setQuery(queryString);
		}else{
			query.setQuery("*:*");
		}
		//添加过滤条件
		if(catalog_name!=null && !"".equals(catalog_name)){
			query.addFilterQuery("product_catalog_name:"+catalog_name);
		}
		if(price!=null && !"".equals(price)){
			String[] split = price.split("-");
			query.addFilterQuery("product_price:"+"["+split[0]+" TO "+split[1]+" ]");
		}
		//添加排序
		if(sort==0){
			query.setSort("product_price", ORDER.asc);
		}else{
			query.setSort("product_price", ORDER.desc);
		}
		//添加分页
		int start=(page-1)*ROWS;
		query.setStart(start);
		query.setRows(ROWS);
		//添加默认查询字段
		query.set("df", "product_keywords");
		//设置高亮
		query.setHighlight(true);
		query.addHighlightField("product_name");
		query.setHighlightSimplePre("<em style='color:red'>");
		query.setHighlightSimplePost("</em>");
		
		//调用dao执行查询
		ResultModel resultModel = productDao.queryProduct(query);
		//计算总页数,封装
		resultModel.setCurPage(page);
		int pageCount=(resultModel.getRecordCount().intValue()+ROWS-1)/ROWS;
		resultModel.setPageCount(pageCount);
		return resultModel;
	}

}
