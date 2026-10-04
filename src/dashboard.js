let attendanceRecords = [];
let leaveRequests = [];
let employees = [];

const API_URL = "http://localhost:8081";


// =========================
// SECTION NAVIGATION
// =========================

function showSection(sectionId) {

    const sections = document.querySelectorAll(".content-section");

    sections.forEach(section => {
        section.classList.remove("active-section");
    });

    const selectedSection = document.getElementById(sectionId);

    if (selectedSection) {
        selectedSection.classList.add("active-section");
    }

    const navItems = document.querySelectorAll(".nav-item");

    navItems.forEach(item => {
        item.classList.remove("active");
    });

    const titles = {
        dashboard: "Dashboard",
        employees: "Employee Management",
        attendance: "Attendance Management",
        leave: "Leave Requests",
        reports: "Attendance Reports",
        search: "Search Employee"
    };

    const pageTitle = document.getElementById("pageTitle");

    if (pageTitle) {
        pageTitle.textContent =
            titles[sectionId] || "Dashboard";
    }

    updateDashboard();
}


// =========================
// EMPLOYEE MANAGEMENT
// =========================

async function loadEmployees() {

    try {

        const response =
            await fetch(`${API_URL}/api/employees`);

        if (!response.ok) {
            throw new Error("Failed to load employees");
        }

        employees = await response.json();

        displayEmployees();
        updateEmployeeCount();

    } catch (error) {

        console.error(
            "Employee loading error:",
            error
        );

        alert(
            "Cannot connect to Java backend.\n" +
            "Please make sure BackendServer is running."
        );
    }
}


function displayEmployees() {

    const table =
        document.getElementById("employeeTable");

    if (!table) return;

    table.innerHTML = "";

    employees.forEach(employee => {

        const row =
            document.createElement("tr");

        row.innerHTML = `
            <td>${employee.employee_id}</td>
            <td>${employee.name}</td>
            <td>${employee.department}</td>
            <td>${employee.phone}</td>
            <td>
                <span class="badge active">
                    ${employee.status}
                </span>
            </td>
        `;

        table.appendChild(row);
    });
}


function updateEmployeeCount() {

    const totalEmployees =
        document.getElementById("totalEmployees");

    if (totalEmployees) {
        totalEmployees.textContent =
            employees.length;
    }
}


// =========================
// ADD EMPLOYEE
// =========================

async function saveEmployee() {

    const id =
        document.getElementById("employeeId")
        .value.trim();

    const name =
        document.getElementById("employeeName")
        .value.trim();

    const department =
        document.getElementById("employeeDepartment")
        .value.trim();

    const phone =
        document.getElementById("employeePhone")
        .value.trim();

    if (
        id === "" ||
        name === "" ||
        department === "" ||
        phone === ""
    ) {

        alert("Please fill all fields!");
        return;
    }

    try {

        const data =
            `employeeId=${encodeURIComponent(id)}` +
            `&name=${encodeURIComponent(name)}` +
            `&department=${encodeURIComponent(department)}` +
            `&phone=${encodeURIComponent(phone)}` +
            `&status=Active`;

        const response =
            await fetch(
                `${API_URL}/api/addEmployee`,
                {
                    method: "POST",
                    headers: {
                        "Content-Type":
                            "application/x-www-form-urlencoded"
                    },
                    body: data
                }
            );

        const result =
            await response.json();

        if (response.ok) {

            alert("Employee added successfully!");

            closeEmployeeModal();

            document.getElementById("employeeId").value = "";
            document.getElementById("employeeName").value = "";
            document.getElementById("employeeDepartment").value = "";
            document.getElementById("employeePhone").value = "";

            await loadEmployees();

        } else {

            alert(
                result.message ||
                "Failed to add employee"
            );
        }

    } catch (error) {

        console.error(error);

        alert(
            "Could not connect to Java backend!"
        );
    }
}


function addEmployee() {
    openEmployeeModal();
}


// =========================
// EMPLOYEE MODAL
// =========================

function openEmployeeModal() {

    const modal =
        document.getElementById("employeeModal");

    if (modal) {
        modal.style.display = "block";
    }
}


function closeEmployeeModal() {

    const modal =
        document.getElementById("employeeModal");

    if (modal) {
        modal.style.display = "none";
    }
}


// =========================
// ATTENDANCE
// =========================

