package jd.dao;

import org.apache.solr.client.solrj.SolrQuery;

import jd.utils.ResultModel;

public interface ProductDao {

	ResultModel queryProduct(SolrQuery query) throws Exception;
}
