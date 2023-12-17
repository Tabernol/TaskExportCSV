package com.krasnopolskyi.krasnopolskyiexportcsv.controller;

import com.krasnopolskyi.krasnopolskyiexportcsv.dto.GoodsResponse;
import com.krasnopolskyi.krasnopolskyiexportcsv.service.GoodsServiceWithFile;
import com.krasnopolskyi.krasnopolskyiexportcsv.service.GoodsServiceWithPath;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
/**
 * GoodsController handles incoming HTTP requests related to goods data, providing endpoints
 * for uploading goods information from either a file path or a multipart file.
 */
@RestController
@RequestMapping("/goods")
@Slf4j
public class GoodsController {
    private final GoodsServiceWithPath goodsServiceWithPath;
    private final GoodsServiceWithFile goodsServiceWithFile;
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "Sorry, but something went wrong. Try again later";

    public GoodsController(GoodsServiceWithPath goodsServiceWithPath, GoodsServiceWithFile goodsServiceWithFile) {
        this.goodsServiceWithPath = goodsServiceWithPath;
        this.goodsServiceWithFile = goodsServiceWithFile;
    }
    /**
     * Handles HTTP POST requests for uploading goods data from a file path.
     *
     * @param path The file path containing the goods data located in the resource folder. Use without quotes
     * @return ResponseEntity containing the response for the goods upload operation.
     * @throws IOException If an I/O error occurs.
     */
    @PostMapping("/path")
    public ResponseEntity<GoodsResponse> uploadFromPath(@RequestBody String path)
            throws IOException{
        GoodsResponse response = goodsServiceWithPath.uploadRows(path);
        return (response.getRowInserted() > 0) ? ResponseEntity.status(HttpStatus.OK).body(response) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    /**
     * Handles HTTP POST requests for uploading goods data from a multipart file.
     *
     * @param file The multipart file containing the goods data.
     * @return ResponseEntity containing the response for the goods upload operation.
     * @throws IOException If an I/O error occurs.
     */
    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GoodsResponse> uploadFromFile(@RequestPart(value = "file") MultipartFile file)
            throws IOException {
        GoodsResponse response = goodsServiceWithFile.uploadRows(file);
        return (response.getRowInserted() > 0) ? ResponseEntity.status(HttpStatus.OK).body(response) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    /**
     * Handles exceptions that occur within the controller, returning an internal server error
     * response with an error message.
     *
     * @param exception The exception that occurred.
     * @return ResponseEntity containing the internal server error response.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllUncaughtException(Exception exception) {
        log.error("Unknown error occurred", exception);
        return ResponseEntity.internalServerError().body(INTERNAL_SERVER_ERROR_MESSAGE);
    }
}
