const API_BASE_URL = '/api';
const ENDPOINTS = {
    USERS: `${API_BASE_URL}/users`,
    BINARY_CONTENT: `${API_BASE_URL}/binaryContents`
};

document.addEventListener('DOMContentLoaded', () => {
    fetchAndRenderUsers();
});

async function fetchAndRenderUsers() {
    try {
        const response = await fetch(ENDPOINTS.USERS);
        if (!response.ok) throw new Error('Failed to fetch users');
        const users = await response.json();
        renderUserList(users);
    } catch (error) {
        console.error('Error fetching users:', error);
    }
}

async function fetchUserProfile(profileId) {
    try {
        const response = await fetch(`${ENDPOINTS.BINARY_CONTENT}/${profileId}`);
        if (!response.ok) throw new Error('Failed to fetch profile');
        const profile = await response.json();

        return `data:${profile.fileType};base64,${profile.bytes}`;
    } catch (error) {
        console.error('Error fetching profile:', error);
        return '/default-avatar.png';
    }
}

async function renderUserList(users) {
    const userListElement = document.getElementById('userList');
    userListElement.innerHTML = '';

    for (const user of users) {
        const userElement = document.createElement('div');
        userElement.className = 'user-item';

        const profileUrl = user.profileId ?
            await fetchUserProfile(user.profileId) :
            '/default-avatar.png';

        userElement.innerHTML = `
            <img src="${profileUrl}" alt="${user.userName}" class="user-avatar">
            <div class="user-info">
                <div class="user-name">${user.userName}</div>
                <div class="user-email">${user.email}</div>
            </div>
            <div class="status-badge ${user.online ? 'online' : 'offline'}">
                ${user.online ? '온라인' : '오프라인'}
            </div>
        `;

        userListElement.appendChild(userElement);
    }
}