async function markAttendance() {

    const employeeId =
        document.getElementById("attendanceId")
        .value.trim();

    const status =
        document.getElementById("attendanceStatus")
        .value;

    if (!employeeId || !status) {

        alert(
            "Please enter Employee ID and select status."
        );

        return;
    }

    try {

        const response =
            await fetch(
                `${API_URL}/api/attendance`,
                {
                    method: "POST",
                    headers: {
                        "Content-Type":
                            "application/x-www-form-urlencoded"
                    },
                    body:
                        "employeeId=" +
                        encodeURIComponent(employeeId) +
                        "&status=" +
                        encodeURIComponent(status)
                }
            );

        const result =
            await response.json();

        if (response.ok) {

            alert(
                "Attendance marked successfully!"
            );

            await loadAttendance();

            document.getElementById(
                "attendanceId"
            ).value = "";

        } else {

            alert(
                result.message ||
                "Failed to mark attendance"
            );
        }

    } catch (error) {

        console.error(
            "Attendance error:",
            error
        );

        alert(
            "Backend connection failed!"
        );
    }
}


async function loadAttendance() {

    try {

        const response =
            await fetch(
                `${API_URL}/api/attendance`
            );

        if (!response.ok) {
            throw new Error(
                "Failed to load attendance"
            );
        }

        attendanceRecords =
            await response.json();

        displayAttendance();
        updateDashboard();

    } catch (error) {

        console.error(
            "Attendance loading error:",
            error
        );
    }
}


function displayAttendance() {

    const table =
        document.getElementById(
            "attendanceTable"
        );

    if (!table) return;

    table.innerHTML = "";

    attendanceRecords.forEach(record => {

        const row =
            document.createElement("tr");

        row.innerHTML = `
            <td>${record.attendance_date}</td>
            <td>${record.employee_id}</td>
            <td>${record.attendance_status}</td>
        `;

        table.appendChild(row);
    });
}


// =========================
// LEAVE MANAGEMENT
// =========================

// Apply Leave
async function applyLeave() {

    const id =
        document.getElementById("leaveId")
        .value.trim();

    const reason =
        document.getElementById("leaveReason")
        .value.trim();

    if (
        id === "" ||
        reason === ""
    ) {

        alert("Please fill all fields!");
        return;
    }

    try {

        const response =
            await fetch(
                `${API_URL}/api/leave`,
                {
                    method: "POST",
                    headers: {
                        "Content-Type":
                            "application/x-www-form-urlencoded"
                    },
                    body:
                        "employeeId=" +
                        encodeURIComponent(id) +
                        "&reason=" +
                        encodeURIComponent(reason)
                }
            );

        const result =
            await response.json();

        if (response.ok) {

            alert(
                "Leave request submitted successfully!"
            );

            document.getElementById(
                "leaveId"
            ).value = "";

            document.getElementById(
                "leaveReason"
            ).value = "";

            await loadLeaves();

        } else {

            alert(
                result.message ||
                "Failed to submit leave request"
            );
        }

    } catch (error) {

        console.error(
            "Leave error:",
            error
        );

        alert(
            "Backend connection failed!"
        );
    }
}


// Load Leaves
async function loadLeaves() {

    try {

        const response =
            await fetch(
                `${API_URL}/api/leave`
            );

        if (!response.ok) {
            throw new Error(
                "Failed to load leaves"
            );
        }

        const data =
            await response.json();

        leaveRequests =
            data.map(leave => ({
                leaveId: leave.leave_id,
                id: leave.employee_id,
                reason: leave.reason,
                status: leave.leave_status
            }));

        displayLeaves();

    } catch (error) {

        console.error(
            "Leave loading error:",
            error
        );
    }
}


// Display Leaves
function displayLeaves() {

    const table =
        document.getElementById(
            "leaveTable"
        );

    if (!table) return;

    table.innerHTML = "";

    leaveRequests.forEach(leave => {

        const row =
            document.createElement("tr");

        row.innerHTML = `
            <td>${leave.id}</td>

            <td>${leave.reason}</td>

            <td>${leave.status}</td>

            <td>
                <button
                    onclick="approveLeave(${leave.leaveId})">
                    Approve
                </button>

                <button
                    onclick="rejectLeave(${leave.leaveId})">
                    Reject
                </button>
            </td>
        `;

        table.appendChild(row);
    });

    updateDashboard();
}


// Approve Leave
async function approveLeave(leaveId) {

    await updateLeaveStatus(
        leaveId,
        "Approved"
    );
}


// Reject Leave
async function rejectLeave(leaveId) {

    await updateLeaveStatus(
        leaveId,
        "Rejected"
    );
}


