const admin = require("firebase-admin");

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

    try {
        await db.ref(`user_books/${userId}/${id}`).set({ status, progress });
        res.status(200).send("Book status updated successfully");
    } catch (error) {
        res.status(500).send(error.message);
    }
};

exports.getUserBooks = async (req, res) => {
    const userId = req.user.userId;

    try {
        const snapshot = await db.ref(`user_books/${userId}`).once("value");
        const userBooks = snapshot.val();
        res.status(200).send(userBooks);
    } catch (error) {
        res.status(500).send(error.message);
    }
};
