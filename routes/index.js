const express = require("express");
const router = express.Router();

const authController = require("../controllers/authController");
const bookController = require("../controllers/bookController");
const activityController = require("../controllers/activityController");
const authenticateToken = require("../middlewares/authenticateToken");

// Auth routes
router.post("/register", authController.register);
router.post("/login", authController.login);
router.post("/logout", authenticateToken, authController.logout);

/*
// Book routes
router.get("/books/home", authenticateToken, bookController.getHomeBooks);
router.get("/books/category/:category", bookController.getBooksByCategory);
router.get("/books/search", bookController.searchBooks);
router.get("/books/:id", bookController.getBookDescription);

// Activity routes
router.get("/activities", activityController.getUserActivities);
router.post("/activities", activityController.addUserActivity);
*/

module.exports = router;
