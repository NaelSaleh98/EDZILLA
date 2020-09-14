package com.vega.springit.Controller;


import com.vega.springit.Repository.LinkRepository;
import com.vega.springit.model.Link;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
/*
@RestController
@RequestMapping("/links")    //all the method in the following class will be in the url "/links"+<what each method mapping >
*/
public class LinkRestController {

/*

    private LinkRepository linkRepository;

    public LinkRestController(LinkRepository linkRepository) { // or you can remove this constructor and add to the above member @Autowired
        this.linkRepository = linkRepository;
    }

    /////////////////list
    @GetMapping("/") //or @RequestMapping( path = "/", method = RequestMethod.GET ) //this will be at url /links/
    public List<Link> list() {
        return linkRepository.findAll();
    }

    ///////////////// CRUD
    @PostMapping("/create") //this will be at url /links/create
    public Link create(@ModelAttribute Link link) {
        return linkRepository.save(link);
    }

    @GetMapping("/{id}")//this will be at url /links/{id}
    public Optional<Link> read(@PathVariable Long id) {
        return linkRepository.findById(id);
    }

    @PutMapping("/")
    public Link update(@ModelAttribute Link link) {
        return linkRepository.save(link);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {

        linkRepository.deleteById(id);
    }


 */
}
