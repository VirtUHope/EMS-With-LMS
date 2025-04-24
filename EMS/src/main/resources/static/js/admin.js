const API_BASE = 'http://localhost:8080/api/employees';  // Adjust base API URL

const token = localStorage.getItem("authToken");

if (!token) {
    alert("Please login first");
    window.location.href = "/index.html"; // Redirect to login page
}

async function fetchEmployees() {
  try {
    const res = await fetch(`${API_BASE}/data`, {
      headers: {
        "Authorization": `${token}`
      }
    });
    console.log("Response Status:", res.status);
    if (!res.ok) throw new Error("Error fetching employee data");

    const employees = await res.json();
    displayEmployees(employees);

  } catch (err) {
    alert(err.message);
  }
}

function displayEmployees(employees) {
  const employeeListDiv = document.getElementById("employeeList");

  employees.forEach(employee => {
    employeeListDiv.innerHTML += `
      <div>
        <p><strong>Username:</strong> ${employee.username}</p>
        <p><strong>Name:</strong> ${employee.name}</p>
        <p><strong>Email:</strong> ${employee.email}</p>
        <p><strong>Department:</strong> ${employee.department}</p>
        <p><strong>Salary:</strong> $${employee.salary}</p>
        <p><strong>Role:</strong> ${employee.role}</p>
      </div>
      <hr>
    `;
  });
}

document.getElementById("logoutButton").addEventListener("click", () => {
  localStorage.removeItem("authToken");
  window.location.href = "/index.html";  // Redirect to login page
});

// Fetch the employees when the page loads
fetchEmployees();
