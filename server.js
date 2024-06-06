const express = require("express");
const bodyParser = require("body-parser");
const admin = require("firebase-admin");
const serviceAccount = require("./service/serviceAccountKey.json"); 
require("dotenv").config();

const app = express();

app.use(bodyParser.json());

admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
    databaseURL: process.env.DATABASE_URL,
});

const routes = require("./routes/index");
app.use("/", routes);

const PORT = process.env.PORT;

app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});
