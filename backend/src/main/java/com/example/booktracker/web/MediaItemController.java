package com.example.booktracker.web;

import com.example.booktracker.domain.MediaType;
import com.example.booktracker.domain.ReadingStatus;
import com.example.booktracker.dto.MediaItemRequest;
import com.example.booktracker.dto.MediaItemResponse;
import com.example.booktracker.service.MediaItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class MediaItemController {

    private final MediaItemService service;

    public MediaItemController(MediaItemService service) {
        this.service = service;
    }

    @GetMapping
    public List<MediaItemResponse> list(@RequestParam(required = false) MediaType type,
                                        @RequestParam(required = false) ReadingStatus status) {
        return service.list(type, status);
    }

    @GetMapping("/{id}")
    public MediaItemResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MediaItemResponse create(@Valid @RequestBody MediaItemRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public MediaItemResponse update(@PathVariable Long id, @Valid @RequestBody MediaItemRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}