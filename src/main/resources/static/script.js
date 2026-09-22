// API endpoints
const API_BASE_URL = '/api';

const ENDPOINTS = {
    USERS: `${API_BASE_URL}/user/findAll`,
    BINARY_CONTENT: `${API_BASE_URL}/binaryContent/find`
};

// Initialize the application
document.addEventListener('DOMContentLoaded', () => {
    fetchAndRenderUsers();
});

// Fetch users from the API
async function fetchAndRenderUsers() {
    try {
        const response = await fetch(ENDPOINTS.USERS);

        if (!response.ok) {
            throw new Error('Failed to fetch users');
        }

        const result = await response.json();

        // ApiResponse의 data에 실제 사용자 목록이 들어있음
        const users = result.data;

        renderUserList(users);
    } catch (error) {
        console.error('Error fetching users:', error);
    }
}

// Fetch user profile image
async function fetchUserProfile(profileId) {
    try {
        const response = await fetch(
            `${ENDPOINTS.BINARY_CONTENT}?binaryContentId=${profileId}`
        );

        if (!response.ok) {
            throw new Error('Failed to fetch profile');
        }

        const result = await response.json();

        // ApiResponse의 data에 BinaryContent가 들어있음
        const profile = result.data;

        // fileName을 이용해서 MIME 타입 결정
        const contentType = getContentType(profile.fileName);

        // byte[]는 Jackson에 의해 Base64 문자열로 변환됨
        return `data:${contentType};base64,${profile.file}`;
    } catch (error) {
        console.error('Error fetching profile:', error);
        return '/default-avatar.png';
    }
}

// Get MIME type from file extension
function getContentType(fileName) {
    const extension = fileName
        .split('.')
        .pop()
        .toLowerCase();

    const contentTypes = {
        jpg: 'image/jpeg',
        jpeg: 'image/jpeg',
        png: 'image/png',
        gif: 'image/gif',
        webp: 'image/webp',
        bmp: 'image/bmp',
        svg: 'image/svg+xml'
    };

    return contentTypes[extension] || 'application/octet-stream';
}

// Render user list
async function renderUserList(users) {
    const userListElement = document.getElementById('userList');

    userListElement.innerHTML = '';

    for (const user of users) {
        const userElement = document.createElement('div');
        userElement.className = 'user-item';

        // Get profile image URL
        const profileUrl = user.profileId
            ? await fetchUserProfile(user.profileId)
            : '/default-avatar.png';

        userElement.innerHTML = `
            <img src="${profileUrl}" alt="${user.name}" class="user-avatar">

            <div class="user-info">
                <div class="user-name">${user.name}</div>
                <div class="user-email">${user.email}</div>
                <div class="user-nitro">${user.nitroLevel}</div>
            </div>

            <div class="status-badge ${user.isOnline ? 'online' : 'offline'}">
                ${user.isOnline  ? '온라인' : '오프라인'}
            </div>
        `;

        userListElement.appendChild(userElement);
    }
}