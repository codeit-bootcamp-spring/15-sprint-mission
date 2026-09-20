// API 주소
const API_BASE_URL = '/api';

const ENDPOINTS = {
    USERS: `${API_BASE_URL}/users/findAll`,
    BINARY_CONTENT: `${API_BASE_URL}/binarycontents/find`
};


// HTML이 모두 로딩되면 사용자 목록 조회
document.addEventListener('DOMContentLoaded', () => {
    fetchAndRenderUsers();
});


// 사용자 목록 조회
async function fetchAndRenderUsers() {

    try {

        // 백엔드 API 호출
        const response = await fetch(ENDPOINTS.USERS);

        if (!response.ok) {
            throw new Error('사용자 목록 조회 실패');
        }

        // JSON → JavaScript 객체
        const users = await response.json();

        // 화면에 사용자 출력
        renderUserList(users);

    } catch (error) {

        console.error('사용자 조회 오류:', error);

    }
}


// 프로필 이미지 조회
async function fetchUserProfile(profileId) {

    try {

        const response = await fetch(
            `${ENDPOINTS.BINARY_CONTENT}?binaryContentId=${profileId}`
        );

        if (!response.ok) {
            throw new Error('프로필 이미지 조회 실패');
        }

        const profile = await response.json();

        // BinaryContent의 byte[] content는
        // JSON으로 전달될 때 Base64 문자열로 전달됨
        return `data:${profile.contentType};base64,${profile.content}`;

    } catch (error) {

        console.error('프로필 이미지 조회 오류:', error);

        // 이미지 조회 실패 시 기본 이미지
        return '/images/default-avatar.png';
    }
}


// 사용자 목록 화면 생성
async function renderUserList(users) {

    const userListElement =
        document.getElementById('userList');

    // 기존 내용 제거
    userListElement.innerHTML = '';


    // 사용자 한 명씩 화면 생성
    for (const user of users) {

        const userElement =
            document.createElement('div');

        userElement.className = 'user-item';


        // 프로필 이미지 결정
        let profileUrl;

        if (user.profileId) {

            profileUrl =
                await fetchUserProfile(user.profileId);

        } else {

            profileUrl =
                '/images/default-avatar.png';
        }


        // HTML 생성
        userElement.innerHTML = `

            <img
                src="${profileUrl}"
                alt="${user.username}"
                class="user-avatar"
            >

            <div class="user-info">

                <div class="user-name">
                    ${user.username}
                </div>

                <div class="user-email">
                    ${user.email}
                </div>

            </div>

            <div class="status-badge ${user.online ? 'online' : 'offline'}">

                ${user.online ? '온라인' : '오프라인'}

            </div>
        `;


        // 사용자 목록에 추가
        userListElement.appendChild(userElement);
    }
}