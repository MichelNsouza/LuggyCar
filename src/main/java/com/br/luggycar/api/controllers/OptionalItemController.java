package com.br.luggycar.api.controllers;

import com.br.luggycar.api.entities.OptionalItem;
import com.br.luggycar.api.requests.OptionalItemRequest;
import com.br.luggycar.api.services.OptionalItemService;
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
    public ResponseEntity<OptionalItem> getOptionalItemById(@PathVariable long id) {
        Optional<OptionalItem> optionalItem = optionalItemService.findOptionalItemById(id);

        if (optionalItem.isPresent()) {
            return ResponseEntity.ok(optionalItem.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/registration")
    public ResponseEntity<OptionalItem> saveOptionalItem(@RequestBody OptionalItemRequest optionalItemRequest) {
        OptionalItem optionalItem = optionalItemService.insertOptionalItem(optionalItemRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(optionalItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OptionalItem> updateOptionalItem(@PathVariable Long id, @RequestBody OptionalItemRequest optionalItemRequest) {
        OptionalItem updatedItem = optionalItemService.updateOptionalItem(id, optionalItemRequest);

        if (updatedItem != null) {
            return ResponseEntity.ok(updatedItem);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteOptionalItem(@PathVariable long id) {
        boolean isDeleted = optionalItemService.deleteOptionalItem(id);
        if (isDeleted) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
    }
}
