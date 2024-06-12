const express = require("express");
const router = express.Router();

const authController = require("../controllers/authController");
const bookController = require("../controllers/bookController");
const authenticateToken = require("../middlewares/authenticateToken");

// check if server is running
router.get('/', (req, res) => {
    res.send('Success');
});

// Auth routes
router.post("/register", authController.register);
router.post("/login", authController.login);
router.post("/logout", authenticateToken, authController.logout);

// User routes
router.get("/users", authenticateToken, authController.getAllUsers);

// Book routes
router.get('/books', authenticateToken, bookController.getBooks);
/*
router.get('/books/:id', authenticateToken, getBookById);
router.put('/books/:id/status', authenticateToken, updateBookStatus);
router.get('/user/books', authenticateToken, getUserBooks);
*/

// Recommendation routes
router.post('/recommend', authenticateToken, bookController.recommend); 




module.exports = router;
