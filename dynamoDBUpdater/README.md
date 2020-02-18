# DynamoDBUpdater
This is a simple lambda that gets trigerred by an S3 event demonstrating how a DynamoDB table can be updated with using Java API

## Design
* `S3CrudServiceImpl` has all the methods that is capable of uploading an object to S3, uploading an object to S3 with metadata, downloading a given object, reading metadata from a given object, and deleting a given object in S3.

* `ClientBuilderManager` utility class build an DynamoDB and S3 clients to access DynamoDB and S3 APIs.

* `PropertyManager` reads required properties from the environemnt and makes them available across the application.

## Configuring AWS Infrastructure
* Create a `DynamoDB` table(e.g. book_inventory).

* Use `isbn` as the primary key.

* Create an `AWS Lambda` with your preferred finction name(e.g. bookInventoryDynamoDBUpdater) with runtime `Java 8`. In `Permissions` section select a role by creating a new role or use an existing role(e.g. `LambdaFullAccess`).

* In created `Lambda Function`'s `Configuration` tab's `Designer` section, add an `S3` trigger. Note that trigger can be configured at `S3` end too. Which ever mechanism you prefer would do the job.

* In `Environment variables` section add below 2 environment variables.
`dynamoDB_table_bookInventory` and `dynamoDB_table_bookInventory_primaryKey`.
Save the relevant values you want to use(ones you used while creating the table).

* In `Function code` section, update `Handler` to denote event listener of the trigger to `updater.dynamoDBUpdater.event.S3RequestEventHandler::handleRequest`

* Upload the `Lambda` `jar` file and click on `Save`

* Note that if you have a higher load you may need to configure `Basic settings' section with appropriate values for memory and execution time thresholds.

## How to test
This is a simple `Java` `Maven` project.

Build `Lambda` `jar` with `mvn clean install package` and upload it to the created `Lambda` function.

Drop a file to the `S3` bucket with `metadata`. You may need to use a simple code snippet to get the relevant `metadata` attached to the object.

Check `DynamoDB` table. you should see the record.

You can check `Lambda` logs with using `CloudWatch` too.

