const API_BASE = 'http://localhost:8080/api/employees';  // Adjust base API URL

document.getElementById("loginForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const username = document.getElementById("username").value;
  const password = document.getElementById("password").value;

  try {
    const response = await fetch(`${API_BASE}/login`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        username: username,
        password: password
      })
    });


    if (!response.ok) throw new Error("Invalid credentials");

    const data = await response.json();
    const token = data.token;

    // Save token to localStorage
    localStorage.setItem("authToken", token);
    alert("Login successful!");

    // Redirect to profile page after successful login
    window.location.href = "/profile.html";  // Change to the correct path for profile page

  } catch (err) {
    document.getElementById("errorMessage").innerText = err.message;
  }
});