// Update Leave Status
async function updateLeaveStatus(
    leaveId,
    status
) {

    try {

        const response =
            await fetch(
                `${API_URL}/api/leave/status`,
                {
                    method: "POST",
                    headers: {
                        "Content-Type":
                            "application/x-www-form-urlencoded"
                    },
                    body:
                        "leaveId=" +
                        encodeURIComponent(leaveId) +
                        "&status=" +
                        encodeURIComponent(status)
                }
            );

        const result =
            await response.json();

        if (response.ok) {

            alert(
                "Leave " +
                status.toLowerCase() +
                " successfully!"
            );

            await loadLeaves();

        } else {

            alert(
                result.message ||
                "Failed to update leave"
            );
        }

    } catch (error) {

        console.error(
            "Leave status error:",
            error
        );

        alert(
            "Backend connection failed!"
        );
    }
}


// =========================
// SEARCH EMPLOYEE
// =========================

async function searchEmployee() {

    const id =
        document.getElementById(
            "searchEmployee"
        ).value.trim();

    const result =
        document.getElementById(
            "searchResult"
        );

    if (id === "") {

        result.innerHTML = `
            <div class="table-card">
                <p>Please enter Employee ID!</p>
            </div>
        `;

        return;
    }

    try {

        const response =
            await fetch(
                `${API_URL}/api/employees`
            );

        const employeeList =
            await response.json();

        const employee =
            employeeList.find(
                e =>
                    e.employee_id
                        .toLowerCase() ===
                    id.toLowerCase()
            );

        if (employee) {

            result.innerHTML = `
                <div class="table-card">

                    <h3>Employee Found</h3>

                    <br>

                    <p>
                        <strong>Employee ID:</strong>
                        ${employee.employee_id}
                    </p>

                    <p>
                        <strong>Name:</strong>
                        ${employee.name}
                    </p>

                    <p>
                        <strong>Department:</strong>
                        ${employee.department}
                    </p>

                    <p>
                        <strong>Phone:</strong>
                        ${employee.phone}
                    </p>

                    <p>
                        <strong>Status:</strong>
                        ${employee.status}
                    </p>

                </div>
            `;

        } else {

            result.innerHTML = `
                <div class="table-card">
                    <p>Employee not found!</p>
                </div>
            `;
        }

    } catch (error) {

        console.error(error);

        result.innerHTML = `
            <div class="table-card">
                <p>
                    Cannot connect to Java backend!
                </p>
            </div>
        `;
    }
}


// =========================
// DASHBOARD STATISTICS
// =========================

function updateDashboard() {

    let present = 0;
    let absent = 0;

    attendanceRecords.forEach(record => {

        if (
            record.attendance_status ===
            "Present"
        ) {
            present++;
        }

        if (
            record.attendance_status ===
            "Absent"
        ) {
            absent++;
        }
    });

    let pending = 0;

    leaveRequests.forEach(leave => {

        if (
            leave.status ===
            "Pending"
        ) {
            pending++;
        }
    });

    const totalEmployees =
        document.getElementById(
            "totalEmployees"
        );

    if (totalEmployees) {

        totalEmployees.textContent =
            employees.length;
    }

    const presentCount =
        document.getElementById(
            "presentCount"
        );

    if (presentCount) {
        presentCount.textContent =
            present;
    }

    const absentCount =
        document.getElementById(
            "absentCount"
        );

    if (absentCount) {
        absentCount.textContent =
            absent;
    }

    const pendingLeaves =
        document.getElementById(
            "pendingLeaves"
        );

    if (pendingLeaves) {
        pendingLeaves.textContent =
            pending;
    }


    // Reports

    const reportPresent =
        document.getElementById(
            "reportPresent"
        );

    if (reportPresent) {

        reportPresent.textContent =
            attendanceRecords.filter(
                record =>
                    record.attendance_status ===
                    "Present"
            ).length;
    }


    const reportAbsent =
        document.getElementById(
            "reportAbsent"
        );

    if (reportAbsent) {

        reportAbsent.textContent =
            attendanceRecords.filter(
                record =>
                    record.attendance_status ===
                    "Absent"
            ).length;
    }


    const reportLate =
        document.getElementById(
            "reportLate"
        );

    if (reportLate) {

        reportLate.textContent =
            attendanceRecords.filter(
                record =>
                    record.attendance_status ===
                    "Late"
            ).length;
    }


    const reportHalfDay =
        document.getElementById(
            "reportHalfDay"
        );

    if (reportHalfDay) {

        reportHalfDay.textContent =
            attendanceRecords.filter(
                record =>
                    record.attendance_status ===
                    "Half Day"
            ).length;
    }
}


// =========================
// LOGOUT
// =========================

function logout() {

    const confirmLogout =
        confirm(
            "Are you sure you want to logout?"
        );

    if (confirmLogout) {

        window.location.href =
            "index.html";
    }
}


// =========================
// INITIAL LOAD
// =========================

document.addEventListener(
    "DOMContentLoaded",
    async function () {

        await loadEmployees();

        await loadAttendance();

        await loadLeaves();

        updateDashboard();
    }
);