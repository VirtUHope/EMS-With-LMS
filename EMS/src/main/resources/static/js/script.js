const API_BASE = "http://localhost:8080";

document.addEventListener("DOMContentLoaded", () => {
  const loginForm = document.getElementById("loginForm");
  if (loginForm) {
    loginForm.addEventListener("submit", async (e) => {
      e.preventDefault();
      const username = document.getElementById("username").value;
      const password = document.getElementById("password").value;

      try {
        const response = await fetch(`${API_BASE}/login`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ username, password })
        });

        if (!response.ok) throw new Error("Invalid credentials");

        const data = await response.json();
        localStorage.setItem("token", data.token);
        window.location.href = "profile.html";
      } catch (err) {
        document.getElementById("error").textContent = err.message;
      }
    });
  }

  // Profile
  if (window.location.pathname.includes("profile.html")) {
    loadProfile();
  }
});

async function loadProfile() {
  const token = localStorage.getItem("token");
  if (!token) return (window.location.href = "index.html");

  const res = await fetch(`${API_BASE}/api/employees/me`, {
    headers: { Authorization: `Bearer ${token}` }
  });

  if (!res.ok) {
    alert("Unauthorized! Please login again.");
    return (window.location.href = "index.html");
  }

  const data = await res.json();
  document.getElementById("profile").innerHTML = `
    <p><strong>Username:</strong> ${data.username}</p>
    <p><strong>Name:</strong> ${data.name}</p>
    <p><strong>Email:</strong> ${data.email}</p>
    <p><strong>Department:</strong> ${data.department}</p>
    <p><strong>Role:</strong> ${data.role}</p>
  `;
}

function logout() {
  localStorage.removeItem("token");
  window.location.href = "index.html";
}
