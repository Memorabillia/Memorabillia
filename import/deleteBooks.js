// Import Firebase Admin SDK
const admin = require("firebase-admin");
const path = require("path");
require("dotenv").config();

// Inisialisasi aplikasi Firebase Admin SDK
const serviceAccountPath = path.resolve(__dirname, "../service/serviceAccountKey.json");

admin.initializeApp({
    credential: admin.credential.cert(serviceAccountPath),
    databaseURL: process.env.DATABASE_URL,
});

// Referensi ke Realtime Database
const db = admin.database();

// Path ke node 'books' yang ingin dihapus
const pathToBooksNode = "/bookId";

// Hapus node 'books'
db.ref(pathToBooksNode)
  .remove()
  .then(() => {
    console.log('Node "books" berhasil dihapus');
  })
  .catch((error) => {
    console.error('Error menghapus node "books":', error);
  });
