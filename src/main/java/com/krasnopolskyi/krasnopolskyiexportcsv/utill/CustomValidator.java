package com.krasnopolskyi.krasnopolskyiexportcsv.utill;

import com.krasnopolskyi.krasnopolskyiexportcsv.entity.TypeOfGoods;
import com.opencsv.exceptions.CsvValidationException;
import com.opencsv.validators.RowValidator;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/**
 * CustomValidator provides validation logic for CSV rows.
 */
public class CustomValidator implements RowValidator {
    /**
     * Checks if the given CSV row is valid.
     *
     * @param strings The array representing the CSV row.
     * @return {@code true} if the row is valid, {@code false} otherwise.
     */
    @Override
    public boolean isValid(String[] strings) {
        try {
            validateRow(strings);
            validationId(strings[0]);
            validateName(strings[1]);
            validatePrice(strings[2]);
            validateTypeOfGoods(strings[3]);
            return true;
        } catch (CsvValidationException e) {
            return false;
        }
    }
    /**
     * Validates the given CSV row.
     *
     * @param strings The array representing the CSV row.
     * @throws CsvValidationException if the row is not valid.
     */
    @Override
    public void validate(String[] strings) throws CsvValidationException {
        validateRow(strings);
        validationId(strings[0]);
        validateName(strings[1]);
        validatePrice(strings[2]);
        validateTypeOfGoods(strings[3]);
    }

    private void validateRow(String[] strings) throws CsvValidationException {
        if (strings == null) throw new CsvValidationException("Row is null");
        if (strings.length != 4) throw new CsvValidationException("Row length is not 4");
    }

    private void validationId(String stringId) throws CsvValidationException {
        try {
            int id = Integer.parseInt(stringId);
            if (id <= 0) {
                throw new NumberFormatException("Column 1(id) is not valid = " + stringId);
            }
        } catch (NumberFormatException e) {
            throw new CsvValidationException(e.getMessage());
        }
    }

    private void validateName(String name) throws CsvValidationException {
        if (name.isBlank()) {
            throw new CsvValidationException("Column 2(name) is not valid. It is blank");
        }
        if (name.length() > 64) {
            throw new CsvValidationException("Column 2(name) is not valid. It is too long = " + name);
        }
    }

    private void validatePrice(String price) throws CsvValidationException {
        try {
            Double.parseDouble(price);
        } catch (NumberFormatException e) {
            throw new CsvValidationException("Column 3(price) is not valid = " + price);
        }
    }


    private void validateTypeOfGoods(String typeOfGoods) throws CsvValidationException {
        Set<Integer> types = Stream.of(TypeOfGoods.values())
                .map(type -> type.number)
                .collect(Collectors.toSet());
        try {
            if (!types.contains(Integer.valueOf(typeOfGoods))) {
                throw new CsvValidationException("Column 4(type_of_goods) is not valid. " +
                        "It is not a type_of_goods. " + typeOfGoods);
            }
        } catch (NumberFormatException e) {
            throw new CsvValidationException("Column 4(type_of_goods) is not valid " + typeOfGoods);
        }
    }
}
