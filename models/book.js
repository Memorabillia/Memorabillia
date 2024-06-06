const { Storage } = require("@google-cloud/storage");
const admin = require("firebase-admin");
const path = require("path");
const csv = require("csv-parser");
require("dotenv").config();

// Path to the service account key files
const serviceAccountPath = path.resolve(__dirname, "../service/serviceAccountKey.json");
const serviceAccountOwnerPath = path.resolve(__dirname, "../service/serviceAccountKey(Owner).json");

admin.initializeApp({
    credential: admin.credential.cert(serviceAccountPath),
    databaseURL: process.env.DATABASE_URL,
});

// Initialize Google Cloud Storage
const storage = new Storage({
    projectId: "capstonebangkitc241-ps182",
    keyFilename: serviceAccountOwnerPath,
});

const db = admin.database();

exports.importBooksFromGCS = async (bucketName, fileName) => {
    const bucket = storage.bucket(bucketName);
    const file = bucket.file(fileName);

    return new Promise((resolve, reject) => {
        const results = [];

        file
            .createReadStream()
            .pipe(csv())
            .on("data", (data) => results.push(data))
            .on("end", async () => {
                try {
                    const updates = {};
                    // const counterRef = db.ref("bookId");
                    let counterSnapshot = await counterRef.once("value");
                    let bookId = counterSnapshot.val() || 0;

                    results.forEach((book) => {
                        bookId += 1;
                        updates[`books/${bookId}`] = book;
                    });

                    await db.ref().update(updates);
                    // await counterRef.set(bookId);
                    console.log(
                        "Books imported successfully from Google Cloud Storage to Firebase."
                    );
                    resolve();
                } catch (error) {
                    reject(error);
                }
            })
            .on("error", (error) => {
                reject(error);
            });
    });
};
