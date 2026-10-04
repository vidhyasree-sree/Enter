const loginForm = document.getElementById("loginForm");
const message = document.getElementById("message");

loginForm.addEventListener("submit", function (event) {

    event.preventDefault();

    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value.trim();

    // Demo login credentials
    if (username === "admin" && password === "admin123") {

        message.textContent = "Login Successful!";
        message.style.color = "#86efac";

        setTimeout(function () {
            window.location.href = "dashboard.html";
        }, 800);

    } else {

        message.textContent = "Invalid username or password!";
        message.style.color = "#fca5a5";
    }
});