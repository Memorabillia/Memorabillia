const admin = require("firebase-admin");
const axios = require("axios");
const db = admin.database();

exports.getBooks = async (req, res) => {
    try {
        const snapshot = await db.ref("books").once("value");
        const books = snapshot.val();
        res.status(200).send(books);
    } catch (error) {
        res.status(500).send(error.message);
    }
};

exports.getBookById = async (req, res) => {
    const { id } = req.params;
    try {
        const snapshot = await db.ref(`books/${id}`).once("value");
        const book = snapshot.val();
        if (book) {
            res.status(200).send(book);
        } else {
            res.status(404).send("Book not found");
        }
    } catch (error) {
        res.status(500).send(error.message);
    }
};

exports.updateBookStatus = async (req, res) => {
    const { id } = req.params;
    const { status, progress } = req.body;
    const userId = req.user.userId;

    // Define allowed statuses
    const allowedStatuses = ["wanttoread", "currentlyreading", "finishedread"];

    // Check if provided status is allowed
    if (!allowedStatuses.includes(status)) {
        return res.status(400).send("Invalid status value");
    }

    // Ensure progress is only provided for "wanttoread" status
    if (status !== "currentlyreading" && progress !== undefined) {
        return res.status(400).send("Progress can only be set for 'wanttoread' status");
    }

    try {
        const updateData = { status };
        if (status === "currentlyreading") {
            updateData.progress = progress !== undefined ? progress : 0;
        } else {
            updateData.progress = null;  // explicitly set progress to null for other statuses
        }

        await db.ref(`user_books/${userId}/${id}`).set(updateData);
        res.status(200).send("Book status updated successfully");
    } catch (error) {
        res.status(500).send(error.message);
    }
};


exports.getUserBooks = async (req, res) => {
    const userId = req.user.userId;

    try {
        // Ambil buku milik user
        const userBooksSnapshot = await db.ref(`user_books/${userId}`).once("value");
        const userBooks = userBooksSnapshot.val();

        if (!userBooks) {
            return res.status(200).send([]);
        }

        // Ambil semua buku untuk mendapatkan detail buku
        const booksSnapshot = await db.ref("books").once("value");
        const books = booksSnapshot.val();

        // Buat daftar buku dengan detail lengkap
        const userBooksWithDetails = Object.keys(userBooks).map(bookId => {
            return {
                ...userBooks[bookId],
                details: books[bookId]
            };
        });

        res.status(200).send(userBooksWithDetails);
    } catch (error) {
        res.status(500).send(error.message);
    }
};


exports.recommend = async (req, res) => {
    let userId = req.user.userId; // Extracted user ID from token
    
    // Convert userId to float32
    userId = new Float32Array([Number(userId)])[0];
    
    try {
        // Call the Flask API to get recommended book titles
        const response = await axios.post("https://predict-eo2t275afq-et.a.run.app/recommend", {
            user_id: userId,
        });
        
        const recommendedISBNs = response.data.recommendations;
        
        // Retrieve complete book details from Firebase based on the recommended titles
        const booksRef = db.ref("books");
        const snapshot = await booksRef.once("value");
        const books = snapshot.val();

        const recommendedBooks = [];

        recommendedISBNs.forEach(isbn => {
            for (const bookId in books) {
                if (books[bookId].ISBN === isbn) {
                    recommendedBooks.push({
                        bookId,
                        ...books[bookId]
                    });
                    break;
                }
            }
        });

        res.status(200).send(recommendedBooks);
    } catch (error) {
        res.status(500).send(error.message);
    }
};