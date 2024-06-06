const admin = require("firebase-admin");
const bcrypt = require("bcrypt");
const { v4: uuidv4 } = require("uuid");
const jwt = require("jsonwebtoken");
require("dotenv").config();

const db = admin.database();
const saltRounds = 10;

const blacklist = [];

const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/;

function validatePassword(password) {
    return passwordRegex.test(password);
}

function validateEmail(email) {
    return emailRegex.test(email);
}

// get all users
exports.getAllUsers = async (req, res) => {
    try {
        const users = [];
        const usersRef = db.ref("users");
        const snapshot = await usersRef.once("value");
        
        snapshot.forEach((childSnapshot) => {
            users.push({ id: childSnapshot.key, ...childSnapshot.val() });
        });

        res.status(200).send(users);
    } catch (error) {
        console.error("Error during fetching users:", error);
        res.status(500).send(error.message);
    }
};

exports.register = async (req, res) => {
    const { username, email, password } = req.body;
    console.log("Register request received:", { username, email, password });
    try {
        if (!username || !email || !password) {
        return res.status(400).send("Username, email, and password are required");
        }

        if (!validateEmail(email)) {
        return res.status(400).send("Invalid email format");
        }

        if (!validatePassword(password)) {
        return res
            .status(400)
            .send(
            "Password must be at least 8 characters and include a lowercase letter, an uppercase letter, and a digit"
            );
        }

        // Check for existing username before hashing password
        const existingUserRef = db
        .ref("users")
        .orderByChild("username")
        .equalTo(username);
        const snapshot = await existingUserRef.once("value");

        if (snapshot.exists()) {
        return res
            .status(409)
            .send("Username already exists. Please choose a different one.");
        }

        const hashedPassword = await bcrypt.hash(password, saltRounds);
        console.log("Password hashed successfully");

        const userId = uuidv4();

        await db.ref("users/" + userId).set({
        username,
        email,
        password: hashedPassword,
        });
        console.log("User data saved to Realtime Database");
        
        res.status(201).send({ uid: userId });
    } catch (error) {
        console.error("Error during registration:", error);
        res.status(400).send(error.message);
    }
};

exports.login = async (req, res) => {
    const { email, password } = req.body;
    try {
        if (!email || !password) {
        return res.status(400).send("Email and password are required");
        }
        
        if (!validateEmail(email)) {
        return res.status(400).send("Invalid email format");
        }
        
        const userSnapshot = await db
        .ref("users")
        .orderByChild("email")
        .equalTo(email)
        .once("value");

        if (!userSnapshot.exists()) {
        return res.status(404).send("User not found");
        }

        let user = null;
        userSnapshot.forEach((childSnapshot) => {
        user = { id: childSnapshot.key, ...childSnapshot.val() };
        });

        const isPasswordValid = await bcrypt.compare(password, user.password);
        if (!isPasswordValid) {
        return res.status(401).send("Invalid password");
        }

        // Generate a JWT token
        const accessToken = jwt.sign({ userId: user.id, email: user.email }, process.env.JWTSECRET, {
        expiresIn: "1h",
        });

        console.log("User logged in successfully");

        // tampilkan username user di console log
        console.log("Username user:", user.username);

        res.status(200).send({ accessToken, userId: user.id });
    } catch (error) {
        console.error("Error during login:", error);
        res.status(400).send(error.message);
    }
};

exports.logout = async (req, res) => {
    const authHeader = req.headers.authorization;
    const token = authHeader && authHeader.split(" ")[1];
    try {
        
        if (token) {
            blacklist.push(token);
        }
        
        res.clearCookie("accessToken"); 

        console.log("User logged out");

        res.status(200).json({ message: "Logout successful" });
    } catch (error) {
        console.error("Error during logout:", error);
        res.status(500).json({ message: "Logout failed" });
    }
};

exports.blacklist = blacklist;