package com.stephen.popcorn.esdao;

import com.stephen.popcorn.model.dto.post.PostEsDTO;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 帖子 ES 操作
 *
 * @author stephen qiu
 */
public interface PostEsDao extends ElasticsearchRepository<PostEsDTO, Long> {
	
	List<PostEsDTO> findByUserId(Long userId);
}