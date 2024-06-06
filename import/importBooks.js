const { importBooksFromGCS } = require("../models/book");

const bucketName = "c241-ps182-bucket";
const fileName = "bookcleaned.csv";

importBooksFromGCS(bucketName, fileName)
    .then(() => {
        console.log("Import completed.");
        process.exit(0);
    })
    .catch((error) => {
        console.error("Error importing books:", error);
        process.exit(1);
    });