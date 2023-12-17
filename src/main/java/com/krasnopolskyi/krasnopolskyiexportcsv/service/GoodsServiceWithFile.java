package com.krasnopolskyi.krasnopolskyiexportcsv.service;

import com.krasnopolskyi.krasnopolskyiexportcsv.dto.GoodsResponse;
import com.krasnopolskyi.krasnopolskyiexportcsv.entity.Goods;
import com.krasnopolskyi.krasnopolskyiexportcsv.repository.GoodsRepository;
import com.krasnopolskyi.krasnopolskyiexportcsv.utill.CustomValidator;
import com.krasnopolskyi.krasnopolskyiexportcsv.utill.GoodsMapper;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvMalformedLineException;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * GoodsServiceWithFile provides a service for validating and uploading goods data from a CSV file
 * received as a MultipartFile. This class handles validation of CSV rows and batch insertion
 * of goods into the database.
 */
@Service
@Slf4j
public class GoodsServiceWithFile {

    private final GoodsRepository goodsRepository;
    private static final int CUSTOM_BATCH_SIZE = 10; //only for example value

    public GoodsServiceWithFile(GoodsRepository goodsRepository) {
        this.goodsRepository = goodsRepository;
    }

    /**
     * Validates the rows in the CSV file received as a MultipartFile.
     * The download will not be executed if some row is not valid
     *
     * @param multipartFile The MultipartFile containing the CSV file.
     * @throws IOException If an I/O error occurs.
     */
    public GoodsResponse uploadRows(MultipartFile multipartFile) throws IOException {
        int insertedRows = 0;
        int validatedRows;
        try {
            validatedRows = validateRows(multipartFile);
            insertedRows = insertRows(multipartFile);
            return new GoodsResponse(insertedRows, insertedRows + " rows successfully saved out of " + validatedRows);
        } catch (CsvValidationException e) {
            return new GoodsResponse(insertedRows, e.getMessage());
        }
    }

    /**
     * Validates the rows in the CSV file received as a MultipartFile.
     *
     * @param multipartFile The MultipartFile containing the CSV file.
     * @return count of validated rows
     * @throws IOException            If an I/O error occurs.
     * @throws CsvValidationException If CSV validation fails.
     */
    private int validateRows(MultipartFile multipartFile) throws IOException, CsvValidationException {
        int line = 1;
        try (CSVReader reader = readFromUploadFile(multipartFile)) {
            while ((reader.readNext()) != null) {
                line++;
            }
            return line - 1;
        } catch (CsvValidationException e) {
            log.error("The problem occurs in the row " + line + ". Message = " + e.getMessage());
            throw new CsvValidationException("The CSV file is invalid. The problem occurs in the row "
                    + line + ". Message = " + e.getMessage());
        } catch (CsvMalformedLineException e) {
            log.error("The problem occurs in the row " + line + " missing closed quotes");
            throw new CsvValidationException("The CSV file is invalid. The problem occurs in the row " + line +
                    " missing closed quotes");
        }
    }

    /**
     * Inserts goods into the database in batches using a provided MultipartFile.
     *
     * @param multipartFile The MultipartFile containing the CSV file.
     * @return The total number of goods inserted.
     * @throws CsvValidationException If CSV validation fails.
     * @throws IOException            If an I/O error occurs.
     */
    private int insertRows(MultipartFile multipartFile)
            throws CsvValidationException, IOException {
        String[] record;
        int totalInserted = 0;
        List<Goods> goods = new ArrayList<>();
        try (CSVReader reader = readFromUploadFile(multipartFile)) {
            while ((record = reader.readNext()) != null) {
                goods.add(GoodsMapper.map(record));
                //if size of list == CUSTOM_BATCH_SIZE than will be call to the repository
                // and save rows
                if (goods.size() == CUSTOM_BATCH_SIZE) {
                    totalInserted += goodsRepository.insertRows(goods);
                    goods.clear();
                }

            }
            //inserting latest goods
            if (!goods.isEmpty()) {
                totalInserted += goodsRepository.insertRows(goods);
            }
        }
        return totalInserted;
    }


    /**
     * Reads and validates the CSV file from the provided MultipartFile.
     *
     * @param multipartFile The MultipartFile containing the CSV file.
     * @return A CSVReader instance with the configured CSV parser and row validator.
     * @throws IOException If an I/O error occurs.
     */
    private CSVReader readFromUploadFile(MultipartFile multipartFile) throws IOException {
        CSVParser parser = new CSVParserBuilder().withSeparator('\t').build();
        return new CSVReaderBuilder(new InputStreamReader(multipartFile.getInputStream()))
                .withCSVParser(parser)// custom parser
                .withRowValidator(new CustomValidator())//custom validator
                .build();

    }
}
