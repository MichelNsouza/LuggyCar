package com.br.luggycar.api.controllers;

import com.br.luggycar.api.entities.Category;
import com.br.luggycar.api.entities.Client;
import com.br.luggycar.api.entities.OptionalItem;
import com.br.luggycar.api.exceptions.ResourceNotFoundException;
import com.br.luggycar.api.requests.ClientResquest;
import com.br.luggycar.api.requests.OptionalItemRequest;
import com.br.luggycar.api.services.OptionalItemService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/optionalitem")
public class OptionalItemController {

    @Autowired
    private OptionalItemService optionalItemService;

    @GetMapping
    public ResponseEntity<List<OptionalItem>> getAllOptionalItem() {
        return ResponseEntity.ok(optionalItemService.findAllOptionalItem());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OptionalItem> getOptionalItemById(@PathVariable long id){
        Optional<OptionalItem> optionalItem = optionalItemService.findOptionalItemById(id);

        return ResponseEntity.ok().body(optionalItem.get());
    }

    @PostMapping("/registration")
    public ResponseEntity<OptionalItem> saveOptionalItem(@RequestBody OptionalItemRequest optionalItemRequest) {
        OptionalItem optionalItem = optionalItemService.insertOptionalItem(optionalItemRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(optionalItem);

    }

    @PutMapping("/{id}")
    public ResponseEntity<OptionalItem> updateOptionalItem(@PathVariable Long id, @RequestBody OptionalItemRequest optionalItemResquest) throws ResourceNotFoundException {

        Optional<OptionalItem> optionalItem = optionalItemService.findOptionalItemById(id);

        OptionalItem OptionalItemResponse = optionalItemService.updateOptionalItem(id,optionalItemResquest );

        return ResponseEntity.ok().body(OptionalItemResponse);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteOptionalItem(@PathVariable long id) {

        return ResponseEntity.ok(optionalItemService.deleteOptionalItem(id));
    }


}
