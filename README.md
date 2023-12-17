# CSV Data Upload Application

This Java Spring Boot application allows you to upload data from a CSV file using the OpenCSV library. The application provides two REST API endpoints for testing purposes, and Swagger-UI is available at [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) after starting the program.

## Endpoints

1. **/goods/path**: Provide the name of a file located in the resources folder and execute the program.

2. **/goods/file**: Upload a CSV file and run the program.

After this step, the subsequent actions performed on the file are the same.

## File Validation

1. The validation process utilizes a custom validator.
2. The program reads the file line by line and throws an exception if a specific row is not valid. The error message can be read in the controller's response.
3. Each row should:
    - Not be empty.
    - Have a Tab as a delimiter.
    - Have a length of 4.
4. The first parameter should:
    - Be a number from 1 to 2,147,483,647.

5. The second parameter should:
    - Not exceed 64 characters.
    - Not be null or empty.
    - Not contain a single double-quote (").

6. The third parameter should:
    - Be a number.
    - For a non-integer, use a dot (.) as a decimal separator.

7. The fourth parameter should:
    - Be either 0 or 1.

## Insertion Process

1. If all rows are valid, the insertion process begins.
2. The program reads the file again, transforming each row into a Goods object, and stores it in a list.
3. When the list size reaches CUSTOM_BATCH_SIZE (in this case, 10), a request is made to the repository layer to insert this data in one go.
4. The request for inserting the remaining data will occur even if the list is smaller, optimizing the process and avoiding potential issues with large files.

## Duplicate ID Handling

- If a record with a specific ID already exists in the database, it is ignored.
- The program continues to work with subsequent records, keeping track of the number of inserted records.

## Repository Query

- The repository performs batch updates using the JDBC template.
- In case of a data access exception, the program logs an error and throws a custom exception.

## Technologies Used

- Java 17
- Spring Boot 3.2.0
- MySQL

## Running the Application

1. Clone the repository.
2. Build the application.
3. Run the application.
4. Access Swagger-UI at [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html).
5. Use the provided endpoints to test CSV file upload.

