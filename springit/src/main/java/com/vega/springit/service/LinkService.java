package com.vega.springit.service;

import com.vega.springit.Repository.LinkRepository;
import com.vega.springit.model.Link;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LinkService {
    private final LinkRepository linkRepository;

    public LinkService(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    public List<Link> findAll(){
        return linkRepository.findAll();
    }
}
