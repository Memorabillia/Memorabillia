const jwt = require("jsonwebtoken");
const { blacklist } = require("../controllers/authController");
require("dotenv").config();

function authenticateToken(req, res, next) {
    const authHeader = req.headers.authorization;
    if (!authHeader || !authHeader.startsWith('Bearer ')) {
        return res.status(401).send("Authorization header is required");
    }

    const token = authHeader.split(' ')[1];
    if (!token) {
        return res.status(401).send("Token not found");
    }

    if (blacklist.includes(token)) {
        return res.status(403).send("Forbidden");
    }

    jwt.verify(token, process.env.JWTSECRET, (err, user) => {
        if (err) {
            return res.status(403).send("Invalid or expired token");
        }
        req.user = user;
        next();
    });
}

module.exports = authenticateToken;
