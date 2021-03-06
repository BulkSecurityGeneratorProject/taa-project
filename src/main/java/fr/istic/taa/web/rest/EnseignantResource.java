package fr.istic.taa.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.istic.taa.domain.Enseignant;
import fr.istic.taa.service.EnseignantService;
import fr.istic.taa.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Enseignant.
 */
@RestController
@RequestMapping("/api")
public class EnseignantResource {

    private final Logger log = LoggerFactory.getLogger(EnseignantResource.class);

    @Inject
    private EnseignantService enseignantService;

    /**
     * POST  /enseignants : Create a new enseignant.
     *
     * @param enseignant the enseignant to create
     * @return the ResponseEntity with status 201 (Created) and with body the new enseignant, or with status 400 (Bad Request) if the enseignant has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/enseignants",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Enseignant> createEnseignant(@RequestBody Enseignant enseignant) throws URISyntaxException {
        log.debug("REST request to save Enseignant : {}", enseignant);
        if (enseignant.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("enseignant", "idexists", "A new enseignant cannot already have an ID")).body(null);
        }
        Enseignant result = enseignantService.save(enseignant);
        return ResponseEntity.created(new URI("/api/enseignants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("enseignant", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /enseignants : Updates an existing enseignant.
     *
     * @param enseignant the enseignant to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated enseignant,
     * or with status 400 (Bad Request) if the enseignant is not valid,
     * or with status 500 (Internal Server Error) if the enseignant couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/enseignants",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Enseignant> updateEnseignant(@RequestBody Enseignant enseignant) throws URISyntaxException {
        log.debug("REST request to update Enseignant : {}", enseignant);
        if (enseignant.getId() == null) {
            return createEnseignant(enseignant);
        }
        Enseignant result = enseignantService.save(enseignant);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("enseignant", enseignant.getId().toString()))
            .body(result);
    }

    /**
     * GET  /enseignants : get all the enseignants.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of enseignants in body
     */
    @RequestMapping(value = "/enseignants",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Enseignant> getAllEnseignants() {
        log.debug("REST request to get all Enseignants");
        return enseignantService.findAll();
    }

    /**
     * GET  /enseignants/:id : get the "id" enseignant.
     *
     * @param id the id of the enseignant to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the enseignant, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/enseignants/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Enseignant> getEnseignant(@PathVariable Long id) {
        log.debug("REST request to get Enseignant : {}", id);
        Enseignant enseignant = enseignantService.findOne(id);
        return Optional.ofNullable(enseignant)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /enseignants/:id : delete the "id" enseignant.
     *
     * @param id the id of the enseignant to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/enseignants/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEnseignant(@PathVariable Long id) {
        log.debug("REST request to delete Enseignant : {}", id);
        enseignantService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("enseignant", id.toString())).build();
    }

    /**
     * SEARCH  /_search/enseignants?query=:query : search for the enseignant corresponding
     * to the query.
     *
     * @param query the query of the enseignant search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/enseignants",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Enseignant> searchEnseignants(@RequestParam String query) {
        log.debug("REST request to search Enseignants for query {}", query);
        return enseignantService.search(query);
    }


}
