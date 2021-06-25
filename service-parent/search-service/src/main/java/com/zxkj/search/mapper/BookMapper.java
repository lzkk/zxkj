package com.zxkj.search.mapper;//package com.hckj.product.microservice.service.es;

import com.zxkj.search.document.BookDoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BookMapper extends ElasticsearchRepository<BookDoc, String> {

    Page<BookDoc> findByAuthor(String author, Pageable pageable);

}
