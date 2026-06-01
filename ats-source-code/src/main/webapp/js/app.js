const API_BASE = '/ats/api';

// Initialize app
document.addEventListener('DOMContentLoaded', () => {
    loadDashboard();
    loadJobs();
    loadCandidates();
    loadApplications();
});

// Dashboard Functions
async function loadDashboard() {
    try {
        const [jobsRes, candidatesRes, applicationsRes] = await Promise.all([
            fetch(`${API_BASE}/jobs`),
            fetch(`${API_BASE}/candidates`),
            fetch(`${API_BASE}/applications`)
        ]);

        const jobs = await jobsRes.json();
        const candidates = await candidatesRes.json();
        const applications = await applicationsRes.json();

        document.getElementById('totalJobs').textContent = jobs.length;
        document.getElementById('totalCandidates').textContent = candidates.length;
        document.getElementById('totalApplications').textContent = applications.length;

        const hired = applications.filter(app => app.status === 'HIRED').length;
        document.getElementById('totalHired').textContent = hired;
    } catch (error) {
        console.error('Error loading dashboard:', error);
    }
}

// Job Functions
async function loadJobs() {
    try {
        const response = await fetch(`${API_BASE}/jobs`);
        const jobs = await response.json();
        const jobsList = document.getElementById('jobsList');
        jobsList.innerHTML = '';

        jobs.forEach(job => {
            const jobCard = document.createElement('div');
            jobCard.className = 'bg-white p-4 rounded-lg shadow';
            jobCard.innerHTML = `
                <div class="flex justify-between items-start">
                    <div>
                        <h3 class="text-xl font-bold text-gray-800">${job.title}</h3>
                        <p class="text-gray-600">${job.department} - ${job.location}</p>
                        <p class="text-sm text-gray-500 mt-2">${job.description || 'No description'}</p>
                        <span class="inline-block mt-2 px-3 py-1 rounded-full text-sm font-semibold
                            ${job.status === 'OPEN' ? 'bg-green-100 text-green-800' : 
                              job.status === 'CLOSED' ? 'bg-red-100 text-red-800' : 
                              'bg-yellow-100 text-yellow-800'}">
                            ${job.status}
                        </span>
                    </div>
                    <div class="flex space-x-2">
                        <button onclick="editJob(${job.id})" class="text-blue-600 hover:text-blue-800">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button onclick="deleteJob(${job.id})" class="text-red-600 hover:text-red-800">
                            <i class="fas fa-trash"></i>
                        </button>
                    </div>
                </div>
            `;
            jobsList.appendChild(jobCard);
        });
    } catch (error) {
        console.error('Error loading jobs:', error);
    }
}

function showJobForm() {
    document.getElementById('jobModal').classList.remove('hidden');
}

function closeJobForm() {
    document.getElementById('jobModal').classList.add('hidden');
    document.getElementById('jobForm').reset();
}

async function submitJobForm(event) {
    event.preventDefault();
    const job = {
        title: document.getElementById('jobTitle').value,
        description: document.getElementById('jobDescription').value,
        department: document.getElementById('jobDepartment').value,
        location: document.getElementById('jobLocation').value,
        status: 'OPEN'
    };

    try {
        const response = await fetch(`${API_BASE}/jobs`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(job)
        });

        if (response.ok) {
            closeJobForm();
            loadJobs();
            loadDashboard();
        }
    } catch (error) {
        console.error('Error creating job:', error);
    }
}

async function deleteJob(id) {
    if (confirm('Are you sure you want to delete this job?')) {
        try {
            await fetch(`${API_BASE}/jobs/${id}`, { method: 'DELETE' });
            loadJobs();
            loadDashboard();
        } catch (error) {
            console.error('Error deleting job:', error);
        }
    }
}

// Candidate Functions
async function loadCandidates() {
    try {
        const response = await fetch(`${API_BASE}/candidates`);
        const candidates = await response.json();
        const candidatesList = document.getElementById('candidatesList');
        candidatesList.innerHTML = '';

        candidates.forEach(candidate => {
            const candidateCard = document.createElement('div');
            candidateCard.className = 'bg-white p-4 rounded-lg shadow';
            candidateCard.innerHTML = `
                <div class="flex justify-between items-start">
                    <div>
                        <h3 class="text-xl font-bold text-gray-800">${candidate.firstName} ${candidate.lastName}</h3>
                        <p class="text-gray-600">${candidate.email}</p>
                        <p class="text-gray-600">${candidate.phone || 'No phone'}</p>
                        ${candidate.notes ? `<p class="text-sm text-gray-500 mt-2">${candidate.notes}</p>` : ''}
                    </div>
                    <div class="flex space-x-2">
                        <button onclick="editCandidate(${candidate.id})" class="text-blue-600 hover:text-blue-800">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button onclick="deleteCandidate(${candidate.id})" class="text-red-600 hover:text-red-800">
                            <i class="fas fa-trash"></i>
                        </button>
                    </div>
                </div>
            `;
            candidatesList.appendChild(candidateCard);
        });
    } catch (error) {
        console.error('Error loading candidates:', error);
    }
}

function showCandidateForm() {
    document.getElementById('candidateModal').classList.remove('hidden');
}

function closeCandidateForm() {
    document.getElementById('candidateModal').classList.add('hidden');
    document.getElementById('candidateForm').reset();
}

async function submitCandidateForm(event) {
    event.preventDefault();
    const candidate = {
        firstName: document.getElementById('candidateFirstName').value,
        lastName: document.getElementById('candidateLastName').value,
        email: document.getElementById('candidateEmail').value,
        phone: document.getElementById('candidatePhone').value
    };

    try {
        const response = await fetch(`${API_BASE}/candidates`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(candidate)
        });

        if (response.ok) {
            closeCandidateForm();
            loadCandidates();
            loadDashboard();
        }
    } catch (error) {
        console.error('Error creating candidate:', error);
    }
}

async function deleteCandidate(id) {
    if (confirm('Are you sure you want to delete this candidate?')) {
        try {
            await fetch(`${API_BASE}/candidates/${id}`, { method: 'DELETE' });
            loadCandidates();
            loadDashboard();
        } catch (error) {
            console.error('Error deleting candidate:', error);
        }
    }
}

// Application Functions
async function loadApplications() {
    try {
        const response = await fetch(`${API_BASE}/applications`);
        const applications = await response.json();

        const statuses = ['APPLIED', 'SCREENING', 'INTERVIEW', 'OFFER'];
        statuses.forEach(status => {
            const list = document.getElementById(`${status.toLowerCase()}List`);
            list.innerHTML = '';
            const filtered = applications.filter(app => app.status === status);
            filtered.forEach(app => {
                const appCard = document.createElement('div');
                appCard.className = 'bg-gray-100 p-2 rounded text-sm cursor-pointer hover:bg-gray-200';
                appCard.innerHTML = `
                    <div class="font-semibold">${app.candidate.firstName} ${app.candidate.lastName}</div>
                    <div class="text-gray-600">${app.job.title}</div>
                `;
                list.appendChild(appCard);
            });
        });
    } catch (error) {
        console.error('Error loading applications:', error);
    }
}
