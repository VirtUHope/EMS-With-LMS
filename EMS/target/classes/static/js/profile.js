const API_BASE = 'http://localhost:8080/api/employees';  // Adjust base API URL

const token = localStorage.getItem("authToken");

if (!token) {
    alert("Please login first");
    window.location.href = "/index.html"; // Redirect to login page
}

async function fetchProfile() {
  try {
    const res = await fetch(`${API_BASE}/profile`, {
      headers: {
        "Authorization": `${token}`
      }
    });

    if (!res.ok) throw new Error("Error fetching profile");

    const profileData = await res.json();
    displayProfile(profileData);

  } catch (err) {
    alert(err.message);
  }
}

function displayProfile(profile) {
  const profileInfoDiv = document.getElementById("profileInfo");

  profileInfoDiv.innerHTML = `
    <p><strong>Username:</strong> ${profile.username}</p>
    <p><strong>Name:</strong> ${profile.name}</p>
    <p><strong>Email:</strong> ${profile.email}</p>
    <p><strong>Department:</strong> ${profile.department}</p>
    <p><strong>Salary:</strong> $${profile.salary}</p>
    <p><strong>Role:</strong> ${profile.role}</p>
  `;
}

document.getElementById("logoutButton").addEventListener("click", () => {
  localStorage.removeItem("authToken");
  window.location.href = "/index.html";  // Redirect to login page
});

// Fetch the profile information when the page loads
fetchProfile();
