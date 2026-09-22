const API_BASE_URL = '/api';
const ENDPOINTS = {
    USERS: `${API_BASE_URL}/user/findAll`,
    BINARY_CONTENT: `${API_BASE_URL}/binaryContent/find`
};

document.addEventListener('DOMContentLoaded', () => {
    fetchAndRenderUsers();
});

async function fetchAndRenderUsers() {
    try {
        const response = await fetch(ENDPOINTS.USERS);
        if (!response.ok) throw new Error('Failed to fetch users');
        const users = await response.json();
        renderUserCount(users.length);
        await renderUserList(users);
    } catch (error) {
        console.error('Error fetching users:', error);
        renderError();
    }
}

async function fetchUserProfile(profileId) {
    try {
        const response = await fetch(`${ENDPOINTS.BINARY_CONTENT}?binaryContentId=${profileId}`);
        if (!response.ok) throw new Error('Failed to fetch profile');
        const profile = await response.json();
        return `data:${profile.fileType};base64,${profile.bytes}`;
    } catch (error) {
        console.error('Error fetching profile:', error);
        return 'default-avatar.png';
    }
}

function renderUserCount(count) {
    const el = document.getElementById('userCount');
    el.textContent = `총 ${count}명의 사용자`;
}

function formatJoinedDate(isoString) {
    if (!isoString) return '';
    const date = new Date(isoString);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `가입일 ${year}.${month}.${day}`;
}

async function renderUserList(users) {
    const userListElement = document.getElementById('userList');
    userListElement.innerHTML = '';

    if (users.length === 0) {
        const empty = document.createElement('div');
        empty.className = 'empty-state';
        empty.textContent = '등록된 사용자가 없습니다.';
        userListElement.appendChild(empty);
        return;
    }

    for (const user of users) {
        const card = document.createElement('div');
        card.className = 'user-card';

        const profileUrl = user.profileId
            ? await fetchUserProfile(user.profileId)
            : 'default-avatar.png';

        const statusClass = user.online ? 'status is-online' : 'status';
        const statusLabel = user.online ? '온라인' : '오프라인';

        card.innerHTML = `
            <img src="${profileUrl}" alt="${user.username}" class="user-avatar">
            <div class="user-info">
                <div class="user-name">${user.username}</div>
                <div class="user-email">${user.email}</div>
                <div class="user-joined">${formatJoinedDate(user.createdAt)}</div>
            </div>
            <div class="${statusClass}">
                <span class="status-dot"></span>
                <span>${statusLabel}</span>
            </div>
        `;

        userListElement.appendChild(card);
    }
}

function renderError() {
    const userListElement = document.getElementById('userList');
    userListElement.innerHTML = '<div class="empty-state">사용자 목록을 불러오지 못했습니다.</div>';
}
